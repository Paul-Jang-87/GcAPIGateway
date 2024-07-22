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

	@Override
	public String getApiReq(String endpoint,int pagenumber) {

		log.info("====== Method : getApiRequet {} ======", endpoint);

		String result = "";

		WebClientApp webClient = new WebClientApp();
		/*
		 * "sortBy", "dateCreated", "sortOrder", "descending", "pageSize",100, "pageNumber", pagenumber => 쿼리파라미터 내용 두개씩 짝. '키:값' 역할
		 *  내용은 생성된 시간으로 / 내림차순 정렬 / 한페이지에 100개씩 / 페이지넘버.
		 *  제네시스에서 캠페인 목록을 불러올 때 한꺼번에 다 불러오지 못한다. 
		 *  예를 들어 제네시스에 등록된 캠페인 개수가 178개라고 가정할 때 최대 100개 1번 나머지 78개 한번. 이렇게 불러올 수 있다. 한번에 178개를 다 불러올 수 없다. 
		 *  이 예시의 경우 api를 2번 호출 해야한다. 
		 *  
		 */
		result = webClient.makeApiRequest(endpoint, "GET", "sortBy", "dateCreated", "sortOrder", "descending", "pageSize",100, "pageNumber", pagenumber);

		return result;
	}
	

	/*
	 *   캠페인 아이디를 가지고 하나의 캠페인에 대한 통계 데이터를 가지온다. 
	 *   주로 발신후 그 결과, 응답값에 대한 처리를 해줄때 사용하는 함수이다. 
	 *   통화시도 횟수, 통화 연결 횟수, 커넷션 비율 등등... 
	 *   근데 주로 통화 시도 횟수, attempts = > dict 를 알기 위해서 사용한다.
	 */
	@Override
	public String getStatusApiReq(String endpoint, String campaignId) {

		log.info("====== Method : getStatusApiRequet {} ======", endpoint);

		String result = "";

		WebClientApp webClient = new WebClientApp();
		result = webClient.makeApiRequest(endpoint, "GET", campaignId);

		return result;
	}
	

	/**
	 * 함수 'getApiReq'가 제네시스의 모든 캠페인 정보들을 조회하는 api함수였다면,
	 * 이 함수는 캠페인 아이디를 가지고 하나의 캠페인에 대한 정보를 조회하는 함수이다. 
	 */
	@Override 
	public String getCampaignsApiReq(String endpoint, String campaignId) {
		/*
		 * <Genesys API 호출> - 캠페인 조회 [GET]/api/v2/outbound/campaigns/{campaignId}
		 */
		log.info("====== Method : getCampaignsApiRequet {} ======", endpoint);
		String result = "";
        
        try {
            WebClientApp webClient = new WebClientApp();
            result = webClient.makeApiRequest(endpoint, "GET", campaignId);
        } catch (Exception e) {
            log.info("getCampaignsApiRequet 에러 : {}",  e.getMessage());
            errorLogger.error(e.getMessage(), e);
        }
        

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


//		log.info("postContactLtApiRequet 요청 후 결과 값 : {} || 컨텍리스트 아이디 : {}", rs,contactListId);
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

		
//		log.info("postContactLtApiBulk 요청 후 결과 값 result : {}", rs);
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
			log.info("{} 정상적으로 삭제 되었습니다.", rst);
		else
			log.error("에러가 발생했습니다. : {}", result);

		return result;
	}

}
