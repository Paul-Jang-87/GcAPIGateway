package gc.apiClient.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.CampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.Ucrm;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateEntity {
	
	private final CustomProperties customProperties;
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private final InterfaceDBPostgreSQL serviceDb;
	
	
	public CreateEntity(CustomProperties customProperties,InterfaceDBPostgreSQL serviceDb) {

		this.customProperties = customProperties;
		this.serviceDb = serviceDb;
	}
	
	public Entity_CampRt createCampRtMsg(JSONObject jsonobj, Entity_CampMa enCampMa) {
		// contactid::contactListId::cpid::CPSQ::dirt::tkda::dateCreated

		log.info("====== Method : createCampRtMsg ======");

		log.info("들어온 rs : {}", jsonobj.toString());
		Entity_CampRt enCampRt = new Entity_CampRt();
		CampRt id = new CampRt();

		int coid = 0;
		int cpsq = Integer.parseInt(jsonobj.getString("cpsq"));
		long hubId = 0;
		int dirt = 0;
		int dict = 0;
		String campid = jsonobj.optString("cpid", ""); 
		String contactLtId = jsonobj.optString("contactListId", ""); 
		String contactId = jsonobj.optString("id", "");
		String tkda = jsonobj.optString("tkda", "");
		Date didt = null;

		try {

			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 ------");
			log.info("coid: {}", coid);
			log.info("cpsq: {}", cpsq);
			log.info("hubid: {}", hubId);
			log.info("dirt: {}", dirt);
			log.info("dict: {}", dict);
			log.info("campid: {}", campid);
			log.info("contactLtId: {}", contactLtId);
			log.info("contactId: {}", contactId);
			log.info("tkda: {}", tkda);
			log.info("didt: {}", jsonobj.optString("lastAttempt", ""));
			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 끝 ------");

			if (tkda.charAt(0) == 'C') {
				hubId = Long.parseLong(tkda.split(",")[1]);
			} else if (tkda.charAt(0) == 'A') {
				cpsq = Integer.parseInt(tkda.split("\\|\\|")[5]);
			} else {
			}

			String didval = jsonobj.optString("lastAttempt", "");
			if(!didval.equals("")) {
				
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
				log.info("didt(포맷 변경 전) : {}", didval);
				Date parsedDate = inputFormat.parse(didval);
				// Formatting the parsed date to the desired format
				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				String formattedDateString = outputFormat.format(parsedDate);
				Date formattedDate = outputFormat.parse(formattedDateString);
				didt = formattedDate;
				log.info("didt(포맷 변경 후) : {}", didt);
				
			}
			

			log.info("dirt(맵핑 전) : {}", jsonobj.optString("lastResult", ""));
			Map<String, String> properties = customProperties.getProperties();
			dirt = Integer.parseInt(properties.getOrDefault(jsonobj.optString("lastResult", ""), "1").trim());
			log.info("dirt(맵핑 후) : {}", dirt);

			ServiceWebClient crmapi = new ServiceWebClient();
			String result = crmapi.getStatusApiReq("campaign_stats", campid);
			dict = ServiceJson.extractIntVal("ExtractDict", result);

			coid = enCampMa.getCoid();
			log.info("campid({})로 조회한 레코드의 coid : {}", campid, coid);
			int rlsq = serviceDb.findCampRtMaxRlsq().intValue();
			log.info("camprt테이블에서 현재 가장 큰 rlsq 값 : {}", rlsq);
			rlsq++;
			log.info("가져온 rlsq의 값에 +1 : {}", rlsq);

			id.setRlsq(rlsq);
			id.setCoid(coid);
			enCampRt.setId(id);
			enCampRt.setContactLtId(contactLtId);
			enCampRt.setContactid(contactId);
			enCampRt.setCpid(campid);
			enCampRt.setTkda(tkda);
			enCampRt.setCamp_seq(cpsq);
			enCampRt.setHubid(hubId);
			enCampRt.setDidt(didt);
			enCampRt.setDirt(dirt);
			enCampRt.setDict(dict);

			log.info("------ return 하기 전 변수들의 최종 값 확인 ------");
			log.info("rlsq: {}", rlsq);
			log.info("coid: {}", coid);
			log.info("campid: {}", campid);
			log.info("cpsq: {}", cpsq);
			log.info("contactLtId: {}", contactLtId);
			log.info("contactId: {}", contactId);
			log.info("tkda: {}", tkda);
			log.info("hubid: {}", hubId);
			log.info("didt: {}", didt);
			log.info("dirt: {}", dirt);
			log.info("dict: {}", dict);
			log.info("------ return 하기 전 변수들의 최종 값 확인 ------");

		} catch (Exception e) {
			log.error("Error Message : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enCampRt;
	}
	
	
	public Entity_CampMa createEnCampMa(JSONObject jsonObject) { // 매개변수로 받는 String msg = > cpid::coid::cpna::division

		log.info("====== Method : createEnCampMa ======");

		Entity_CampMa enCampMa = new Entity_CampMa();
		String cpid = "";
		int coid = 0;
		String cpnm = "";
		String contactListid = "";
		String contactListnm = "";
		String queueid = "";
		String divisionid = "";
		String divisionnm = "";
		String insdate = "";
		String moddate = "";

		try {
			
			coid = Integer.parseInt(jsonObject.optString("coid", "")); // 센터구분 코드
			
		} catch (Exception e) {
			log.info("잘못된 coid(센터구분 코드)입니다 coid(센터구분 코드)는 두 자리 숫자여야 합니다 : {}", jsonObject.getString("coid"));
			coid = 99;
			log.info("coid(센터구분 코드)임의로 숫자 '99'로 변경 : {}", coid);
		}
		
		cpid = jsonObject.optString("cpid", ""); //캠페인아이디
		cpnm = jsonObject.optString("cpnm", ""); //캠페인명
		contactListid = jsonObject.optString("contactListid", ""); //컨텍리스트아이디
		contactListnm = jsonObject.optString("contactListnm", ""); //컨텍리스트명
		queueid = jsonObject.optString("queueid", ""); //큐아이디
		divisionid = jsonObject.optString("divisionid", ""); //디비전아이디
		divisionnm = jsonObject.optString("divisionnm", ""); //디비전명
		insdate = jsonObject.optString("insdate", "");//최초생성일
		moddate = jsonObject.optString("moddate", ""); //마지막수정일

		enCampMa.setCpid(cpid);
		enCampMa.setCoid(coid);
		enCampMa.setCpna(cpnm);
		enCampMa.setContactltid(contactListid);
		enCampMa.setContactltnm(contactListnm);
		enCampMa.setQueueid(queueid);
		enCampMa.setDivisionid(divisionid);
		enCampMa.setDivisionnm(divisionnm);
		enCampMa.setInsdate(insdate);
		enCampMa.setModdate(moddate);

		return enCampMa;
	}
	
	
	public Entity_CampMa_D createEnCampMa_D(Entity_CampMa enCampma) { // 매개변수로 받는 String msg = > cpid::coid::cpna::division

		log.info("====== Method : Entity_CampMa_D ======");
		
		// 로컬 시간 가져오기
        LocalDateTime localDateTime = LocalDateTime.now();
        // UTC 시간대로 변환
        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // 문자열로 변환
        String formattedDateTime = utcDateTime.format(formatter);

		Entity_CampMa_D enCampMa_D = new Entity_CampMa_D();
		
		String cpid = enCampma.getCpid();
		int coid = enCampma.getCoid();
		String cpnm = enCampma.getCpna();
		String contactListid = enCampma.getContactltid();
		String contactListnm = enCampma.getContactltnm();
		String queueid = enCampma.getQueueid();
		String divisionid = enCampma.getDivisionid();
		String divisionnm = enCampma.getDivisionnm();
		String insdate = formattedDateTime;
		String moddate = enCampma.getModdate();
			

		enCampMa_D.setCpid(cpid);
		enCampMa_D.setCoid(coid);
		enCampMa_D.setCpna(cpnm);
		enCampMa_D.setContactltid(contactListid);
		enCampMa_D.setContactltnm(contactListnm);
		enCampMa_D.setQueueid(queueid);
		enCampMa_D.setDivisionid(divisionid);
		enCampMa_D.setDivisionnm(divisionnm);
		enCampMa_D.setInsdate(insdate);
		enCampMa_D.setModdate(moddate);

		return enCampMa_D;
	}
	
	
	public Entity_ContactLt createContactLtMsg(String msg) {// (콜봇에서 뽑아온거)cpid::cpsq::cske::csno::tkda::flag

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		String ContactLvalues[] = msg.split("::");

		try {
			id.setCpid(ContactLvalues[0]);
			id.setCpsq(Integer.parseInt(ContactLvalues[1]));
			enContactLt.setId(id);
			enContactLt.setCske(ContactLvalues[2]);// "customerkey"
			enContactLt.setFlag(ContactLvalues[5]);// "HO2"
			enContactLt.setTkda(ContactLvalues[4]);// "custid,111"

		} catch (Exception e) {
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}
	
	
	public Entity_Ucrm createUcrm(String msg) throws Exception {

		Entity_Ucrm enUcrm = new Entity_Ucrm();
		Ucrm id = new Ucrm();
		JSONObject jsonObj = new JSONObject(msg);
		String payload = jsonObj.getString("payload");
		JSONObject payloadObject = new JSONObject(payload);

		String ctiCmpnId = payloadObject.optString("ctiCmpnId", "");
		String ctiCmpnSno = payloadObject.optString("ctiCmpnSno", "");
		String cablTlno = payloadObject.optString("cablTlno", "");
		String custNm = payloadObject.optString("custNm", "");
		String custTlno = payloadObject.optString("custTlno", "");

		id.setCpid(ctiCmpnId);
		id.setCpsq(ctiCmpnSno);
		enUcrm.setId(id);
		enUcrm.setCablTlno(cablTlno);
		enUcrm.setCustNm(custNm);
		enUcrm.setCustTlno(custTlno);
		enUcrm.setHldrCustId(payloadObject.optString("hldrCustId", ""));
		enUcrm.setSubssDataChgCd(payloadObject.optString("subssDataChgCd", ""));
		enUcrm.setSubssDataDelYn(payloadObject.optString("subssDataDelYn", ""));
		enUcrm.setTlno(payloadObject.optString("tlno", ""));
		enUcrm.setTopcDataIsueDtm(payloadObject.optString("topcDataIsueDtm", ""));
		enUcrm.setTopcDataIsueSno(payloadObject.optString("topcDataIsueSno", ""));
		enUcrm.setTrdtCntn(payloadObject.optString("trdtCntn", ""));
		enUcrm.setWorkDivsCd(payloadObject.optString("workDivsCd", ""));

		return enUcrm;
	}
	
	
	public String createContactLtGC(String msg) {
		// 뽑아온다(콜봇).cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

		String values[] = msg.split("::");

		JSONObject data = new JSONObject();
		JSONObject mainObj = new JSONObject();
		try {
			data.put("CPID", values[0]);
			data.put("CPSQ", values[1]);
			data.put("CSKE", values[2]);
			data.put("CSNA", "");
			data.put("CBDN", "");
			data.put("TKDA", values[4]);
			data.put("TNO1", values[3]);
			data.put("TNO2", "");
			data.put("TNO3", "");
			data.put("TNO4", "");
			data.put("TNO5", "");
			data.put("TLNO", "");
			data.put("TMZO", "Asia/Seoul"); // <-- (+09:00) 삭제
			data.put("QUEUEID", values[7]);
			data.put("TRYCNT", "0");

			mainObj.put("data", data);
			mainObj.put("id", values[1]);
			mainObj.put("contactListId", values[6]);

		} catch (Exception e) {
			log.error("Error Message :{}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return mainObj.toString();
	}
	
	
	public Entity_UcrmRt createUcrmRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_UcrmRt enUcrmRt = new Entity_UcrmRt();
		UcrmCampRt ucrmCampRt = new UcrmCampRt();
		ucrmCampRt.setCpid(cpid);
		ucrmCampRt.setCpsq(cpsq);
		enUcrmRt.setId(ucrmCampRt);
		enUcrmRt.setDivisionid(divisionid);

		return enUcrmRt;
	}
	
	
	public Entity_CallbotRt createCallbotRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_CallbotRt enCallbotRt = new Entity_CallbotRt();
		CallBotCampRt callbotCampRt = new CallBotCampRt();
		callbotCampRt.setCpid(cpid);
		callbotCampRt.setCpsq(cpsq);
		enCallbotRt.setId(callbotCampRt);
		enCallbotRt.setDivisionid(divisionid);

		return enCallbotRt;
	}
	
	
	public Entity_ApimRt createApimRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_ApimRt apimRt = new Entity_ApimRt();
		ApimCampRt apimCampRt = new ApimCampRt();
		apimCampRt.setCpid(cpid);
		apimCampRt.setCpsq(cpsq);
		apimRt.setId(apimCampRt);
		apimRt.setDivisionid(divisionid);

		return apimRt;
	}
	
	
	public Entity_ContactLt createContactUcrm(JSONObject jsonObject) throws Exception {
		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		try {
			JSONObject dataObject = jsonObject.getJSONObject("data");
			String cpid = dataObject.getString("CPID");
			String cpsq = dataObject.getString("CPSQ");
			String cske = dataObject.getString("CSKE");
			String tkda = dataObject.getString("TKDA");
			String tno1 = dataObject.getString("TNO1");

			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setFlag("A");
			enContactLt.setTkda(tkda);
			enContactLt.setTno1(tno1);

		} catch (Exception e) {
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}
	
	
	
	

}
