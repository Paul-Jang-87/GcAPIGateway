package gc.apiClient.datamapping;

import java.util.HashMap;
import java.util.Map;

public class MappingHomeCenter {//홈 매핑 클래스.
	
	 // Define your mapping data
    private Map<String, String> idToCentercodeMapping;

    // Constructor to initialize data
    public MappingHomeCenter() {
    	idToCentercodeMapping = new HashMap<>(); 
    	idToCentercodeMapping.put("21", "H1");
    	idToCentercodeMapping.put("23", "H2");
    	idToCentercodeMapping.put("22", "H3");
    	idToCentercodeMapping.put("26", "H4");
    	idToCentercodeMapping.put("24", "H7");
    	idToCentercodeMapping.put("16", "H11");
    	idToCentercodeMapping.put("13", "H10");
    	idToCentercodeMapping.put("12", "H9");
    	idToCentercodeMapping.put("11", "H8");
    	idToCentercodeMapping.put("25", "H12");
    }

    public String getCentercodeById(String id) {
        return idToCentercodeMapping.get(id);
    }

}
