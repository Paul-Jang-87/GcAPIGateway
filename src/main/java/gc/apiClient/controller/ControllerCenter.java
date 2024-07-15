package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.Entity_ToApim;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.kafMsges.MsgApim;
import gc.apiClient.kafMsges.MsgCallbot;
import gc.apiClient.kafMsges.MsgUcrm;
import gc.apiClient.messages.MessageToApim;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerCenter {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerCenter(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb, CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId) {

		log.info("====== Method : receiveMessage ( TYPE : {} ) ====== ", tranId);

		String result = "";
		String topic_id = tranId;

		switch (topic_id) {

		case "campma":

			try {

				List<JSONObject> camplist = new ArrayList<JSONObject>();
				JSONObject campInfoObj = null;
				result = serviceWeb.getApiReq("campaigns", 1); // 제네시스 api 호출. 'campaignId'는 'WebClientApp'클래스에 미리 정의해둔 endpoint. api :
				int reps = ServiceJson.extractIntVal("CampaignListSize", result);// G.C에서 불러온 캠페인 개수.
				log.info("제네시스에서 조회한 캠페인 수 : {} ", reps);

				// G.C API 캠페인 조회에서 한번에 조회 가능한 캠페인 수는 최대 100개 reps 100개 이상인 경우 page 처리를 통해 캠페인을
				// 조회한다.
				if (reps > 100) {
					int page = 1;
					for (int i = 0; i < 100; i++) {
						campInfoObj = ServiceJson.extractObjVal("ExtractValCrm", result, i);
						camplist.add(campInfoObj);
					}
					reps = reps - 100;
					while ((reps / 100) != 0) {
						++page;
						result = serviceWeb.getApiReq("campaigns", page);
						for (int i = 0; i < 100; i++) {
							campInfoObj = ServiceJson.extractObjVal("ExtractValCrm", result, i);
							camplist.add(campInfoObj);
						}
						reps = reps - 100;
					}
					++page;
					result = serviceWeb.getApiReq("campaigns", page);
					reps = reps % 100;
					for (int i = 0; i < reps; i++) {
						campInfoObj = ServiceJson.extractObjVal("ExtractValCrm", result, i);
						camplist.add(campInfoObj);
					}

					handlingCampMaster(camplist);

				} else {
					for (int i = 0; i < reps; i++) {
						campInfoObj = ServiceJson.extractObjVal("ExtractValCrm", result, i);
						camplist.add(campInfoObj);
					}
				}

			} catch (Exception e) {
				log.error("에러 메시지 : {}", e.getMessage());
				errorLogger.error(e.getMessage(), e);
			}
		}

		return Mono.empty();
	}

	/**
	 * Genesys Cloud 람다 이벤트 호출을 통해 campaign update, delete 이벤트를 처리해 주는 함수
	 * 
	 * @param msg
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/updateOrDelCampma")
	public Mono<ResponseEntity<String>> updateOrDelCampMa(@RequestBody String msg, HttpServletRequest request) throws Exception {

		JSONObject campInfoObj = null;
		Entity_CampMa enCampMa = null;

		try {
			log.info("====== Method : updateOrDelCampMa ======");

			String ipAddress = request.getRemoteAddr();
			int port = request.getRemotePort();
			log.info("IP주소 : {}, 포트 : {}로 부터 api가 호출되었습니다.", ipAddress, port);// 어디서 이 api를 불렀는지 ip와 port 번호를 찍어본다.

			campInfoObj = ServiceJson.extractObjVal("ExtractCampMaUpdateOrDel", msg);
			String division = campInfoObj.getString("divisionnm");
			String action = campInfoObj.getString("action");

			enCampMa = serviceDb.createEnCampMa(campInfoObj);
			String cpid = campInfoObj.getString("cpid");
			String cpna = campInfoObj.getString("cpna");

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");// src/main/resources 경로의
																						// application.properties 참조,
																						// division 아이디를 키로하여 값 조회.

			Map<String, String> businessLogic = BusinessLogic.selectedBusiness(divisionName.trim());

			String endpoint = "";
			String business = businessLogic.get("business");
			String topic_id = businessLogic.get("topic_id");

			log.info(">>> businessLogic :: {}", business);
			switch (business) {
			case "UCRM":

				if (action.equals("update")) {
					// 캠페인 수정은 '캠페인명(cpnm)'만 가능
					// 캠페인 수정 외 캠페인 발신, 발신중지등 캠페인 변화에 대한 이벤트도 'update'로 날아온다.
					MsgUcrm msgucrm = new MsgUcrm();
					msg = msgucrm.makeMaMsg(enCampMa, action);

					log.info("'update'를 위한 변경할 대상 cpid : {}, 새로운 캠페인명 : {} ", cpid, cpna);

					try {

						serviceDb.updateCampMa(cpid, cpna); // 캠페인 아이디를 기준으로 해당 레코드의 캠페인명 업데이트 (DB)

						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg); // update 내용 kafka-producer 메시지 전송

					} catch (EntityNotFoundException ex) { // update 실행 전 cpid로 CAMPMA TABLE 조회, 조회가 되지 않을 경우 Exception
															// 발생
						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
					}

					return Mono.just(ResponseEntity.ok().body(String.format("UCRM, cpid가 %s인 레코드가 성공적으로 업데이트 완료되었습니다.", cpid)));

				} else {// update가 아닌 delete일 때
					log.info("'delete' event - 삭제 대상 cpid : {}", cpid);
					serviceDb.delCampMaById(cpid);
					return Mono.just(ResponseEntity.ok().body(String.format("UCRM, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
				}

			case "Callbot":

				MsgCallbot msgcallbot = new MsgCallbot();
				msg = msgcallbot.makeMaMsg(enCampMa, action);

				// 테이블에 Update, Delete logic 추가.
				if (action.equals("update")) {

					log.info("'update'를 위한 변경할 대상 cpid : {}, 새로운 캠페인명 : {} ", cpid, cpna);

					try {
						serviceDb.updateCampMa(cpid, cpna);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg); // update 내용 kafka-producer 메시지 전송

					} catch (EntityNotFoundException ex) { // update 실행 전 cpid로 CAMPMA TABLE 조회, 조회가 되지 않을 경우 Exception
															// 발생
						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
					}

					return Mono.just(ResponseEntity.ok().body(String.format("Callbot, cpid가 %s인 레코드가 성공적으로 업데이트 완료되었습니다.", cpid)));

				} else {
					log.info("'delete' event - 삭제 대상 cpid : {}", cpid);
					serviceDb.delCampMaById(cpid);
					return Mono.just(ResponseEntity.ok().body(String.format("Callbot, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
				}

			default:
				// APIM application으로 전송
				MsgApim msgapim = new MsgApim();
				msg = msgapim.makeMaMsg(enCampMa, action);

				MessageToApim apim = new MessageToApim();

				if (action.equals("update")) {
					try {
						serviceDb.updateCampMa(cpid, cpna);
						endpoint = "/cmpnMstrRegist";
						apim.sendMsgToApim(endpoint, msg);
						log.info("업데이트를 위한 변경할 대상 cpid : {}, 새로운 캠페인명 : {}, APIM으로 보냄. : {}", cpid, cpna, msg);

					} catch (EntityNotFoundException ex) {
						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
					}

					return Mono.just(ResponseEntity.ok().body(String.format("Apim, cpid가 %s인 레코드가 성공적으로 업데이트가 완료되었습니다.", cpid)));
				} else {
					serviceDb.delCampMaById(cpid);
					endpoint = "/cmpnMstrRegist";
					apim.sendMsgToApim(endpoint, msg);
					log.info("'delete' event - 삭제 대상 cpid : {}, APIM으로 보냄. : {}", cpid, msg);
				}
				return Mono.just(ResponseEntity.ok().body(String.format("\"Apim, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return Mono.just(ResponseEntity.ok().body(String.format("에러가 발생하였습니다. : %s", e.getMessage())));
		}
	}

	@PostMapping("/SaveRtData") // 제네시스의 이벤트를 통해 이 api가 불려짐.
	public Mono<ResponseEntity<String>> saveRtData(@RequestBody String msg) {

		log.info("====== Method : saveRtData ======");

		try {

			String result = ServiceJson.extractStrVal("ExtrSaveRtData", msg);// cpid::cpsq::divisionid 리턴 값으로 이 String
																				// 받음.
			String division = result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");
			log.info("디비전 이름 : {}", divisionName);

			switch (divisionName.trim()) {
			case "Home":
			case "Mobile":

				Entity_UcrmRt enUcrmrt = serviceDb.createUcrmRt(result);
				serviceDb.insertUcrmRt(enUcrmrt);
				return Mono.just(ResponseEntity.ok("Ucrm 데이터가 성공적으로 인서트 되었습니다."));

			case "CallbotHome":
			case "CallbotMobile":

				Entity_CallbotRt enCallBotRt = serviceDb.createCallbotRt(result);
				serviceDb.insertCallbotRt(enCallBotRt);
				return Mono.just(ResponseEntity.ok("Callbot 데이터가 성공적으로 인서트 되었습니다."));
			default:
				Entity_ApimRt enApimRt = serviceDb.createApimRt(result);
				serviceDb.insertApimRt(enApimRt);
				return Mono.just(ResponseEntity.ok("Apim 데이터가 성공적으로 인서트 되었습니다."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Mono.just(ResponseEntity.ok().body(String.format("에러가 발생했습니다. : %s", e.getMessage())));
		}

	}

	@GetMapping("/sendapimrt")
	public Mono<ResponseEntity<String>> sendApimRt() {

		try {
			log.info("====== Method : sendApimRt ======");

			Page<Entity_ApimRt> entitylist = serviceDb.getAllApimRt();// apim 발신 결과와 관련된 테이블의 레코드들을 최대 1000개까지 가지고 온다.

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				int reps = entitylist.getNumberOfElements();// 몇개의 레코드를 가지고 왔는지.
				log.info("'CAMPRT_UCUBE_W' table에서 조회 된 레코드 개수 : {}", reps);

				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				String contactLtId = "";
				String divisionName = "";
				String cpid = "";
				Entity_CampMa enCpma = null;

				for (int i = 0; i < reps; i++) {

					Entity_ApimRt enApimRt = entitylist.getContent().get(i);

					cpid = enApimRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					enCpma = serviceDb.findCampMaByCpid(cpid);
					String cpsq = enApimRt.getId().getCpsq(); // 첫번째 레코드부터 cpsq를 가지고 온다.

					contactLtId = enCpma.getContactltid();
					divisionName = enCpma.getDivisionnm();

					try {
						divisionName = enCpma.getDivisionnm();
					} catch (Exception e) {
						String division = enApimRt.getDivisionid();
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");
					}

					mapdivision.put(contactLtId, divisionName);

					if (!contactlists.containsKey(contactLtId)) {// 맵(contactlists)에 키값(contactLtId)이 없다면.
						contactlists.put(contactLtId, new ArrayList<>());// 'contactLtId'로 된 키 값 추가.
					}
					contactlists.get(contactLtId).add(cpsq);// 'contactLtId'키의 배열에 고객번호(cpsq) 넣음.
					serviceDb.delApimRtById(enApimRt.getId());

					log.info("Arraylist에 추가 - ContactListId : '{}', ", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionName = mapdivision.get(entry.getKey());
						// 캠페인 발신결과 전송 (G.C API add contact bulk 데이터는 한번에 최대 50개까지 add 가능)
						// 배열 크기가 50이상일 때, 배열 안의 인수 개수가 50개 이상일 때는 일단 발신 결과 전송 로직을 탄다. 50개 이상 쌓이고 하게 되면
						// 에러남.
						if (entry.getValue().size() >= 50) {
							sendCampRtToAPIM(entry.getKey(), entry.getValue(), divisionName);// contactLtId 와 contactLtId에 해당하는 배열, divisionName
						}
					}
				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {// 배열 안의 크기가 50개 이상이 아닐 때, 즉 50개 이상 전송하고 남은 나머지들 전송.
					divisionName = mapdivision.get(entry.getKey());
					sendCampRtToAPIM(entry.getKey(), entry.getValue(), divisionName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	public Mono<Void> sendCampRtToAPIM(String contactLtId, List<String> values, String divisionName) throws Exception {

		log.info("====== Method : sendCampRtToAPIM ======");
		ObjectMapper objectMapper = null;
		String result = serviceWeb.postContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.
		String contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, 0);// JsonString 결과값과 조회하고 싶은
																						// 인덱스(첫번째)를 인자로 넣는다.
		String cpid = contactsresult.split("::")[2];

		Entity_CampMa enCampMa = serviceDb.findCampMaByCpid(cpid);
		Entity_CampRt entityCmRt = null;
		int rlsq = serviceDb.findCampRtMaxRlsq().intValue();

		MsgApim msgapim = new MsgApim();
		Entity_ToApim enToApim = new Entity_ToApim();
		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, i);

			entityCmRt = serviceDb.createCampRtMsg(contactsresult, enCampMa, rlsq);
			enToApim = msgapim.rstMassage(entityCmRt);

			apimEntitylt.add(enToApim);
		}

		objectMapper = new ObjectMapper();

		String jsonString = objectMapper.writeValueAsString(apimEntitylt);

		// localhost:8084/dspRslt
		// 192.168.219.134:8084/dspRslt
		MessageToApim apim = new MessageToApim();
		String endpoint = "/dspRslt";
		apim.sendMsgToApim(endpoint, jsonString);
		log.info("CAMPRT 로직, APIM으로 보냄. : {} ", jsonString);
		apimEntitylt.clear();
		values.clear();

		return Mono.empty();
	}

	public void handlingCampMaster(List<JSONObject> camplist) throws Exception {

		JSONObject campInfoObj = null;
		String division = "";
		String business = "";
		String topic_id = "";
		String endpoint = "";
		String cpid = "";
		String msg = "";
		MessageToProducer producer = null;

		// 제네시스로부터 가져온 cpid(캠페인 아이디)들을 담을 list변수들
		List<String> cpFrmGenesys1 = new ArrayList<String>();
		List<String> cpFrmGenesys2 = new ArrayList<String>();

		// DB부터 가져온 cpid(캠페인 아이디)들을 담을 list변수들
		List<String> cpFrmDB1 = new ArrayList<String>();
		List<String> cpFrmDB2 = new ArrayList<String>();
		List<Entity_CampMa> allRecords = serviceDb.getAllRecords();

		Map<String, Integer> cpididx = new HashMap<String, Integer>();

		for (int i = 0; i < camplist.size(); i++) {

			cpid = camplist.get(i).getString("cpid");
			cpFrmGenesys1.add(cpid);
			cpFrmGenesys2.add(cpid);
			cpididx.put(cpid, i);
		}

		for (Entity_CampMa entity : allRecords) {
			cpFrmDB1.add(entity.getCpid());
			cpFrmDB2.add(entity.getCpid());
		}

		cpFrmGenesys1.removeAll(cpFrmDB1);
		cpFrmDB2.removeAll(cpFrmGenesys2);

		if (cpFrmGenesys1.size() > 0) {// 제네시스에는 있고 DB에는 없는 경우

			for (String campid : cpFrmGenesys1) {

				int idx = cpididx.get(campid);
				log.info("제네시스에는 있고 DB에는 없는 경우, 그 캠페인의 숫자는? {} // 캠페인아이디와 해당인덱스 : {} / {}", cpFrmGenesys1.size(), campid, idx);

				campInfoObj = ServiceJson.extractObjVal("ExtrCmpObj", camplist, idx);

				division = campInfoObj.getString("divisionnm");

				Map<String, String> businessLogic = BusinessLogic.selectedBusiness(division);

				business = businessLogic.get("business");
				topic_id = businessLogic.get("topic_id");

				Entity_CampMa enCampMa = serviceDb.createEnCampMa(campInfoObj);

				switch (business.trim()) {// 여기서 비즈니스 로직 구분. default는 'apim'
				case "UCRM":

					MsgUcrm msgucrm = new MsgUcrm();
					msg = msgucrm.makeMaMsg(enCampMa, "insert"); // 카프카 프로듀서로 보내기 위한 메시지 조립작업.

					try {
						serviceDb.insertCampMa(enCampMa);// 'enCampMa' db에 인서트
					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					}

					producer = new MessageToProducer();
					endpoint = "/gcapi/post/" + topic_id;
					producer.sendMsgToProducer(endpoint, msg);

					break;

				case "Callbot":

					MsgCallbot msgcallbot = new MsgCallbot();
					msg = msgcallbot.makeMaMsg(enCampMa, "insert");

					try {
						serviceDb.insertCampMa(enCampMa);
					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					}

					producer = new MessageToProducer();
					endpoint = "/gcapi/post/" + topic_id;
					producer.sendMsgToProducer(endpoint, msg);

					break;

				default:

					try {
						serviceDb.insertCampMa(enCampMa);
					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
						continue;
					}

					MsgApim msgapim = new MsgApim();
					msg = msgapim.makeMaMsg(enCampMa, "insert");

					// localhost:8084/dspRslt
					// 192.168.219.134:8084/dspRslt
					MessageToApim apim = new MessageToApim();
					endpoint = "/cmpnMstrRegist";
					apim.sendMsgToApim(endpoint, msg);
					log.info("CMPA 로직, APIM으로 보냄. : {}", msg);

					break;
				}
			}
		}

		if (cpFrmDB2.size() > 0) {// DB에는 있고 제네시스에는 없는 경우
			for (String campid : cpFrmDB2) {

				log.info("DB에는 있고 제네시스에는 없는 경우, 그 캠페인의 숫자는? {} / DB에서 삭제해야할 cpid(캠페인 아이디는?) : {}", cpFrmDB2.size(), campid);
				serviceDb.delCampMaById(campid);
				cpFrmGenesys1.remove(cpid);
				cpFrmGenesys2.remove(cpid);
			}

		}

	}

	@GetMapping("/gethc")
	public Mono<ResponseEntity<String>> gealthCheck() throws Exception {
		return Mono.just(ResponseEntity.ok("TEST RESPONSE"));
	}

	@GetMapping("/apim-gw")
	public Mono<ResponseEntity<String>> getHealthCheckAPIM() throws Exception {
		return Mono.just(ResponseEntity.ok("TEST RESPONSE"));
	}

	@GetMapping("/kafka-gw")
	public Mono<ResponseEntity<String>> getHealthCheckKafka() throws Exception {
		return Mono.just(ResponseEntity.ok("TEST RESPONSE"));
	}

}
