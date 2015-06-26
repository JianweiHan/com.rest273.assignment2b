package com.rest273.client;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class V1_client {

	public static void main(String[] args) {
		ClientObject client = new ClientObject("client1", 0);

		clientRest(client);

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
