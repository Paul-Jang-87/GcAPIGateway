package gc.apiClient.service;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.postgresql.Entity_Ucrm;
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
	public String ExtractValCrm12(String stringMsg, int i) {// IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.

		String jsonResponse = stringMsg;

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractValCrm12 ======");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("entities").path(i).path("id").asText();
			String coid = jsonNode.path("entities").path(i).path("callerName").asText();
			String cpnm = jsonNode.path("entities").path(i).path("name").asText();
			result = result + "::" + coid;
			result = result + "::" + cpnm;
			result = result + "::" + jsonNode.path("entities").path(i).path("division").path("name").asText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		log.info("result : {}", result);
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

		log.info("캠페인 리스트 사이즈 : {}", result);
		log.info("====== END CampaignListSize ======");
		return result;
	}

	@Override
	public String ExtractCampMaUpdateOrDel(String stringMsg) {// IF-CRM_001,IF-CRM_002에서 사용하기 위한 추출함수.
		// cpid::coid::cpna::divisionid::action
		String jsonResponse = stringMsg;

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractCampMaUpdateOrDel ======");

		log.info("Incoming Msg : {}", stringMsg);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("campaign_id").asText();
			String coid = jsonNode.path("callerName").asText();
			String cpnm = jsonNode.path("campaign_name").asText();
			result = result + "::" + coid;
			result = result + "::" + cpnm;
			result = result + "::" + jsonNode.path("division").asText();
			result = result + "::" + jsonNode.path("action").asText();

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("result : {}", result);
		log.info("====== END ExtractCampMaUpdateOrDel ======");
		return result;
	}

	@Override
	public String ExtractValCallBot(String stringMsg, int i) {// IF-CRM_003,IF-CRM_004에서 사용하기 위한 추출함수.

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractValCallBot ======");
		// (콜봇에서 뽑아온거)cpid::cpsq::cske::csno::tkda::flag

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cmpnItemDto").path(i).path("cmpnId").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("cmpnSeq").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("custNo").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("custNum").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("token").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("flag").asText();

		} catch (Exception e) {
			log.info("Error Message : {}", e.getMessage());
		}

		log.info("result : {}", result);
		log.info("====== End ExtractValCallBot ======");
		return result;
	}

	@Override
	public String ExtractValUcrm(String stringMsg) {

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractValUcrm ======");

		log.info("Incoming msg : {}", stringMsg);

		JSONObject jsonObject = new JSONObject(stringMsg);
		String payload = jsonObject.getString("payload");
		JSONObject payloadObject = new JSONObject(payload);

		String result = "";
		// (콜봇에서 뽑아온거)cpid::cpsq::cske::csno::tkda::flag

		try {
			result = payloadObject.getString("ctiCmpnId");
			result = result + "::" + payloadObject.getString("ctiCmpnSno");
			result = result + "::" + payloadObject.getString("hldrCustId");
			result = result + "::" + payloadObject.getString("tlno");
			result = result + "::" + payloadObject.getString("trdtCntn");
			result = result + "::" + payloadObject.getString("workDivsCd");
			result = result + "::" + payloadObject.getString("topcDataIsueSno");
			result = result + "::" + payloadObject.getString("topcDataIsueDtm");
			result = result + "::" + payloadObject.getString("subssDataChgCd");
			result = result + "::" + payloadObject.getString("subssDataDelYn");
			result = result + "::" + payloadObject.getString("custNm");
			result = result + "::" + payloadObject.getString("cablTlno");
			result = result + "::" + payloadObject.getString("custTlno");

		} catch (Exception e) {

			log.info("Error Message : {}", e.getMessage());
			e.printStackTrace();
		}

		log.info("result : {}", result);
		return result;
	}

	@Override
	public String ExtractRawUcrm(Entity_Ucrm enUcrm) {// cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

//		log.info(" ");
//		log.info("====== ClassName : ServiceJson & Method : ExtractRawUcrm ======");

//		log.info("record info : {}",enUcrm.toString() );

		String cpid = enUcrm.getId().getCpid();
		String cpsq = enUcrm.getId().getCpsq();

		String result = "";
		result = cpid;
//		log.info("cpid : {}",cpid );
		result = result + "::" + cpsq;
//		log.info("cpid : {}",cpsq );
		result = result + "::" + enUcrm.getHldrCustId();
//		log.info("cske : {}",enUcrm.getHldrCustId() );
		result = result + "::" + enUcrm.getTlno();
//		log.info("csno : {}",enUcrm.getTlno() );
		result = result + "::" + enUcrm.getTrdtCntn();
//		log.info("tkda : {}",enUcrm.getTrdtCntn() );
		result = result + "::" + enUcrm.getWorkDivsCd();
//		log.info("flag : {}",enUcrm.getWorkDivsCd() );

//		log.info("result : {}",result );
//		log.info("====== End ExtractRawUcrm ======");
		return result;
	}

	@Override
	public String ExtractVal56(String stringMsg) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractVal56 ======");
		log.info("Incoming Message : {}", stringMsg);

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {

			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("campaign_id").asText();
			result += "::" + jsonNode.path("contactList_id").asText();
			result += "::" + jsonNode.path("division").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("result : {}", result);
		log.info("====== End ExtractVal56 ======");

		return result;
	}

	@Override
	public String ExtractContacts56(String stringMsg, int i) {// IF-CRM_005,IF-CRM_006에서 사용하기 위한 추출함수.

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractContacts56 ======");
		log.info("들어온 {}번째 결과 값 result : {}", i, stringMsg);
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path(i).path("id").asText();
			result = result + "::" + jsonNode.path(i).path("contactListId").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("CPID").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("CPSQ").asText();
			result = result + "::" + jsonNode.path(i).path("callRecords").path("TNO1").path("lastResult").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("TKDA").asText();
			result = result + "::" + jsonNode.path(i).path("callRecords").path("TNO1").path("lastAttempt").asText();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (result.equals("::::::::::::")) {
			result = "";
		}

		log.info("추출 이후 결과 값 rs: {}", result);
		log.info("====== End ExtractContacts56 ======");

		return result;
	}

	@Override
	public int ExtractDict(String stringMsg) {

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtractDict ======");
		log.info("GetStatusApiRequet 요청 후 ExtractDict로 들어온 rs : {}", stringMsg);
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

		log.info("rs에서 추출 한 결과 result(dict) : {}", result);
		log.info("====== End ExtractDict ======");
		return result;
	}

	@Override
	public String ExtractContactLtId(String stringMsg) {

//		log.info(" ");
//		log.info("====== ClassName : ServiceJson & Method : ExtractContactLtId ======");
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";
		String que = " ";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactList").path("id").asText();
			if (jsonNode.path("queue").path("id").asText().equals("")) {
			} else {
				que = jsonNode.path("queue").path("id").asText();
			}
			result = result + "::" + que;

		} catch (Exception e) {
			log.info("Error Message : {}", e.getMessage());
		}

//		log.info("result of ExtractContactLtId : {}",result);
//		log.info("====== End ExtractContactLtId ======");
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
		System.out.println("resulte : " + result);

		return result;

	}

	@Override
	public String ExtrSaveRtData(String stringMsg) throws Exception {

		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtrSaveRtData ======");
		log.info("incoming msg : {} : ",stringMsg);
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		jsonNode = objectMapper.readTree(jsonResponse);
		result = jsonNode.path("cpid").asText();
		result += "::" + jsonNode.path("cpsq").asText();
		result += "::" + jsonNode.path("divisionid").asText();

		log.info("result : {}", result);

		log.info("====== End ExtrSaveRtData ======");
		return result;
	}

	@Override
	public String ExtrDivisionNm(String stringMsg) throws Exception {
		
		log.info(" ");
		log.info("====== ClassName : ServiceJson & Method : ExtrDivisionNm ======");

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		jsonNode = objectMapper.readTree(jsonResponse);
		result = jsonNode.path("division").path("name").asText();
		
		log.info("result : {}", result);

		log.info("====== End ExtrDivisionNm ======");
		return result;
		
	}

}
