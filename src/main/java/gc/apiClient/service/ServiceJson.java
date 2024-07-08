package gc.apiClient.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.postgresql.Entity_Ucrm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceJson  {
	
	/**
	 * 
	 * @param methodNm
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	public static String extractStrVal(String methodNm, Object... params) throws Exception {
		
		log.info("====== Method : extractStrVal ( TYPE: {} ) ======", methodNm);
		
        switch (methodNm) {
            case "ExtractVal":
                return ExtractVal((String) params[0]);
            case "ExtractValCrm":
                return ExtractValCrm((String) params[0], (int) params[1]);
            case "ExtractContacts":
            	return ExtractContacts((String) params[0], (int) params[1]);
            case "ExtractValCallBot":
            	return ExtractValCallBot((String) params[0], (int) params[1]);
            case "ExtractCampMaUpdateOrDel":
                return ExtractCampMaUpdateOrDel((String) params[0]);
            case "ExtrSaveRtData":
                return ExtrSaveRtData((String) params[0]);
            case "ExtractContactLtId":
            	return ExtractContactLtId((String) params[0]);
            case "ExtractRawUcrm":
            	  if (params[0] instanceof Entity_Ucrm) {
                      return ExtractRawUcrm((Entity_Ucrm) params[0]);
                  } else {
                      throw new IllegalArgumentException("Expected Entity_Ucrm as parameter for ExtractRawUcrm");
                  }
            default:
                throw new IllegalArgumentException("Invalid strategy type");
        }
    }
	
	/**
	 * 
	 * 
	 * @param methodNm
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static int extractIntVal(String methodNm, Object... params) throws Exception {
		
		log.info("====== Method : extractIntVal ( TYPE: {} ) ======", methodNm);
		
        switch (methodNm) {
            case "CampaignListSize":
                return CampaignListSize((String) params[0]);
            case "ExtractDict":
            	return ExtractDict((String) params[0]);
            default:
                throw new IllegalArgumentException("Invalid strategy type");
        }
    }

	
	public static String ExtractVal(String stringMsg) throws Exception { 
		// campMa 테이블의 3가지 속성(coid,cpid,cpna)에 넣기 위한 가공 작업
		// (현재는 아무거나 임의로 뽑아봄)-매개변수로 들어온 JsonString data 'stringMsg'에서
		// 'id','name','dialingMode'값 그냥 뽑아봄.

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

			jsonNode = objectMapper.readTree(jsonResponse);

			result = jsonNode.path("entities").path(0).path("id").asText();

			result = result + "::" + jsonNode.path("entities").path(0).path("queue").path("name").asText();

			result = result + "::" + jsonNode.path("entities").path(0).path("dialingMode").asText();

		// 리턴 데이터 형식 예) adhoahfd::oadiifaj::ohdhfa
		return result;
	}
	

	public static String ExtractValCrm(String stringMsg, int i) throws Exception {//stringMsg에서 원하는 값만 추출. 

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

			jsonNode = objectMapper.readTree(jsonResponse);
			String cpid = jsonNode.path("entities").path(i).path("id").asText();
			String coid = jsonNode.path("entities").path(i).path("callerName").asText();
			String cpnm = jsonNode.path("entities").path(i).path("name").asText();
			String divisionName = jsonNode.path("entities").path(i).path("division").path("name").asText();

		result = cpid+"::"+coid+"::"+cpnm+"::"+divisionName;
		log.info("result : {}", result);
		log.info("cpid(캠페인아이디) : {}", cpid);
		log.info("coid(센터구분코드) : {}", coid);
		log.info("cpnm(캠페인명) : {}", cpnm);
		log.info("divisionName(디비전명) : {}", divisionName);
		return result;
	}

	public static int CampaignListSize(String stringMsg) throws Exception{
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 0;

			jsonNode = objectMapper.readTree(jsonResponse);
			result = Integer.parseInt(jsonNode.path("total").asText()); //매개 변수로 받은 'stringMsg'에 "total"이라는 키가 있음. 그 키 값의 의미는 조회된 캠페인의 숫자.

		log.info("총 캠페인 개수 : {}", result);
		return result;
	}
	
	
	public static String ExtractCampMaUpdateOrDel(String stringMsg) throws Exception{
		// cpid::coid::cpna::divisionid::action
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

			jsonNode = objectMapper.readTree(jsonResponse);
			String cpid = jsonNode.path("cpid").asText();
			String coid = jsonNode.path("callerName").asText();
			String cpnm = jsonNode.path("cpnm").asText();
			String division = jsonNode.path("division").asText();
			String action = jsonNode.path("action").asText();

		result = cpid+"::"+coid+"::"+cpnm+"::"+division+"::"+action;
		
		log.info("result : {}", result);
		log.info("cpid(캠페인아이디) : {}", cpid);
		log.info("coid(센터구분코드) : {}", coid);
		log.info("cpnm(캠페인명) : {}", cpnm);
		log.info("division(디비전아이디) : {}", division);
		log.info("action(crud타입) : {}", action);
		return result;
	}

	
	public static String ExtractValCallBot(String stringMsg, int i) throws Exception{

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cmpnItemDto").path(i).path("cmpnId").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("cmpnSeq").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("custNo").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("custNum").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("token").asText();
			result = result + "::" + jsonNode.path("cmpnItemDto").path(i).path("flag").asText();


		return result;
	}


	
	public static String ExtractRawUcrm(Entity_Ucrm enUcrm) throws Exception {// cpid::cpsq::cske::csno::tkda::flag


		String cpid = enUcrm.getId().getCpid();
		String cpsq = enUcrm.getId().getCpsq();

		String result = "";
		result = cpid;
		result = result + "::" + cpsq;
		result = result + "::" + enUcrm.getHldrCustId();
		result = result + "::" + enUcrm.getTlno();
		result = result + "::" + enUcrm.getTrdtCntn();
		result = result + "::" + enUcrm.getWorkDivsCd();
		return result;
	}

	
	public static String ExtractContacts(String stringMsg, int i) throws Exception {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path(i).path("id").asText();
			result = result + "::" + jsonNode.path(i).path("contactListId").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("CPID").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("CPSQ").asText();
			result = result + "::" + jsonNode.path(i).path("callRecords").path("TNO1").path("lastResult").asText();
			result = result + "::" + jsonNode.path(i).path("data").path("TKDA").asText();
			result = result + "::" + jsonNode.path(i).path("callRecords").path("TNO1").path("lastAttempt").asText();

		if (result.equals("::::::::::::")) {
			result = "";
		}

		log.info("추출 이후 결과 값 rs: {}", result);

		return result;
	}

	
	public static int ExtractDict(String stringMsg) throws Exception {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 0;

			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactRate").path("attempts").asInt();

		return result;
	}

	public static String ExtractContactLtId(String stringMsg) throws Exception{

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";
		String que = " ";

			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactList").path("id").asText();
			if (jsonNode.path("queue").path("id").asText().equals("")) {
			} else {
				que = jsonNode.path("queue").path("id").asText();
			}
			result = result + "::" + que;

		return result;
	}

	
	public static String ExtrSaveRtData(String stringMsg) throws Exception {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		jsonNode = objectMapper.readTree(jsonResponse);
		String cpid = jsonNode.path("cpid").asText();
		String cpsq = jsonNode.path("cpsq").asText();
		String divisionid = jsonNode.path("divisionid").asText();

		result = cpid+"::"+cpsq+"::"+divisionid;
		log.info("result : {}", result);
		log.info("cpid(캠페인아이디) : {}", cpid);
		log.info("cpsq(캠페인시퀀스) : {}", cpsq);
		log.info("divisionid(디비전아이디) : {}", divisionid);

		return result;
	}
	
}
