package kafka.gcClient.interfaceCollection;

public interface InterfaceJson {
	
	  default String ExtractVal(String stringMsg) {
		  return "a";
	  }
	  default String ExtractCpid(String stringMsg) {
		  return "b";
	  }
	  
	  default String ExtractCpidfromThird(String stringMsg) {
		  return "a";
	  }

}
