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
		case "Home":
		case "홈":

			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnma_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "Mobile":
		case "모바일":

			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotHome": //
		case "콜봇홈": //

			business = "Callbot";
			topic_id = "from_clcc_hmaiccmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotMobile"://
		case "콜봇모바일"://

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
			topic_id = "from_clcc_hmucrmcmpnrs_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		} else if( (tkda=='A')&&(divisionName.equals("Home")) ){
			
			business = "CALLBOT";
			topic_id = "from_clcc_hmaiccmpnrs_message"; // 나중에 실제 토픽 명으로 교체해야함.
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);
			
		}else if( (tkda=='A')&&(divisionName.equals("Mobile")) ){
			
			business = "CALLBOT";
			topic_id = "from_clcc_mblaiccmpnrs_message"; // 나중에 실제 토픽 명으로 교체해야함.
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
