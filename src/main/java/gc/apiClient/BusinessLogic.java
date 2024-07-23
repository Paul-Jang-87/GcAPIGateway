package gc.apiClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 현재 프로젝는 여러 비지니스 로직이 있다 
 * 여러 비즈니스 로직 중 UCRM 혹은 Callbot 로직을 division 값에 따라 분류해주는 클래스 이다.
 * 분류가 되면 토픽이 정해진다.
 * 토픽은 우편 주소와 같다 메시지를 어디로 보낼지에 대한 정보이다. 
 *
 */
public class BusinessLogic {
	
	
	
	private static Map<String, String> businesslogic;

	public static Map<String, String> selectedBusiness(String divisionid) {

		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";

		switch (divisionid.trim()) {
		case "2c366c7a-349e-481c-bc61-df5153045fe8": //홈

			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnma_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "232637ae-d261-46e5-92ea-62e8e4696eb5": //모바일

			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "1cd99d76-03bd-4bb6-87f1-1ea5b18cfa24": //콜봇홈

			business = "Callbot";
			topic_id = "from_clcc_hmaiccmpnma_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;

		case "b26cc9f6-0608-46d9-a059-ab3d6b943771": //콜봇모바일

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

	public static Map<String, String> rtSelectedBusiness(String divisionid) {
		
		businesslogic = new HashMap<String, String>();
		String topic_id = "";
		String business = "";
		
		switch (divisionid.trim()) {
		
		case "2c366c7a-349e-481c-bc61-df5153045fe8": //홈

			business = "UCRM";
			topic_id = "from_clcc_hmucrmcmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "232637ae-d261-46e5-92ea-62e8e4696eb5": //모바일

			business = "UCRM";
			topic_id = "from_clcc_mblucrmcmpnrs_message"; 
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "1cd99d76-03bd-4bb6-87f1-1ea5b18cfa24": //콜봇홈

			business = "Callbot";
			topic_id = "from_clcc_hmaiccmpnrs_message";
			businesslogic.put("business", business);
			businesslogic.put("topic_id", topic_id);

			break;
			
		case "b26cc9f6-0608-46d9-a059-ab3d6b943771": //콜봇모바일

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
