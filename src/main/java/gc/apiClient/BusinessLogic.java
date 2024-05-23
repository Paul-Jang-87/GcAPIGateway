package gc.apiClient;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessLogic {

	private static Map<String, String> businesslogic;

	public static Map<String, String> SelectedBusiness(String division) {

		log.info(" ");
		log.info("====== Class : BusinessLogic - Method : SelectedBusiness ======");
		log.info("division : {}", division);

		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";

		switch (division) {
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

		log.info("business : {}", business);
		log.info("topic_id : {}", topic_id);

		log.info("===== END SelectedBusiness =====");
		return businesslogic;
	}

	public static Map<String, String> SelectedBusiness(Character tkda ,String divisionName) {
		
		log.info(" ");
		log.info("====== Class : BusinessLogic - Method : SelectedBusiness ======");
		log.info("tkda : {}", tkda);
		log.info("divisionName : {}", divisionName);
		
		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";
		
		if ( (tkda=='C')&&(divisionName.equals("Home")) ) { // UCRM
			
			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

		} else if( (tkda=='C')&&(divisionName.equals("Mobile")) ){
			
			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnrs_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		} else if( (tkda=='A')&&(divisionName.equals("CallbotHome")) ){ 
			
			business = "CALLBOT";
			topic_id = "from_clcc_hmaiccmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		}else if( (tkda=='A')&&(divisionName.equals("CallbotMobile")) ){
			
			business = "CALLBOT";
			topic_id = "from_clcc_mblaiccmpnrs_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		}else {
			
			business = "APIM";
			topic_id = ""; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		}
		
		log.info("business : {}", business);
		log.info("topic_id : {}", topic_id);
		
		log.info("===== END SelectedBusiness =====");
		return businesslogic;
	}

}
