package gc.apiClient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.webclient.WebClientApp;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceWebClient implements InterfaceWebClient {

	@Override
	public String GetApiRequet(String endpoint) {

		log.info("===== GetApiRequet =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "GET");
		result = webClientExample.makeApiRequest();

		System.out.println("GetApiRequet 요청 후 결과 값 : " + result);

		return result;
	}

	@Override
	public String GetStatusApiRequet(String endpoint, String campaignId) {// path parameter 'campaignId'

		log.info("===== GetStatusApiRequet =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "GET");
		result = webClientExample.makeApiRequest(campaignId);

		log.info("GetStatusApiRequet 요청 후 결과 값 : {}",result);

		return result;
	}

	@Override // "/api/v2/outbound/campaigns/{campaignId}"
	public String GetCampaignsApiRequet(String endpoint, String campaignId) {// path parameter 'campaignId'

		log.info("===== GetCampaignsApiRequet =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "GET");
		result = webClientExample.makeApiRequest(campaignId);

		log.info("GetStatusApiRequet 요청 후 결과 값 : {}",result);

		return result;
	}

	@Override
	public String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {// path parameter
																									// 'contactListId','contactId'

		log.info("===== GetContactLtApiRequet =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "GET");
		result = webClientExample.makeApiRequest(contactListId, contactId);

		System.out.println("GetContactLtApiRequet 요청 후 결과 값 : " + result);

		return result;
	}

	@Override // api/v2/outbound/contactlists/{contactListId}/contacts
	public String PostContactLtApiRequet(String endpoint, String contactListId, String msg) {// path parameter
																								// 'contactListId','contactId'
		log.info("===== PostContactLtApiRequet =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "POST");
		result = webClientExample.makeApiRequest34(contactListId, msg);

		log.info("PostContactLtApiRequet 요청 후 결과 값 : {}",result);
		return result;
	}

	
	
	@Override // "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk"
	public String PostContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) {// path parameter
		
		log.info("===== PostContactLtApiBulk =====");
		
		String result = "";

		WebClientApp webClientExample = new WebClientApp(endpoint, "POST");
		result = webClientExample.makeApiRequest56(contactListId, cskes);

		log.info("PostContactLtApiBulk 요청 후 결과 값 : {}",result);

		return result;
	}

}
