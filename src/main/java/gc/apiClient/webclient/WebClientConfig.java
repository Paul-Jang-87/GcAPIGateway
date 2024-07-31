package gc.apiClient.webclient;

import org.springframework.stereotype.Component;

import gc.apiClient.AppConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 *  현재 앱'GcAPIGateway'에서 사용하는 제네시스 api들과 endpoint들 모아둔 컴포넌트. 
 *  제네시스 api호출을 위해 필요할 설정(제네시스 도메인, 제네시스 OAuth 아이디, 비번)들도 정리되어있다. 
 */

 
@Component
@Slf4j
public class WebClientConfig {		// api들의 정보들 수록.
	private static final String API_BASE_URL = "https://api.apne2.pure.cloud"; //제네시스 기본 api 주소.
	
	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	
	@PostConstruct
    private void init() {
		CLIENT_ID 		= AppConfig.getId();
		CLIENT_SECRET 	= AppConfig.getSecret();
		log.info("CLIENT_ID & CLIENT_SECRET = {} / {}" , CLIENT_ID,CLIENT_SECRET);
		
    }
	
	public static String getBaseUrl() {
		return API_BASE_URL;
	}

	public static String getApiEndpoint(String apiName) {//프로젝트 안에서 사용하는 모든 api들을 여기에 모아두었음. 

		String API_END_POINT = "";

		switch (apiName) {// api들의 method 방식과 endpoint에 대한 정보들. 사용할 신규 api가 있다면 여기에 등록하면 된다.
		case "campaignId":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}";
			break;
		case "campaign_stats":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}/stats";
			break;
		case "campaigns":
			API_END_POINT = "/api/v2/outbound/campaigns";
			break;
		case "contactList":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk";
			break;
		case "contact":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts";
			break;
		case "delcontacts":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts";
			break;
		default:
			API_END_POINT = "Invalid api";
			break;
		}
		return API_END_POINT;
	}

	public static String getClientId() {
		return CLIENT_ID;
	}

	public static String getClientSecret() {
		return CLIENT_SECRET;
	}

}