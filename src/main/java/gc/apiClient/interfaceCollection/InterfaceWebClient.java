package gc.apiClient.interfaceCollection;

import java.util.List;

public interface InterfaceWebClient {

	String GetApiRequet(String endpoint);

	default String GetStatusApiRequet(String endpoint, String campaignId) {/// api/v2/outbound/campaigns/{campaignId}/stats를
																			/// 위한 함수.
		return "b";
	}

	default String GetCampaignsApiRequet(String endpoint, String campaignId) {// "/api/v2/outbound/campaigns/{campaignId}"
		return "b";
	}

	// "/api/v2/outbound/contactlists/{contactListId}/contacts/{contactId}"
	default String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {/// api/v2/outbound/campaigns/{campaignId}/stats를
																									/// 위한 함수.
		return "b";
	}

	// "api/v2/outbound/contactlists/{contactListId}/contacts"
	default String PostContactLtApiRequet(String endpoint, String contactListId, String msg) {
		return "b";
	}

	// "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk"
	default String PostContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) {
		return "b";
	}

}
