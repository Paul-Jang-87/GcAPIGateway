package gc.apiClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.interfaceCollection.InterfaceJson;

public class ServiceJson implements InterfaceJson {

	@Override
	public String ExtractVal(String stringMsg) { // campMa 테이블의 3가지 속성(coid,cpid,cpna)에 넣기 위한 가공 작업
		// (현재는 아무거나 임의로 뽑아봄)-매개변수로 들어온 JsonString data 'stringMsg'에서
		// 'id','name','dialingMode'값 그냥 뽑아봄.

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);

			result = jsonNode.path("entities").path(0).path("id").asText();

			result = result + "|" + jsonNode.path("entities").path(0).path("queue").path("name").asText();

			result = result + "|" + jsonNode.path("entities").path(0).path("dialingMode").asText();

		} catch (JsonMappingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 리턴 데이터 형식 예) adhoahfd|oadiifaj|ohdhfa
		return result;
	}

	@Override
	public String ExtractValCrm12(String stringMsg) {// IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.
		
		String jsonResponse = stringMsg;

		System.out.println("=== ExtractValCrm12 ===");
		System.out.println(jsonResponse);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String ExtractValCrm34(String stringMsg) {// IF-CRM_003,IF-CRM_004에서 사용하기 위한 추출함수.

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();
			result = result + "|" + jsonNode.path("cpsq").toString();
			result = result + "|" + jsonNode.path("cske").asText();
			result = result + "|" + jsonNode.path("csna").asText();
			result = result + "|" + jsonNode.path("flag").asText();
			result = result + "|" + jsonNode.path("tkda").asText();
			result = result + "|" + jsonNode.path("tn01").asText();
			result = result + "|" + jsonNode.path("tn02").asText();
			result = result + "|" + jsonNode.path("tn03").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("=== ExtractValCrm34 ===");
		System.out.println("result : "+result);
		return result;
	}

	@Override
	public String ExtractVal56(String stringMsg) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("id").asText();
			result += "|" + jsonNode.path("detail").path("eventBody").path("contactList").path("id").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	@Override
	public String ExtractContacts56(String stringMsg ,int i) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.

		System.out.println("===== ExtractContacts56 =====");
		
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);

			result = jsonNode.path(i).path("id").asText();

			result = result + "|" + jsonNode.path(i).path("contactListId").asText();

			result = result + "|" + jsonNode.path(i).path("callRecords").path("전화번호").path("lastAttempt").asText();
			
			result = result + "|" + jsonNode.path(i).path("callRecords").path("전화번호").path("lastResult").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("result : "+ result);

		return result;
	}

	@Override
	public int ExtractDict(String stringMsg) {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 100;

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactRate").path("attempts").asInt();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	@Override
	public String ExtractContactLtId(String stringMsg) {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactList").path("id").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String ExtractDidtDirt(String stringMsg) {// stringMsg에서 didt,dirt추출헤서 리턴해주는 함수.
		
		System.out.println("=====ExtractDidtDirt=====");
//		String result = "2023-11-29T11:15:31.705Z|ININ-OUTBOUND-PREVIEW-ERROR-PHONE-NUMBER"; // didt,dirt 임시로 넣어둠.
		
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("callRecords").path("전화번호").path("lastAttempt").asText();
			result += "|" + jsonNode.path("callRecords").path("전화번호").path("lastResult").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("resulte : "+result);

		return result;

	}
	
	
	//Callbot
	@Override
	public String ExtractValCallbot12(String stringMsg) {//콜봇용 IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.
		
		String jsonResponse = stringMsg;

		System.out.println("=== ExtractValCrm12 ===");
		System.out.println(jsonResponse);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}
	

	@Override
	public String ExtractValCallbot34(String stringMsg) {//콜봇용 IF-CRM_003,IF-CRM_004에서 사용하기 위한 추출함수.

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();
			result = result + "|" + jsonNode.path("cpsq").toString();
			result = result + "|" + jsonNode.path("cske").asText();
			result = result + "|" + jsonNode.path("tn01").asText();
			result = result + "|" + jsonNode.path("tkda").asText();
			result = result + "|" + jsonNode.path("flag").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("=== ExtractValCrm34 ===");
		System.out.println("result : "+result);
		return result;
	}
	
	
	
	
}
