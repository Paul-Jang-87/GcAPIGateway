package gc.apiClient.interfaceCollection;

public interface InterfaceJsonOracle {
	
	  default String ExtractDataCall(String stringMsg) {
		  return "";
	  }
	
	  default String ExtractDataCallCustomer(String stringMsg) {
		  return "";
	  }
	  default String ExtractDataCallService(String stringMsg) {
		  return "";
	  }
	  default String ExtractDataCallCode(String stringMsg) {
		  return "";
	  }
	  default String ExtractMDataCall(String stringMsg) {
		  return "";
	  }
	  default String ExtractMDataCallCustomer(String stringMsg) {
		  return "";
	  }
	  default String ExtractMDataCallService(String stringMsg) {
		  return "";
	  }
	  default String ExtractMMasterServiceCode(String stringMsg) {
		  return "";
	  }
	  default String ExtractMWaDataCall(String stringMsg) {
		  return "";
	  }
	  default String ExtractMWaDataCallOptioncal(String stringMsg) {
		  return "";
	  }
	  default String ExtractMWaDataCallTrace(String stringMsg) {
		  return "";
	  }
	  default String ExtractMWaMTraceCode(String stringMsg) {
		  return "";
	  }
	  default String ExtractWaDataCall(String stringMsg) {
		  return "";
	  }
	  default String ExtractWaDataCallOptioncal(String stringMsg) {
		  return "";
	  }
	  default String ExtractWaDataCallTrace(String stringMsg) {
		  return "";
	  }
	  default String ExtractWaMTraceCode(String stringMsg) {
		  return "";
	  }
	  
	  public <T> T returnKey(String topic, String msg);
}
