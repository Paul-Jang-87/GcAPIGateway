package gc.apiClient.service;

import org.springframework.stereotype.Service;

import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.webclient.WebClientApp;

@Service
public class ServiceWebClient implements InterfaceWebClient {

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest();
		
		System.out.println("GetApiRequet 요청 후 결과 값 : "+result);
		
		return result;
	}
	
	@Override
	public String GetStatusApiRequet(String endpoint, String campaignId) {//path parameter 'campaignId'
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest(campaignId);
		
		System.out.println("GetStatusApiRequet 요청 후 결과 값 : "+result);
		
		return result;
	}
	
	@Override // "/api/v2/outbound/campaigns/{campaignId}"
	public String GetCampaignsApiRequet(String endpoint, String campaignId) {//path parameter 'campaignId'
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest(campaignId);
		
		System.out.println("GetStatusApiRequet 요청 후 결과 값 : "+result);
		
		return result;
	}
	
	@Override
	public String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {//path parameter 'contactListId','contactId'
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET");
		result = webClientExample.makeApiRequest(contactListId,contactId);
		
		System.out.println("GetContactLtApiRequet 요청 후 결과 값 : "+result);
		
		return result;
	}
	
	
	@Override  //    api/v2/outbound/contactlists/{contactListId}/contacts
	public String PostContactLtApiRequet(String endpoint, String contactListId, String msg) {//path parameter 'contactListId','contactId'
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"POST");
		result = webClientExample.makeApiRequest34(contactListId, msg);
		
		System.out.println("PostContactLtApiRequet 요청 후 결과 값 : "+result);
		
		return result;
	}

}
