package gc.apiClient.interfaceCollection;

public interface InterfaceJson  {//defult로 해놓으면 InterfaceJson을 다른 class에서 implements 했을 때 모두 구현할 필요 없음.
								//원하는 함수가 구현할 수 있음.
	
	  default String ExtractVal(String stringMsg) {
		  return "";
	  }
	  default String ExtractValCrm12(String stringMsg, int size) {
		  return "";
	  }
	  
	  default String ExtractValCrm34(String stringMsg) {
		  return "";
	  }
	  
	  default String ExtractVal56(String stringMsg) {
		  return "a";
	  }
	  
	  default String ExtractContacts56(String stringMsg ,int i) {
		  return "a";
	  }
	  
	  default int ExtractDict(String stringMsg) {
		  return 1;
	  }
	  
	  default String ExtractDidtDirt(String stringMsg) {
		  return "";
	  }
	  
	  default String ExtractContactLtId(String stringMsg) {
		  return "";
	  }
	  
	  
	  default String ExtractCampMaUpdateOrDel(String stringMsg) {
		  return "";
	  }
	  
	  int CampaignListSize(String stringMsg);
	  
	  
}
