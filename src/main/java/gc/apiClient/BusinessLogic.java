package gc.apiClient;

import java.util.HashMap;
import java.util.Map;


public class BusinessLogic {

	private static Map<String, String> businesslogic;

	public static Map<String, String> selectedBusiness(String division) {

		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";

		switch (division.trim()) {
		case "홈":
		case "Home":

			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnma_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "모바일":
		case "Mobile":

			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "콜봇홈": 
		case "CallbotHome": 

			business = "Callbot";
			topic_id = "from_clcc_hmaiccmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "콜봇모바일":
		case "CallbotMobile":

			business = "Callbot";
			topic_id = "from_clcc_mblaiccmpnma_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		default:

			business = "APIM";
			topic_id = "APIMcamptopic";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
		}


		return businesslogic;
	}

	public static Map<String, String> rtSelectedBusiness(String divisionName) {
		
		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";
		
		switch (divisionName.trim()) {
		
		case "홈":
		case "Home":

			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "모바일":
		case "Mobile":

			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnrs_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "콜봇홈": 
		case "CallbotHome": 

			business = "Callbot";
			topic_id = "from_clcc_hmaiccmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "콜봇모바일":
		case "CallbotMobile":

			business = "Callbot";
			topic_id = "from_clcc_mblaiccmpnrs_message";  
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		default:

			business = "APIM";
			topic_id = ""; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
		}
		
		return businesslogic;
	}

}
