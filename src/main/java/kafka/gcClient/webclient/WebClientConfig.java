package kafka.gcClient.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component
@PropertySource("classpath:application.properties")
public class WebClientConfig {// api들의 정보들 수록.

	private static final String API_BASE_URL = "https://api.apne2.pure.cloud";
//	private static String CLIENT_ID = "8ed02ed8-2e38-41ee-b70d-ab09e43b3ff1";
//	private static String CLIENT_SECRET = "HRwIs7Tn4DHJH_ODIGusDPOEM9Z7cYsFyxG_jb4f5iY";
	
	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	
	@Value("${gc.client.id}")
	private  String id;
	@Value("${gc.client.secret}")
	private  String pw;
	
	@PostConstruct
    private void init() {
		CLIENT_ID = id;
		CLIENT_SECRET = pw;
    }
	
	
	public static String getBaseUrl() {
		return API_BASE_URL;
	}

	public static String getApiEndpoint(String apiName) {

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
		case "contactList":
			API_END_POINT = "/api/v2/outbound/contactlists/{contactListId}/contacts/{contactId}";
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
	
	
    //암호화된 id, 비밀번호 db에서 가져와 복호화 하는 함수. 
//	public void getClientIdPwd() {
//
//		Entity_AppConfig enttAppconfig = new Entity_AppConfig();
//
//		enttAppconfig = servicedb.getEntityById((long) 1);
//
//		String id = enttAppconfig.getGcClientId();
//		String pwd = enttAppconfig.getGcClientSecret();
//
//		String decryptedId;
//		String decryptedPassword;
//		try {
//			decryptedId = AESDecryption.decrypt(id);
//			decryptedPassword = AESDecryption.decrypt(pwd);
//
//			System.out.println("Decrypted ID: " + decryptedId);
//			System.out.println("Decrypted Password: " + decryptedPassword);
//
//			CLIENT_ID = decryptedId;
//			CLIENT_SECRET = decryptedPassword;
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}