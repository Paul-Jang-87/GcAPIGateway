package gc.apiClient.interfaceCollection;

import gc.apiClient.entity.postgresql.Entity_Ucrm;

public interface InterfaceJson  {//defult로 해놓으면 InterfaceJson을 다른 class에서 implements 했을 때 모두 구현할 필요 없음.
								//원하는 함수가 구현할 수 있음.
	
	  String ExtractVal(String stringMsg) throws Exception;
	  String ExtractValCrm12(String stringMsg, int size) throws Exception;
	  
	  String ExtractValCallBot(String stringMsg,int i)throws Exception;
	  String ExtractValUcrm(String stringMsg)throws Exception;
	  String ExtractRawUcrm(Entity_Ucrm enUcrm)throws Exception;
	  
	  String ExtractVal56(String stringMsg) throws Exception;
	  String ExtractContacts56(String stringMsg ,int i) throws Exception;
	  int ExtractDict(String stringMsg)throws Exception;
	  String ExtractDidtDirt(String stringMsg) throws Exception;
	  String ExtractContactLtId(String stringMsg) throws Exception;
	  String ExtractCampMaUpdateOrDel(String stringMsg) throws Exception;
	  int CampaignListSize(String stringMsg)throws Exception;
	  
	  
}
