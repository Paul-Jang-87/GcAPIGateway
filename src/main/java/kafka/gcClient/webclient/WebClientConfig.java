package kafka.gcClient.webclient;

public class WebClientConfig {//api들의 정보들 수록. 

	private static final String API_BASE_URL = "https://api.apne2.pure.cloud";
	private static final String CLIENT_ID = "8ed02ed8-2e38-41ee-b70d-ab09e43b3ff1";
	private static final String CLIENT_SECRET = "0xgqeo_xNbAUAy1JvXyGCrF5jr8yPOAg_TbDDbOOrB4";

	public static String getBaseUrl() {
		return API_BASE_URL;
	}

	public static String getApiEndpointInfo(String apiName) {

		String API_END_POINT = "";

		switch (apiName) {//api들의 method 방식과 endpoint에 대한 정보들. 사용할 신규 api가 있다면 여기에 등록하면 된다. 
		case "campaigns":
			API_END_POINT = "GET:/api/v2/outbound/campaigns/{campaignId}";
			break;
		case "campaign_stats":
			API_END_POINT = "GET:/api/v2/outbound/campaigns/{campaignId}/stats";
			break;
		case "campaignId":
			API_END_POINT = "GET:/api/v2/outbound/campaigns";
			break;
		case "contactList":
			API_END_POINT = "GET:/api/v2/outbound/contactlists/{contactListId}/contacts/{contactId}";
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