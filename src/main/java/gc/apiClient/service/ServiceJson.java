package gc.apiClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.interfaceCollection.InterfaceJson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

			result = result + "::" + jsonNode.path("entities").path(0).path("queue").path("name").asText();

			result = result + "::" + jsonNode.path("entities").path(0).path("dialingMode").asText();

		} catch (JsonMappingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 리턴 데이터 형식 예) adhoahfd::oadiifaj::ohdhfa
		return result;
	}

	@Override
	public String ExtractValCrm12(String stringMsg,int i) {// IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.
		
		String jsonResponse = stringMsg;

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractValCrm12 ======");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";
		log.info("메세지 : {}",jsonResponse);

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = result +"::"+jsonNode.path("entities").path(i).path("id").asText();
			String coid = jsonNode.path("entities").path(i).path("contactList").path("name").asText().split("-")[0];
			String cpnm = jsonNode.path("entities").path(i).path("contactList").path("name").asText().split("-")[1];
			result = result + "::" + coid;
			result = result + "::" + cpnm;
			result = result + "::" + jsonNode.path("entities").path(i).path("division").path("name").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("result : {}",result);
		log.info("====== END ExtractValCrm12 ======");
		return result;
	}
	
	
	@Override
	public int CampaignListSize(String stringMsg) {
		String jsonResponse = stringMsg;

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : CampaignListSize ======");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 0;

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = Integer.parseInt(jsonNode.path("total").asText());

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("캠페인 리스트 사이즈 : {}",result);
		log.info("====== END CampaignListSize ======");
		return result;
	}
	
	
	@Override
	public String ExtractCampMaUpdateOrDel(String stringMsg) {// IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.
		
		String jsonResponse = stringMsg;

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractCampMaUpdateOrDel ======");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("detail").path("eventBody").path("id").asText();
			String coid = jsonNode.path("detail").path("eventBody").path("name").asText().split("-")[0];
			String cpnm = jsonNode.path("detail").path("eventBody").path("name").asText().split("-")[1];
			result = result + "::" + coid;
			result = result + "::" + cpnm;
			result = result + "::" + jsonNode.path("detail").path("eventBody").path("division").path("id").asText();
			result = result + "::" + jsonNode.path("detail").path("metadata").path("action").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("result : {}",result);
		log.info("====== END ExtractCampMaUpdateOrDel ======");
		return result;
	}
	

	@Override
	public String ExtractValCrm34(String stringMsg) {// IF-CRM_003,IF-CRM_004에서 사용하기 위한 추출함수.

		log.info("====== ExtractValCrm34 ======");
		
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();
			result = result + "::" + jsonNode.path("cpsq").toString();
			result = result + "::" + jsonNode.path("cske").asText();
			result = result + "::" + jsonNode.path("csna").asText();
			result = result + "::" + jsonNode.path("flag").asText();
			result = result + "::" + jsonNode.path("tkda").asText();
			result = result + "::" + jsonNode.path("tno1").asText();
			result = result + "::" + jsonNode.path("tno2").asText();
			result = result + "::" + jsonNode.path("tno3").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		log.info("result : {}",result);
		return result;
	}

	@Override
	public String ExtractVal56(String stringMsg) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.
		
		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractVal56 ======");
		log.info("Incoming Message : {}",stringMsg);

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("id").asText();
			result += "::" + jsonNode.path("detail").path("eventBody").path("contactList").path("id").asText();
			result += "::" + jsonNode.path("detail").path("eventBody").path("division").path("id").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("result : {}", result); // campaignid, contactlistid, division 추출
		log.info("====== End ExtractVal56 ======");

		return result;
	}
	
	
	@Override
	public String ExtractContacts56(String stringMsg ,int i) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractContacts56 ======");
		log.info("들어온 {}번째 결과 값 result : {}",i,stringMsg);
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);

			result = jsonNode.path(i).path("id").asText();

			result = result + "::" + jsonNode.path(i).path("contactListId").asText();

			result = result + "::" + jsonNode.path(i).path("callRecords").path("전화번호").path("lastAttempt").asText();
			
			result = result + "::" + jsonNode.path(i).path("callRecords").path("전화번호").path("lastResult").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("추출 이후 결과 값 rs: {}",result);
		log.info("====== End ExtractContacts56 ======");

		return result;
	}

	@Override
	public int ExtractDict(String stringMsg) {

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractDict ======");
		log.info("GetStatusApiRequet 요청 후 ExtractDict로 들어온 rs : {}",stringMsg);
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

		log.info("rs에서 추출 한 결과 result(dict) : {}",result);
		log.info("====== End ExtractDict ======");
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
//		String result = "2023-11-29T11:15:31.705Z::ININ-OUTBOUND-PREVIEW-ERROR-PHONE-NUMBER"; // didt,dirt 임시로 넣어둠.
		
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("callRecords").path("전화번호").path("lastAttempt").asText();
			result += "::" + jsonNode.path("callRecords").path("전화번호").path("lastResult").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("resulte : "+result);

		return result;

	}

	
	
}
