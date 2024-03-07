package gc.apiClient;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessLogic {

	private static Map<String, String> businesslogic;

	public static Map<String, String> SelectedBusiness(String division) {

		log.info("====== Class : BusinessLogic - Method : SelectedBusiness ======");
		log.info("division : {}", division);

		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";

		switch (division) {
		case "Home":
		case "홈":

			business = "UCRM";
			topic_id = "firsttopic"; // "from_clcc_cmpnma_h_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "Mobile":
		case "모바일":

			business = "UCRM";
			topic_id = "secondtopic"; // "from_clcc_cmpnma_m_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotHome":
		case "콜봇홈":

			business = "Callbot";
			topic_id = "callbotHtopic"; // "from_clcc_aiccmpnma_h_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotMobile":
		case "콜봇모바일":

			business = "Callbot";
			topic_id = "callbotMtopic"; // "from_clcc_aiccmpnma_m_message"
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

	public static Map<String, String> SelectedBusiness(Character division) {

		log.info("====== Class : BusinessLogic - Method : SelectedBusiness ======");
		log.info("division : {}", division);

		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";

		switch (division) {
		case "Home":
		case "홈":

			business = "UCRM";
			topic_id = "firsttopic"; // "from_clcc_cmpnma_h_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "Mobile":
		case "모바일":

			business = "UCRM";
			topic_id = "secondtopic"; // "from_clcc_cmpnma_m_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotHome":
		case "콜봇홈":

			business = "Callbot";
			topic_id = "callbotHtopic"; // "from_clcc_aiccmpnma_h_message"
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "CallbotMobile":
		case "콜봇모바일":

			business = "Callbot";
			topic_id = "callbotMtopic"; // "from_clcc_aiccmpnma_m_message"
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

}
