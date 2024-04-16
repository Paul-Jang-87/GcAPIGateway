package gc.apiClient.interfaceCollection;

import java.util.List;

public interface InterfaceWebClient {

	String GetApiRequet(String endpoint)throws Exception;
	Void PostContactLtClearReq(String endpoint,String contactListId)throws Exception;
	String GetStatusApiRequet(String endpoint, String campaignId) throws Exception;
	String GetDivisionName(String endpoint, String divisionid) throws Exception;
	String GetCampaignsApiRequet(String endpoint, String campaignId) throws Exception;
	String GetContactLtApiRequet(String endpoint, String contactListId, String contactId) throws Exception;
	String PostContactLtApiRequet(String endpoint, String contactListId, List<String> msg) throws Exception;
	String PostContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) throws Exception;

}
