package com.rest273.client;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class V1_client {
	
	private JSONArray objectList;
	private String uRN;
	private String bootServerURL;
/*
	public V1_client(JSONArray  objectList)
	{
		this.objectList=objectList;
	}
*/	

	public String getURN() {
		return uRN;
	}


	public void setURN(String uRN) {
		this.uRN = uRN;
	}


	public JSONArray getObjectList() {
		return objectList;
	}


	public void setObjectList(JSONArray objectList) {
		this.objectList = objectList;
	}



	
	private JSONArray initialServer() throws JSONException {
		
		JSONArray serverArray = new JSONArray();
		
		JSONObject object = new JSONObject();
		object.put("ObjectName","LWM2M Server");
		object.put("ObjectID","1");
		object.put("ObjectInstanceID","0");
		serverArray.put(object);
		
		JSONObject URI = new JSONObject();
		URI.put("ResourceName","LWM2M Server URI");
		URI.put("ResourceID","0");
		URI.put("ResourceInstanceID","");
		URI.put("Value","");
		serverArray.put(URI);
				
		return serverArray;
	}
	
	private JSONArray initialDevice() throws JSONException {
		
		JSONArray deviceArray = new JSONArray();
		
		JSONObject object = new JSONObject();
		object.put("ObjectName","Device");
		object.put("ObjectID","3");
		object.put("ObjectInstanceID","0");
		deviceArray.put(object);
		
		JSONObject manufacturer = new JSONObject();
		manufacturer.put("ResourceName","Manufacturer");
		manufacturer.put("ResourceID","0");
		manufacturer.put("ResourceInstanceID","");
		manufacturer.put("Value","LG");
		deviceArray.put(manufacturer);
		
		JSONObject mondelNumber = new JSONObject();
		mondelNumber.put("ResourceName","Model Number");
		mondelNumber.put("ResourceID","1");
		mondelNumber.put("ResourceInstanceID","");
		mondelNumber.put("Value","Smart Refrigerator 1.0");
		deviceArray.put(mondelNumber);
		
		JSONObject serialNumber = new JSONObject();
		serialNumber.put("ResourceName","Serial Number");
		serialNumber.put("ResourceID","2");
		serialNumber.put("ResourceInstanceID","");
		serialNumber.put("Value","zx90003k31");
		deviceArray.put(serialNumber);
		return deviceArray;
	}
	
	public void bootStrap() throws JSONException{
		System.out.println("Start to boot and try to register to bootstrap server.");
		if (bootServerURL!=null){
			
			Client client = Client.create();
			WebResource webResource = client.resource(bootServerURL);
			
			JSONObject uRNobject = new JSONObject();
			uRNobject.put("clientESN_URN", uRN);
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, uRNobject);
			
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			
			JSONObject responseJson = new JSONObject(response.getEntity(String.class));
			
			objectList.getJSONArray(0).getJSONObject(1).remove("Value");
			objectList.getJSONArray(0).getJSONObject(1).put("Value",responseJson.getString("serverURI"));
			System.out.println("Boot successfully! LWM2M Server URI is: " + responseJson.getString("serverURI"));
		}
		else{
			System.out.println("Can not find bootstrap server URL. Bootstrap failed!");
		}
	}
	
	
	public void register() throws JSONException{
		System.out.println("Start to register to LWM2M server.");
		String m2mServerURL = objectList.getJSONArray(0).getJSONObject(1).getString("Value");
		if (m2mServerURL!=null){
			
			m2mServerURL += "/post";
			
			Client client = Client.create();
			WebResource webResource = client.resource(m2mServerURL);
			JSONObject registerObject = new JSONObject();
			registerObject.put("Endpoint Client Name", uRN);
			
			JSONArray JsonArray = new JSONArray();
			JSONObject iterObject;
			String objectinsID;
			for(int i=0;i < objectList.length();i++)
			{
				objectinsID=objectList.getJSONArray(i).getJSONObject(0).getString("ObjectID") + "/"+objectList.getJSONArray(i).getJSONObject(0).getString("ObjectInstanceID");
			
				iterObject=new JSONObject();
				iterObject.put("n", objectinsID);
				JsonArray.put(iterObject);
			}
			
			registerObject.put("e",JsonArray);
			
             ClientResponse response = webResource.type("application/json").post(ClientResponse.class, registerObject);
			
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			
			System.out.println("Response from LWM2M server: " + response.getEntity(String.class));
		}
		else {
			System.out.println("Can not find LWM2M server URL. Register failed!");
		}
		
	}
	
	
	public static void main(String[] args) throws JSONException {
		

		V1_client client = new V1_client();
		client.setURN("00000002");
		client.bootServerURL="http://localhost:8080/com.rest273.bootstrapServer/rest/V1/server/post";
		
		JSONArray serverArray = new JSONArray();
		JSONArray deviceArray = new JSONArray();
		JSONArray objectList = new JSONArray();
		serverArray = client.initialServer();
		deviceArray = client.initialDevice();
		objectList.put(serverArray);
		objectList.put(deviceArray);
		client.setObjectList(objectList);
		client.bootStrap();
		System.out.println(objectList.getJSONArray(0).getJSONObject(1).getString("Value"));
		client.register();				

	}
	
	
	
	
	
	public static void clientRest(ClientObject clientInput) {
		try {

			Client client = Client.create();

			WebResource webResource = client
					.resource("http://localhost:8080/com.rest273.assignment2b/rest/V1/server/post");

			JSONObject obj = new JSONObject();
			boolean iter = true;
			int i = 0;
			while (iter == true) {
				try {

					clientInput.setTime(i);
					String key_name = "clientID";
					String clientVal = clientInput.getClientID();
					obj.put(key_name, clientVal);
					String data_name = "times";
					int timesVal = clientInput.getTime();
					obj.put(data_name, timesVal);

				} catch (Exception e) {
					e.printStackTrace();
				}
				ClientResponse response = webResource.type("application/json")
						.post(ClientResponse.class, obj);

				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				System.out.print(clientInput.getClientID()
						+ " response from Server: ");
				String output = response.getEntity(String.class);
				System.out.println(output);
				try {
					Thread.sleep(3000); // 1000 milliseconds is one second.
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				i++;
			}
		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}
}
