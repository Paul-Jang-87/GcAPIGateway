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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/**
 * 데이터베이스에서 데이터를 조작하기 위해서는 조작하려는 해당 테이블과 대응하는 엔티티 객체를 만들어 줘야한다. 이 서비스는 엔티티 객체를
 * 만드는 것을 담당하는 서비스이다. 이 서비스에서는 포스트그레DB만 다루고 있다. 포스트그레DB에는 총 8개의 테이블이 있기때문에(9개이지만
 * 한 개는 안 쓰기에 - 'map_coid'테이블) 8개 종류의 엔티티 객체만 취급한다. 패키지
 * 'gc.apiClient.entity.postgresql'참조
 */

public class CreateEntity {

	private final CustomProperties customProperties;
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private final InterfaceDBPostgreSQL serviceDb;

	public CreateEntity(CustomProperties customProperties, InterfaceDBPostgreSQL serviceDb) {

		this.customProperties = customProperties;
		this.serviceDb = serviceDb;
	}

	@Transactional
	public Entity_CampRt createCampRtMsg(JSONObject jsonobj, Entity_CampMa enCampMa) {
		// contactid::contactListId::cpid::CPSQ::dirt::tkda::dateCreated

		log.info("====== Method : createCampRtMsg ======");

		log.info("들어온 rs : {}", jsonobj.toString());
		Entity_CampRt enCampRt = new Entity_CampRt();
		CampRt id = new CampRt();

		int coid = 0;
		int cpsq = 0;
		long hubId = 0;
		int dirt = 0;
		int dict = 0;
		int rlsq = 0;
		String campid = "";
		String contactLtId = "";
		String contactId = "";
		String tkda = "";
		Date didt = null;

		try {

			cpsq = Integer.parseInt(jsonobj.getString("cpsq"));
			campid = jsonobj.getString("cpid");// 2024-07-30 optString에서 getString으로 변경
			contactLtId = jsonobj.getString("contactListId");
			contactId = jsonobj.optString("id", "");
			tkda = jsonobj.optString("tkda", "");

			if (tkda.charAt(0) == 'C') {
				hubId = Long.parseLong(tkda.split(",")[1]);
			} else if (tkda.charAt(0) == 'A') {
				cpsq = Integer.parseInt(tkda.split("\\|\\|")[5]);
			} else {
			}

			String didval = jsonobj.optString("lastAttempt", "");
			if (!didval.equals("")) {

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

			// dict값(발신시도 횟수)를 제네시스로 부터 가져오기 위해 cpid로 제네시스 api를 호출한다. api =>
			// "/api/v2/outbound/campaigns/{campaignId}/stats"
			ServiceWebClient crmapi = new ServiceWebClient();
			String result = crmapi.getStatusApiReq("campaign_stats", campid);
			dict = ServiceJson.extractIntVal("ExtractDict", result);

			coid = enCampMa.getCoid();
			log.info("campid({})로 조회한 레코드의 coid : {}", campid, coid);
			rlsq = serviceDb.findCampRtMaxRlsq().intValue();
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

		} catch (Exception e) {// 2024-07-30 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("createCampRtMsg 에러 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());

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

			return enCampRt;
		}

		return enCampRt;
	}

	public Entity_CampMa createEnCampMa(JSONObject jsonobj) {

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

			cpid = jsonobj.getString("cpid"); // 캠페인아이디
			cpnm = jsonobj.optString("cpnm", ""); // 캠페인명
			contactListid = jsonobj.optString("contactListid", ""); // 컨텍리스트아이디
			contactListnm = ""; // 사용 안 하는 컬럼.
			queueid = jsonobj.optString("queueid", ""); // 큐아이디
			divisionid = jsonobj.optString("divisionid", ""); // 디비전아이디
			divisionnm = jsonobj.optString("divisionnm", ""); // 디비전아이디
			insdate = jsonobj.optString("insdate", "");// 최초생성일
			moddate = jsonobj.optString("moddate", ""); // 마지막수정일

			try {
				coid = Integer.parseInt(jsonobj.optString("coid", "")); // 센터구분 코드

			} catch (Exception e) {
				log.info("잘못된 coid(센터구분 코드)입니다 coid(센터구분 코드)는 두 자리 숫자여야 합니다 : {}", jsonobj.getString("coid"));
				coid = 99;
				log.info("coid(센터구분 코드)임의로 숫자 '99'로 변경 : {}", coid);
			}

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

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());

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
		}

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

