package com.rest273.client;

public class ClientObject {
	
	private String clientID;
	private int time;
	
	//constructor
	public ClientObject(String clientID, int time) {
		super();
		this.clientID = clientID;
		this.time = time;
	}

	
	//getter and setter
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}


}
