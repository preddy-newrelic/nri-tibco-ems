package com.newrelic.nri.tibco.ems;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
	
	protected static final String INSIGHTS_MODE = "insights_mode";
	protected static final String INFRA_MODE = "infra_mode";
	
	protected static final String ACCOUNT_ID = "account_id";
	protected static final String COLLECTOR_URL = "collector_url";
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	protected static Map<String, Object> globalProperties;
	protected String accountId = null;
	protected String collectorUrl = null;
	
	public static void main(String[] argv) {
		Args args = new Args();
		JCommander jct = JCommander.newBuilder().addObject(args).build();
		jct.parse(argv);
		if (args.isHelp()) {
			jct.usage();
		}

		String configFile = System.getenv("CONFIG_FILE");
		if (configFile == null) {
			configFile = args.getConfigFile();
			if (configFile == null) {
				System.err.println("Error: Please specify a valid config_file argument");
				System.exit(-1);
			}
		}
		
		Main m = new Main();
		try {
			m.setup(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void setup(String pluginConfigFileName) throws Exception {
		Map<String, Object> pluginConfigProperties = null;
		File pluginConfigFile = new File(pluginConfigFileName);
		if (pluginConfigFile.exists()) {
			Reader reader = new FileReader(pluginConfigFile);
			pluginConfigProperties = objectMapper.readValue(reader, new TypeReference<Map<String, Object>>() {});
		} else {
			String msg = "Config File [" + pluginConfigFileName + "] does not exist";
			throw new Exception(msg);
		}

		Object globalObj = pluginConfigProperties.get("global");
		if ((globalObj != null) && (globalObj instanceof Map)) {
			globalProperties = (Map<String, Object>) globalObj;
			
			collectorUrl  = (String) globalProperties.get(COLLECTOR_URL);
			if (collectorUrl == null) {
				accountId = (String) globalProperties.get(ACCOUNT_ID);
				if (accountId == null || accountId.isEmpty()) {
					String msg = ACCOUNT_ID + " is empty or null, unable to start";
					throw new Exception(msg);
				} else {
					accountId = accountId.trim();
					//logger.info("Using account ID: " + accountId);
				}
			} else {
				accountId = null;
				collectorUrl = collectorUrl.trim();
				//logger.info("Using Collector URL: " + collectorUrl);
			}
			
		} else {
			//String msg = "plugin.json 'global' configuration entry must be an map with keys of type string";
			//throw new Exception(msg);
		}

		Object agentsObj = pluginConfigProperties.get("instances");
		if (agentsObj == null) {
			// For backward compatibility, try to see if agent is defined. If so, use it.
			agentsObj = pluginConfigProperties.get("agents");

			if (agentsObj == null) {
				throw new Exception("plugin.json 'instances' configuration entry must not be null");
			}
		}
		if (!(agentsObj instanceof List)) {
			String msg = "plugin.json 'instances' configuration entry must be a list";
			throw new Exception(msg);
		}

		List agents = (ArrayList) agentsObj;
		for (Iterator agentIterator = agents.iterator(); agentIterator.hasNext();) {
			Map<String, Object> agentProperties = (Map<String, Object>) agentIterator.next();

			String agentName = (String) agentProperties.get("name");
			if (agentName == null) {
				String msg = "'name' is a required property for each agent config";
				throw new Exception(msg);
			}

			String hostName = (String) agentProperties.get("host");
			if (hostName == null) {
				hostName = (String) agentProperties.get("hostname");
				if (hostName == null) {
					try {
						hostName = InetAddress.getLocalHost().getHostName();
					} catch (Throwable t) {
						hostName = "localhost";
					}
				}
			}

			try {
				EMSMonitorFactory factory = new EMSMonitorFactory();
				EMSMonitor monitor = factory.createAgent(agentProperties);
				JSONMetricReporter metricReporter = new JSONMetricReporter();
				monitor.populateMetrics(metricReporter);
				JSONObject reportJson = metricReporter.getJSON();
				String jsonStr = reportJson.toString();
				System.out.println(jsonStr);
			} catch (Exception e) {
				e.printStackTrace();
				Utils.reportError("Error occurred while getting metrics",e);
			}

		}
	}

}
