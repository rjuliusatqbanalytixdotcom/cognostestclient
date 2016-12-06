package com.qbanalytix.cognostestclient.resources.model;

public class ClientInformation {
	
	public static final ClientInformation INSTANCE = new ClientInformation();

	private String hostname;
	private String ipAddress;
	private int port;

	public static void init(String hostname, String ipAddress, int port) {
		INSTANCE.hostname = hostname;
		INSTANCE.ipAddress = ipAddress;
		INSTANCE.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}
}