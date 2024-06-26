package gc.apiClient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.webclient.WebClientApp;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceWebClient implements InterfaceWebClient {
	
	private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL_MS = 1000; // 1 second (adjust as needed)

	@Override
	public String GetApiRequet(String endpoint,int pagenumber) {

		log.info("====== Method : GetApiRequet ======");

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", "sortBy", "dateCreated", "sortOrder", "descending", "pageSize",100, "pageNumber", pagenumber);

		return result;
	}

	@Override
	public String GetStatusApiRequet(String endpoint, String campaignId) {

		log.info("====== Method : GetStatusApiRequet ======");
		log.info("Endpoint : {} => {}", endpoint, "/api/v2/outbound/campaigns/{campaignId}/stats");
		log.info("campaignId : {}", campaignId);

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", campaignId);

		return result;
	}

	@Override // "/api/v2/outbound/campaigns/{campaignId}"
	public String GetCampaignsApiRequet(String endpoint, String campaignId) {// path parameter 'campaignId'

		log.info("====== Method : GetCampaignsApiRequet ======");

		
		String result = "";

        int attempt = 0;
        boolean success = false;
        
        while (attempt < MAX_RETRIES && !success) {
            try {
                WebClientApp webClient = new WebClientApp();
                result = webClient.makeApiRequest(endpoint, "GET", campaignId);
                success = true; // Mark success if no exception is thrown
            } catch (Exception e) {
                log.error("Attempt {} failed: {}", attempt + 1, e.getMessage());
                attempt++;
                if (attempt < MAX_RETRIES) {
                    log.info("Retrying after {} ms...", RETRY_INTERVAL_MS);
                    try {
                        Thread.sleep(RETRY_INTERVAL_MS);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

		return result;
	}

	@Override
	public String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {// path parameter
																									// 'contactListId','contactId'

		log.info("====== Method : GetContactLtApiRequet ======");

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", contactListId, contactId);

		return result;
	}

	@Override // api/v2/outbound/contactlists/{contactListId}/contacts/bulk
	public String PostContactLtApiRequet(String endpoint, String contactListId, List<String> msg) {// path parameter
																									// 'contactListId','contactId'
		log.info("====== Method : PostContactLtApiRequet ======");

		String result = "";
		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest34(endpoint, contactListId, msg.toString());

		int cnt = 3;
		int retryCount = 1;
		while (result == null && retryCount < cnt) {

			log.info("Retrying count : {}", retryCount);
			log.info("Retrying...");
			retryCount++;
			result = webClient.makeApiRequest34(endpoint, contactListId, msg.toString());
			log.error("Result after retrying : {}", result);
		}

		if (result == null && retryCount >= cnt) {
			log.error("Final result : {}", result);
		}

		if (result != null) {
			result = "성공";
		}

		log.info("PostContactLtApiRequet 요청 후 결과 값 : {}, 시도횟수 : {} || 컨텍리스트 아이디 : {}", result, retryCount,contactListId);
		return result;
	}

	@Override // "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk"
	public String PostContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) {// path parameter

		log.info("====== Method : PostContactLtApiBulk ======");

		String result = "";
		log.info("Endpoint : {} => {}", endpoint, "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk");
		log.info("contactListId : {}", contactListId);
		log.info("cskes : {}", cskes.toString());

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest56(endpoint, contactListId, cskes);

		log.info("PostContactLtApiBulk 요청 후 결과 값 result : {}", result);
		return result;
	}

	@Override
	public Void PostContactLtClearReq(String endpoint, String contactListId) {

		log.info("====== Method : PostContactLtClearReq ======");

		log.info("Endpoint : /api/v2/outbound/contactlists/{contactListId}/clear");
		log.info("contactListId : {}", contactListId);
		WebClientApp webClient = new WebClientApp();
		webClient.makeApiRequest(endpoint, "POST", contactListId);

		return null;
	}

	@Override
	public String DelContacts(String endpoint, String contactListId, List<String> msg) throws Exception {
		log.info("====== Method : DelContacts ======");
		log.info("Incoming message : {}", msg.toString());

		String result = "";
		WebClientApp webClient = new WebClientApp();

		String rst = msg.toString();
		rst = rst.substring(1, rst.length() - 1);
		result = webClient.ApionlyfordelContacts(endpoint, "DELETE", contactListId, rst);

		if (result == null)
			log.error("{} 정상적으로 삭제 되었습니다.", rst);
		else
			log.error("에러가 발생했습니다. : {}", result);

		return result;
	}

}
