package gc.apiClient.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component
@PropertySource("classpath:application.properties") // 참조 경로, 참조하는 파일 프로젝트 내 'application.properties'파일
public class WebClientConfig {// api들의 정보들 수록.

	private static final String API_BASE_URL = "https://api.apne2.pure.cloud"; //제네시스 기본 api 주소.
	
	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	
	@Value("${gc.client.id}")//'application.properties' 파일 안의 'gc.client.id'값 가져옴.
	private  String id;//제네시스 api를 호출 하기 위해서 필요한 인증 아이디. 
	@Value("${gc.client.secret}")//'application.properties' 파일 안의 'gc.client.secret'값 가져옴.
	private  String pw;//제네시스 api를 호출 하기 위해서 필요한 인증 패스워드.
	
	@PostConstruct
    private void init() {
		CLIENT_ID = id;
		CLIENT_SECRET = pw;
    }
	
	
	public static String getBaseUrl() {
		return API_BASE_URL;
	}

	public static String getApiEndpoint(String apiName) {//프로젝트 안에서 사용하는 모든 api들을 여기에 모아두었음. 

		String API_END_POINT = "";

		switch (apiName) {// api들의 method 방식과 endpoint에 대한 정보들. 사용할 신규 api가 있다면 여기에 등록하면 된다.
		case "campaigns":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}";
			break;
		case "campaign_stats":
			API_END_POINT = "/api/v2/outbound/campaigns/{campaignId}/stats";
			break;
		case "campaignId":
			API_END_POINT = "/api/v2/outbound/campaigns";
			break;
		case "divisionId":
			API_END_POINT = "/api/v2/authorization/divisions/{divisionId}";
			break;
		case "contactList":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk";
			break;
		case "contact":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts";
			break;
		case "contactltclear":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/clear";
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