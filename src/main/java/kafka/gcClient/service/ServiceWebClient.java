package kafka.gcClient.service;

import org.springframework.stereotype.Service;

import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.interfaceCollection.InterfaceWebClient;
import kafka.gcClient.webclient.WebClientApp;

@Service
public class ServiceWebClient implements InterfaceWebClient {
	

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest();
		
		System.out.println(result);
		
		return result;
	}
	
	@Override
	public String GetStatusApiRequet(String endpoint, String campaignId) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest(campaignId);
		
		System.out.println(result);
		
		return result;
	}
	
	@Override
	public String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest(contactListId,contactId);
		
		System.out.println(result);
		
		return result;
	}

}