			cpid = enCampma.getCpid();
			coid = enCampma.getCoid();
			cpnm = enCampma.getCpna();
			contactListid = enCampma.getContactltid();
			contactListnm = enCampma.getContactltnm();
			queueid = enCampma.getQueueid();
			divisionid = enCampma.getDivisionid();
			divisionnm = enCampma.getDivisionnm();
			insdate = formattedDateTime;
			moddate = enCampma.getModdate();

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

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", enCampma.toString());
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

		}

		return enCampMa_D;
	}

	public Entity_ContactLt createContactLtMsg(JSONObject jsonobj) {// (콜봇에서 뽑아온거)cpid::cpsq::cske::csno::tkda::flag

		ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime seoulTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();

		String cpid = "";
		String cpsq = "";
		String cske = "";
		String csno = "";
		String tkda = "";
		String flag = "";

		try {

			cpid = jsonobj.getString("cpid");
			cpsq = jsonobj.getString("cpsq");
			cske = jsonobj.optString("cske", "");
			csno = jsonobj.optString("csno", "");
			tkda = jsonobj.optString("tkda", "");
			flag = jsonobj.optString("flag", "");

			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setTno1(csno);
			enContactLt.setFlag(flag);
			enContactLt.setTkda(tkda);
			enContactLt.setDate(seoulTime.toLocalDateTime());

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setTno1(csno);
			enContactLt.setFlag(flag);
			enContactLt.setTkda(tkda);
			enContactLt.setDate(seoulTime.toLocalDateTime());

			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}

	public Entity_Ucrm createUcrm(String msg) throws Exception {

		Entity_Ucrm enUcrm = new Entity_Ucrm();
		Ucrm id = new Ucrm();

		JSONObject jsonObj = null;
		String payload = "";
		JSONObject payloadObject = null;
		String ctiCmpnId = "";
		String ctiCmpnSno = "";
		String cablTlno = "";
		String custNm = "";
		String custTlno = "";

		try {

			jsonObj = new JSONObject(msg);
			payload = jsonObj.getString("payload");
			payloadObject = new JSONObject(payload);
			ctiCmpnId = payloadObject.getString("ctiCmpnId");
			ctiCmpnSno = payloadObject.optString("ctiCmpnSno", "");
			cablTlno = payloadObject.optString("cablTlno", "");
			custNm = payloadObject.optString("custNm", "");
			custTlno = payloadObject.optString("custTlno", "");

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

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", msg);
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

		}

		return enUcrm;
	}

	public String createContactLtGC(JSONObject jsonobj) {
		// 뽑아온다(콜봇).cpid::cpsq::cske::csno::tkda::flag::contactltid::queid

		JSONObject data = new JSONObject();
		JSONObject mainObj = new JSONObject();
		String cpid = "";
		String cpsq = "";
		String cske = "";
		String tkda = "";
		String csno = "";
		String queueid = "";
		String contactltid = "";

		try {

			cpid = jsonobj.getString("cpid");
			cpsq = jsonobj.getString("cpsq");
			cske = jsonobj.getString("cske");
			tkda = jsonobj.getString("tkda");
			csno = jsonobj.getString("csno");
			queueid = jsonobj.getString("queueid");
			contactltid = jsonobj.getString("contactltid");

			data.put("CPID", cpid);
			data.put("CPSQ", cpsq);
			data.put("CSKE", cske);
			data.put("CSNA", "");
			data.put("CBDN", "");
			data.put("TKDA", tkda);
			data.put("TNO1", csno);
			data.put("TNO2", "");
			data.put("TNO3", "");
			data.put("TNO4", "");
			data.put("TNO5", "");
			data.put("TLNO", "");
			data.put("TMZO", "Asia/Seoul"); // <-- (+09:00) 삭제
			data.put("QUEUEID", queueid);
			data.put("TRYCNT", "0");

			mainObj.put("data", data);
			mainObj.put("id", cpsq);
			mainObj.put("contactListId", contactltid);

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			data.put("CPID", cpid);
			data.put("CPSQ", cpsq);
			data.put("CSKE", cske);
			data.put("CSNA", "");
			data.put("CBDN", "");
			data.put("TKDA", tkda);
			data.put("TNO1", csno);
			data.put("TNO2", "");
			data.put("TNO3", "");
			data.put("TNO4", "");
			data.put("TNO5", "");
			data.put("TLNO", "");
			data.put("TMZO", "Asia/Seoul"); // <-- (+09:00) 삭제
			data.put("QUEUEID", queueid);
			data.put("TRYCNT", "0");

			mainObj.put("data", data);
			mainObj.put("id", cpsq);
			mainObj.put("contactListId", contactltid);

			log.error("Error Message :{}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return mainObj.toString();
	}

	public Entity_UcrmRt createUcrmRt(JSONObject jsonobj) throws Exception {

		String cpid = "";
		String cpsq = "";
		String divisionid = "";
		Entity_UcrmRt enUcrmRt = new Entity_UcrmRt();
		UcrmCampRt ucrmCampRt = new UcrmCampRt();

		try {

			cpid = jsonobj.getString("cpid");
			cpsq = jsonobj.optString("cpsq", "");
			divisionid = jsonobj.optString("divisionid", "");

			ucrmCampRt.setCpid(cpid);
			ucrmCampRt.setCpsq(cpsq);
			enUcrmRt.setId(ucrmCampRt);
			enUcrmRt.setDivisionid(divisionid);

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			ucrmCampRt.setCpid(cpid);
			ucrmCampRt.setCpsq(cpsq);
			enUcrmRt.setId(ucrmCampRt);
			enUcrmRt.setDivisionid(divisionid);
		}

		return enUcrmRt;
	}

	public Entity_CallbotRt createCallbotRt(JSONObject jsonobj) throws Exception {

		String cpid = "";
		String cpsq = "";
		String divisionid = "";
		Entity_CallbotRt enCallbotRt = new Entity_CallbotRt();
		CallBotCampRt callbotCampRt = new CallBotCampRt();

		try {
			cpid = jsonobj.getString("cpid");// 2024-07-30 키 값으로 쓰일 수 있는 변수들은 'getString'로 변경
			cpsq = jsonobj.optString("cpsq", "");
			divisionid = jsonobj.optString("divisionid", "");

			callbotCampRt.setCpid(cpid);
			callbotCampRt.setCpsq(cpsq);
			enCallbotRt.setId(callbotCampRt);
			enCallbotRt.setDivisionid(divisionid);

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			callbotCampRt.setCpid(cpid);
			callbotCampRt.setCpsq(cpsq);
			enCallbotRt.setId(callbotCampRt);
			enCallbotRt.setDivisionid(divisionid);

		}

		return enCallbotRt;
	}

	public Entity_ApimRt createApimRt(JSONObject jsonobj) throws Exception {

		String cpid = "";
		String cpsq = "";
		String divisionid = "";
		Entity_ApimRt apimRt = new Entity_ApimRt();
		ApimCampRt apimCampRt = new ApimCampRt();

		try {

			cpid = jsonobj.getString("cpid");// 2024-07-30 키 값으로 쓰일 수 있는 변수들은 'getString'로 변경
			cpsq = jsonobj.optString("cpsq", "");
			divisionid = jsonobj.optString("divisionid", "");

			apimCampRt.setCpid(cpid);
			apimCampRt.setCpsq(cpsq);
			apimRt.setId(apimCampRt);
			apimRt.setDivisionid(divisionid);

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			apimCampRt.setCpid(cpid);
			apimCampRt.setCpsq(cpsq);
			apimRt.setId(apimCampRt);
			apimRt.setDivisionid(divisionid);
		}

		return apimRt;
	}

	public Entity_ContactLt createContactUcrm(JSONObject jsonobj) throws Exception {

		ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime seoulTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		JSONObject dataObject = null;

		String cpid = "";
		String cpsq = "";
		String cske = "";
		String tkda = "";
		String tno1 = "";

		try {
			dataObject = jsonobj.getJSONObject("data");
			cpid = dataObject.getString("CPID");
			cpsq = dataObject.getString("CPSQ");
			cske = dataObject.getString("CSKE");
			tkda = dataObject.getString("TKDA");
			tno1 = dataObject.getString("TNO1");

			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setFlag("A");
			enContactLt.setTkda(tkda);
			enContactLt.setTno1(tno1);
			enContactLt.setDate(seoulTime.toLocalDateTime());

		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).

			log.error("파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", jsonobj.toString());
			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setFlag("A");
			enContactLt.setTkda(tkda);
			enContactLt.setTno1(tno1);
			enContactLt.setDate(seoulTime.toLocalDateTime());
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}

}
