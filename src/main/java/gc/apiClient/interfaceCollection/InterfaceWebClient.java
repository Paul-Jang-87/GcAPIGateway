package gc.apiClient.interfaceCollection;

public interface InterfaceWebClient {

	String GetApiRequet(String endpoint);

	default String GetStatusApiRequet(String endpoint, String campaignId) {///api/v2/outbound/campaigns/{campaignId}/stats를 위한 함수. 
		return "b";
	}

	// "/api/v2/outbound/contactlists/{contactListId}/contacts/{contactId}"
	default String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) {///api/v2/outbound/campaigns/{campaignId}/stats를 위한 함수. 
		return "b";
	}
}
