package gc.apiClient.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.webclient.WebClientApp;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceWebClient implements InterfaceWebClient {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL_MS = 1000; // 1 second (adjust as needed)

	@Override
	public String getApiReq(String endpoint,int pagenumber) {

		log.info("====== Method : getApiRequet {} ======", endpoint);

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", "sortBy", "dateCreated", "sortOrder", "descending", "pageSize",100, "pageNumber", pagenumber);

		return result;
	}

	@Override
	public String getStatusApiReq(String endpoint, String campaignId) {

		log.info("====== Method : getStatusApiRequet {} ======", endpoint);

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", campaignId);

		return result;
	}
	

	@Override 
	public String getCampaignsApiReq(String endpoint, String campaignId) {
		/*
		 * <Genesys API 호출> - 캠페인 조회 [GET]/api/v2/outbound/campaigns/{campaignId}
		 */
		log.info("====== Method : getCampaignsApiRequet {} ======", endpoint);
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
                errorLogger.error(e.getMessage(), e);
                attempt++;
                if (attempt < MAX_RETRIES) {
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
	public String getContactLtApiReq(String endpoint, String contactListId, String contactId) {
		log.info("====== Method : getContactLtApiRequet {} ======", endpoint);
		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", contactListId, contactId);

		return result;
	}

	@Override 
	public String postContactLtApiReq(String endpoint, String contactListId, List<String> msg) {
		/*
		 * Genesys API 호출 - 컨택리스트 적재 [POST] api/v2/outbound/contactlists/{contactListId}/contacts
		 */
		log.info("====== Method : gostContactLtApiRequet {} ======", endpoint);

		String result = "";
		WebClientApp webClient = new WebClientApp();
		result = webClient.apiReqPushContacts(endpoint, contactListId, msg.toString());

		int cnt = 3;
		int retryCount = 1;
		while (result == null && retryCount < cnt) {
			log.info("Retrying count : {}", retryCount);
			retryCount++;
			result = webClient.apiReqPushContacts(endpoint, contactListId, msg.toString());
		}

		if (result == null && retryCount >= cnt) {
			log.error("Final result : {}", result);
		}

		if (result != null) {
			result = "성공";
		}

		log.info("postContactLtApiRequet 요청 후 결과 값 : {}, 시도횟수 : {} || 컨텍리스트 아이디 : {}", result, retryCount,contactListId);
		return result;
	}

	@Override 
	public String postContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) {
		/*
		 * Genesys API 호출 - 컨택리스트 조회 [POST] api/v2/outbound/contactlists/{contactListId}/contacts/bulk
		 */
		log.info("====== Method : postContactLtApiBulk {} ======", endpoint);
		String result = "";
		log.info("contactListId : {}, cskes : {}", contactListId, cskes.toString());

		WebClientApp webClient = new WebClientApp();
		result = webClient.apiReqGetRtOfContacts(endpoint, contactListId, cskes);

		log.info("postContactLtApiBulk 요청 후 결과 값 result : {}", result);
		return result;
	}
	
	/**
	 * 컨택리스트에 있는 대상자 전부 삭제 API 호출 - 사용 X
	 * 혹시 몰라 남겨둠
	 */
	@Override
	public Void postContactLtClearReq(String endpoint, String contactListId) {
		log.info("====== Method : postContactLtClearReq {} ======", endpoint);
		log.info("contactListId : {}", contactListId);
		
		WebClientApp webClient = new WebClientApp();
		webClient.makeApiRequest(endpoint, "POST", contactListId);

		return null;
	}

	@Override
	public String delContacts(String endpoint, String contactListId, List<String> msg) throws Exception {
		/*
		 * Genesys API 호출 - 컨택리스트 삭제 [DELETE] api/v2/outbound/contactlists/{contactListId}/contacts
		 */
		log.info("====== Method : delContacts {} ======", endpoint);
		log.info("Incoming message (삭제 대상자 리스트-CPSQ) : {}", msg.toString());

		String result = "";
		WebClientApp webClient = new WebClientApp();

		String rst = msg.toString();
		rst = rst.substring(1, rst.length() - 1);
		result = webClient.apionlyfordelContacts(endpoint, "DELETE", contactListId, rst);

		if (result == null)
			log.error("{} 정상적으로 삭제 되었습니다.", rst);
		else
			log.error("에러가 발생했습니다. : {}", result);

		return result;
	}

}
