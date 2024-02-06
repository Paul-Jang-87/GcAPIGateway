package kafka.gcClient.interfaceCollection;

public interface InterfaceJson {//defult로 해놓으면 InterfaceJson을 다른 class에서 implements 했을 때 모두 구현할 필요 없음.
								//원하는 함수가 구현할 수 있음.
	
	  default String ExtractVal(String stringMsg) {
		  return "";
	  }
	  default String ExtractValCrm12(String stringMsg) {
		  return "";
	  }
	  
	  default String ExtractValCrm34(String stringMsg) {
		  return "";
	  }
	  
	  default String ExtractCrm56(String stringMsg) {
		  return "a";
	  }
	  
	  default int ExtractDict(String stringMsg) {
		  return 1;
	  }
	  
	  default String ExtractDidtDirt(String stringMsg) {
		  return "";
	  }

}
