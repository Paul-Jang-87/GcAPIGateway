package gc.apiClient.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.webclient.WebClientApp;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceWebClient implements InterfaceWebClient {

	@Override
	public String GetApiRequet(String endpoint) {

		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : GetApiRequet ======");
		
		String result = "";

		WebClientApp webClient = new WebClientApp(endpoint, "GET");
		result = webClient.makeApiRequest("sortBy","dateCreated","sortOrder","descending");

		log.info("GetApiRequet 요청 후 결과 값 : {}",result);
		log.info("====== End GetApiRequet ======");
		return result;
	}
	
	@Override
	public String GetDivisionName(String endpoint, String divisionid) {// path parameter 'divisionid'

		log.info("===== GetDivisionName =====");
		
		String result = "";

		WebClientApp webClient = new WebClientApp(endpoint, "GET");
		result = webClient.makeApiRequest(divisionid);

		log.info("GetDivisionName 요청 후 결과 값 : {}",result);

		return result;
	}
	

	@Override
	public String GetStatusApiRequet(String endpoint, String campaignId) {

		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : GetStatusApiRequet ======");
		log.info("Endpoint : {} => {}",endpoint,"/api/v2/outbound/campaigns/{campaignId}/stats");
		log.info("campaignId : {}",campaignId);
		
		String result = "";

		WebClientApp webClient = new WebClientApp(endpoint, "GET");
		result = webClient.makeApiRequest(campaignId);

		log.info("GetStatusApiRequet 요청 후 결과 값 rs : {}",result);

		log.info("===== End GetStatusApiRequet =====");
		return result;
	}

	@Override // "/api/v2/outbound/campaigns/{campaignId}"
	public String GetCampaignsApiRequet(String endpoint, String campaignId) {// path parameter 'campaignId'

		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : GetCampaignsApiRequet ======");
		
		String result = "";

		WebClientApp webClient = new WebClientApp(endpoint, "GET");
		result = webClient.makeApiRequest(campaignId);

		log.info("GetStatusApiRequet 요청 후 결과 값 : {}",result);

		log.info("====== End GetCampaignsApiRequet ======");
		return result;
	}

	@Override
	public String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {// path parameter
																									// 'contactListId','contactId'

		log.info("===== GetContactLtApiRequet =====");
		
		String result = "";

		WebClientApp webClient = new WebClientApp(endpoint, "GET");
		result = webClient.makeApiRequest(contactListId, contactId);

		System.out.println("GetContactLtApiRequet 요청 후 결과 값 : " + result);

		return result;
	}

	@Override // api/v2/outbound/contactlists/{contactListId}/contacts/bulk
	public String PostContactLtApiRequet(String endpoint, String contactListId, List<String> msg) {// path parameter
																								// 'contactListId','contactId'
		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : PostContactLtApiRequet ======");
		String result = "";
		
		log.info("PostContactLtApiRequet message before send to G.C : {}",msg.toString());
		int byteSize = msg.toString().getBytes().length;
	    log.info("Bytes size of PostContactLtApiRequet message: {}", byteSize);

		WebClientApp webClient = new WebClientApp(endpoint, "POST");
		result = webClient.makeApiRequest34(contactListId, msg.toString());

		log.info("PostContactLtApiRequet 요청 후 결과 값 : {}",result);
		log.info("====== End PostContactLtApiRequet ======");
		return result;
	}
	
	

	
	
	@Override // "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk"
	public String PostContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) {// path parameter
		
		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : PostContactLtApiBulk ======");
		
		String result = "";
		log.info("Endpoint : {} => {}",endpoint,"/api/v2/outbound/contactlists/{contactListId}/contacts/bulk");
		log.info("contactListId : {}",contactListId);
		log.info("cskes : {}",cskes.toString());

		WebClientApp webClient = new WebClientApp(endpoint, "POST");
		result = webClient.makeApiRequest56(contactListId, cskes);

		log.info("PostContactLtApiBulk 요청 후 결과 값 result : {}",result);
		log.info("====== End PostContactLtApiBulk ======");
		return result;
	}

	@Override
	public Void PostContactLtClearReq(String endpoint,String contactListId) {
		
		log.info(" ");
		log.info("====== ClassName : ServiceWebClient & Method : PostContactLtClearReq ======");
		
		log.info("Endpoint : /api/v2/outbound/contactlists/{contactListId}/clear");
		log.info("contactListId : {}",contactListId);
		WebClientApp webClient = new WebClientApp(endpoint, "POST");
		webClient.makeApiRequest(contactListId);

		log.info("====== End PostContactLtClearReq ======");
		
		return null;
	}


}
