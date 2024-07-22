package gc.apiClient.service;

import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.postgresql.Entity_Ucrm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceJson {

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
		case "ExtractValCallBot":
			return ExtractValCallBot((String) params[0], (int) params[1]);
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

	public static JSONObject extractObjVal(String methodNm, Object... params) throws Exception {
		
		if(methodNm.equals("ExtractCampMaUpdateOrDel")) {
			log.info("====== Method : extractObjVal ( TYPE: {} ) ======", methodNm);
		}

		switch (methodNm) {
		case "ExtractValCrm":
			return ExtractValCrm((String) params[0], (int) params[1]);
		case "ExtrCmpObj":
			return ExtrCmpObj((List<JSONObject>) params[0], (int) params[1]);
		case "ExtractCampMaUpdateOrDel":
			return ExtractCampMaUpdateOrDel((String) params[0]);
		case "ExtrSaveRtData":
			return ExtrSaveRtData((String) params[0]);
		case "ExtractContacts":
			return ExtractContacts((String) params[0], (int) params[1]);
		default:
			throw new IllegalArgumentException("Invalid strategy type");
		}
	}

	
	public static JSONObject ExtractValCrm(String stringMsg, int i) throws Exception {// stringMsg에서 원하는 값만 추출.

		String jsonResponse = stringMsg;

		JSONObject jsonObj = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		jsonNode = objectMapper.readTree(jsonResponse);
		String cpid = jsonNode.path("entities").path(i).path("id").asText();
		String coid = jsonNode.path("entities").path(i).path("callerName").asText();
		String cpnm = jsonNode.path("entities").path(i).path("name").asText();
		String contactListid = jsonNode.path("entities").path(i).path("contactList").path("id").asText();
		String contactListnm = jsonNode.path("entities").path(i).path("contactList").path("name").asText();
		String queueid = jsonNode.path("entities").path(i).path("queue").path("id").asText();
		String divisionid = jsonNode.path("entities").path(i).path("division").path("id").asText();
		String divisionnm = jsonNode.path("entities").path(i).path("division").path("name").asText();
		String insdate = jsonNode.path("entities").path(i).path("dateCreated").asText();
		String moddate = jsonNode.path("entities").path(i).path("dateModified").asText();

		jsonObj.put("cpid", cpid);
		jsonObj.put("coid", coid);
		jsonObj.put("cpnm", cpnm);
		jsonObj.put("contactListid", contactListid);
		jsonObj.put("contactListnm", contactListnm);
		jsonObj.put("queueid", queueid);
		jsonObj.put("divisionid", divisionid);
		jsonObj.put("divisionnm", divisionnm);
		jsonObj.put("insdate", insdate);
		jsonObj.put("moddate", moddate);

		return jsonObj;
	}
	
	
	public static JSONObject ExtrCmpObj(List<JSONObject> camplist, int i) throws Exception {// stringMsg에서 원하는 값만 추출.

		JSONObject jsonObj = camplist.get(i);
		
		String cpid = jsonObj.getString("cpid");
		String coid = jsonObj.getString("coid");
		String cpnm = jsonObj.getString("cpnm");
		String contactListid = jsonObj.getString("contactListid");
		String contactListnm = jsonObj.getString("contactListnm");
		String queueid = jsonObj.getString("queueid");
		String divisionid = jsonObj.getString("divisionid");
		String divisionnm = jsonObj.getString("divisionnm");
		String insdate = jsonObj.getString("insdate");
		String moddate = jsonObj.getString("moddate");

		jsonObj.put("cpid", cpid);
		jsonObj.put("coid", coid);
		jsonObj.put("cpnm", cpnm);
		jsonObj.put("contactListid", contactListid);
		jsonObj.put("contactListnm", contactListnm);
		jsonObj.put("queueid", queueid);
		jsonObj.put("divisionid", divisionid);
		jsonObj.put("divisionnm", divisionnm);
		jsonObj.put("insdate", insdate);
		jsonObj.put("moddate", moddate);

		return jsonObj;
	}
	
	

	public static int CampaignListSize(String stringMsg) throws Exception {
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 0;

		jsonNode = objectMapper.readTree(jsonResponse);
		result = Integer.parseInt(jsonNode.path("total").asText()); // 매개 변수로 받은 'stringMsg'에 "total"이라는 키가 있음. 그 키 값의 의미는 조회된 캠페인의 숫자.

		return result;
	}

	public static JSONObject ExtractCampMaUpdateOrDel(String stringMsg) throws Exception {
		// cpid::coid::cpna::divisionid::action
		
		log.info("Method : ExtractCampMaUpdateOrDel / 인입 메시지 : {} ", stringMsg);
		
		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject jsonObj = new JSONObject();
		JsonNode jsonNode = null;

		jsonNode = objectMapper.readTree(jsonResponse);
		String cpid = jsonNode.path("cpid").asText();
		String coid = jsonNode.path("callerName").asText();
		String cpnm = jsonNode.path("cpnm").asText();
		String divisionid = jsonNode.path("division").asText();
		String divisionnm = jsonNode.path("divisionnm").asText();
		String action = jsonNode.path("action").asText();
		String contactListid = jsonNode.path("contactList_id").asText();
		String contactListnm = jsonNode.path("contactListnm").asText();
		String queueid = jsonNode.path("queueid").asText();
		
		jsonObj.put("cpid", cpid);
		jsonObj.put("coid", coid);
		jsonObj.put("cpnm", cpnm);
		jsonObj.put("divisionid", divisionid);
		jsonObj.put("divisionnm", divisionnm);
		jsonObj.put("action", action);
		jsonObj.put("contactListid", contactListid);
		jsonObj.put("contactListnm", contactListnm);
		jsonObj.put("queueid", queueid);

		return jsonObj;
	}

	public static String ExtractValCallBot(String stringMsg, int i) throws Exception {

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

	public static JSONObject ExtractContacts(String stringMsg, int i) throws Exception {

		String jsonResponse = stringMsg;
		JSONObject jsonObj = new JSONObject();
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
		
		jsonObj.put("id", jsonNode.path(i).path("id").asText());
		jsonObj.put("contactListId", jsonNode.path(i).path("contactListId").asText());
		jsonObj.put("cpid", jsonNode.path(i).path("data").path("CPID").asText());
		jsonObj.put("cpsq", jsonNode.path(i).path("data").path("CPSQ").asText());
		jsonObj.put("lastResult", jsonNode.path(i).path("callRecords").path("TNO1").path("lastResult").asText());
		jsonObj.put("tkda", jsonNode.path(i).path("data").path("TKDA").asText());
		jsonObj.put("lastAttempt", jsonNode.path(i).path("callRecords").path("TNO1").path("lastAttempt").asText());

		if (result.equals("::::::::::::")) {
			result = "";
		}

		log.info("추출 이후 결과 값 rs: {}", result);

		return jsonObj;
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

	public static String ExtractContactLtId(String stringMsg) throws Exception {

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

	public static JSONObject ExtrSaveRtData(String stringMsg) throws Exception {

		String jsonResponse = stringMsg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		JSONObject jsonobj = new JSONObject();
		String result = "";

		jsonNode = objectMapper.readTree(jsonResponse);
		String cpid = jsonNode.path("cpid").asText();
		String cpsq = jsonNode.path("cpsq").asText();
		String divisionid = jsonNode.path("divisionid").asText();
		
		jsonobj.put("cpid", cpid);
		jsonobj.put("cpsq", cpsq);
		jsonobj.put("divisionid", divisionid);

		result = cpid + "::" + cpsq + "::" + divisionid;
		log.info("result : {}", result);
		log.info("cpid(캠페인아이디) : {}", cpid);
		log.info("cpsq(캠페인시퀀스) : {}", cpsq);
		log.info("divisionid(디비전아이디) : {}", divisionid);

		return jsonobj;
	}

}
