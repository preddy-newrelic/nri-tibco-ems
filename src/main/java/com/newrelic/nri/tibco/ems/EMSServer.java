package com.newrelic.nri.tibco.ems;

/*
 * Program to pull metric data from TIBCO EMS
 * New Relic, Inc.  ALL RIGHTS RESERVED
 * Requires TIBCO libraries linked as tibjms.jar, tibjmsadmin.jar, jms-2.0.jar
 * Also the newrelic metrics_publish.2.0.1.jar
 * Created 2015-12-09
 * Modification History:
 * 
 */

import java.util.ArrayList;
import java.util.List;

public class EMSServer {
	private String host;
	private int port = 7222;
	private String username;
	private String password;
	private String name;
	private List<String> queueIgnores;
	private List<String> topicIgnores;
	private Boolean flagIncludeDynamicQueues;
	private Boolean flagIncludeDynamicTopics;

	// SSL
	private boolean useSSL;
	private String sslIdentityFile;
	private String sslIdentityPassword;
	private String sslTrustedCerts;
	
	private String sslCiphers = null;
	private boolean sslDebug = false;
	private boolean sslVerifyHost = false;
	private boolean sslVerifyHostName = false;
	private String sslVendor = null;

	public EMSServer(String name, String host, int port, String username, String password) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		queueIgnores = new ArrayList<String>();
		topicIgnores = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getEMSURL() {
		String emsURL = null;
		emsURL = "tcp://" + host + ":" + port;
		return (emsURL);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<String> getQueueIgnores() {
		return queueIgnores;
	}

	public void addToQueueIgnores(String queueIgnore) {
		queueIgnores.add(queueIgnore);
	}

	public void removeFromQueueIgnores(String queueIgnore) {
		queueIgnores.remove(queueIgnore);
	}

	public List<String> getTopicIgnores() {
		return topicIgnores;
	}

	public void addToTopicIgnores(String topicIgnore) {
		queueIgnores.add(topicIgnore);
	}

	public void removeFromTopicIgnores(String topicIgnore) {
		queueIgnores.remove(topicIgnore);
	}

	public void setFlagIncludeDynamicQueues(Boolean dynQValue) {
		flagIncludeDynamicQueues = dynQValue;
	}

	public Boolean getFlagIncludeDynamicQueues() {
		return flagIncludeDynamicQueues;
	}

	public void setFlagIncludeDynamicTopics(Boolean dynTValue) {
		flagIncludeDynamicTopics = dynTValue;
	}

	public Boolean getFlagIncludeDynamicTopics() {
		return flagIncludeDynamicTopics;
	}

	public String getSslIdentityFile() {
		return sslIdentityFile;
	}

	public void setSslIdentityFile(String sslIdentityFile) {
		this.sslIdentityFile = sslIdentityFile;
	}

	public String getSslIdentityPassword() {
		return sslIdentityPassword;
	}

	public void setSslIdentityPassword(String sslIdentityPassword) {
		this.sslIdentityPassword = sslIdentityPassword;
	}

	public String getSslTrustedCerts() {
		return sslTrustedCerts;
	}

	public void setSslTrustedCerts(String sslTrustedCerts) {
		this.sslTrustedCerts = sslTrustedCerts;
	}

	public String getSslCiphers() {
		return sslCiphers;
	}

	public void setSslCiphers(String sslCiphers) {
		this.sslCiphers = sslCiphers;
	}

	public Boolean getSslDebug() {
		return sslDebug;
	}

	public void setSslDebug(boolean sslDebug) {
		this.sslDebug = sslDebug;
	}

	public Boolean getSslVerifyHost() {
		return sslVerifyHost;
	}

	public void setSslVerifyHost(boolean sslVerifyHost) {
		this.sslVerifyHost = sslVerifyHost;
	}

	public Boolean getSslVerifyHostName() {
		return sslVerifyHostName;
	}

	public void setSslVerifyHostName(boolean sslVerifyHostName) {
		this.sslVerifyHostName = sslVerifyHostName;
	}

	public String getSslVendor() {
		return sslVendor;
	}

	public void setSslVendor(String sslVendor) {
		this.sslVendor = sslVendor;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

}
