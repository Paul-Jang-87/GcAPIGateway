package gc.apiClient.datamapping;

import java.util.HashMap;
import java.util.Map;

public class MappingMobileCenter {//모바일 매핑 클래스
	
	 // Define your mapping data
    private Map<String, String> idToCentercodeMapping;

    // Constructor to initialize data
    public MappingMobileCenter() {
    	idToCentercodeMapping = new HashMap<>();
    	idToCentercodeMapping.put("31", "T1");
    	idToCentercodeMapping.put("33", "T2");
    	idToCentercodeMapping.put("36", "T3");
    	idToCentercodeMapping.put("35", "T4");
    	idToCentercodeMapping.put("34", "T7");
    	idToCentercodeMapping.put("37", "T6");
    	idToCentercodeMapping.put("32", "T8");
    	idToCentercodeMapping.put("59", "T9");
    }

    // Method to get a name based on an ID
    public String getCentercodeById(String id) {
        return idToCentercodeMapping.get(id);
    }

}
