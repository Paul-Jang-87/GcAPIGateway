package kafka.gcClient.interfaceCollection;

public interface InterfaceWebClient {

	String GetApiRequet(String endpoint);

	default String GetStatusApiRequet(String endpoint, String campaignId) {///api/v2/outbound/campaigns/{campaignId}/stats를 위한 함수. 
		return "b";
	}

}
