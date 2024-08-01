package gc.apiClient.interfaceCollection;

import java.util.List;

public interface InterfaceWebClient {

	String getApiReq(String endpoint, int pagenumber)throws Exception;
	Void postContactLtClearReq(String endpoint,String contactListId)throws Exception;
	String getCampaignsApiReq(String endpoint, String campaignId);
	String postContactLtApiReq(String endpoint, String contactListId, List<String> msg) throws Exception;
	String delContacts(String endpoint, String contactListId, List<String> msg) throws Exception;
	String postContactLtApiBulk(String endpoint, String contactListId, List<String> cskes) throws Exception;

}
