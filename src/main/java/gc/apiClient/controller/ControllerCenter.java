package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
import gc.apiClient.messages.MessageToApim;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import gc.apiClient.webclient.WebClientApp;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kafMsges.MsgApim;
import kafMsges.MsgCallbot;
import kafMsges.MsgUcrm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

	@Scheduled(fixedRate = 86400 * 1000) // 24시간 마다 토큰 초기화.
	public void RefreshToken() {

		WebClientApp.EmptyTockenlt();

	}
	

	@Scheduled(fixedRate = 60000)
	public void scheduledMethod() {// 1분 간격으로 안의 함수들 비동기적으로 실행

		Mono.fromCallable(() -> receiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();
		Mono.fromCallable(() -> sendApimRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}


	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId) {

		log.info("====== Method : receiveMessage ( TYPE : {} ) ====== ", tranId);

		String result = "";
		String topic_id = tranId;
		int size = 0;
		int numberOfRecords = 0;

		switch (topic_id) {

		case "campma":

			try {
				
				// 제네시스 api 호출. 'campaignId'는 'WebClientApp'클래스에 미리 정의 해둔 endpoint.
				// G.C API 캠페인 전체 조회 (/api/v2/outbound/campaigns/{campaignId})
				result = serviceWeb.getApiReq("campaignId", 1);	
				size = ServiceJson.extractIntVal("CampaignListSize", result);	// G.C에서 불러온 캠페인 개수.

				numberOfRecords = serviceDb.getRecordCount(); 	// DB CAMPMA TABLE 총 레코드 갯수.

				if (size == numberOfRecords) {// 조회된 캠페인의 개수와 현재 db에 저장된 캠페인 정보의 숫자가 동일하다. 즉, 새로 생성된 캠페인이 없다.

				} else {
					// 조회된 캠페인의 개수와 현재 db에 저장된 캠페인 정보의 숫자가 다르다. 즉, api 호출로 캠페인들을 조회 해본 결과 새로 생성된 캠페인이 있다.
					int reps = size - numberOfRecords;  // 몇 개의 캠페인이 새로 생성됐는지.
					log.info("신규 생성된 캠페인 수 : {} ", reps);

					// G.C API 캠페인 조회에서 한번에 조회 가능한 캠페인 수는 최대 100개.
					// reps 100개 이상인 경우 page 처리를 통해 캠페인을 조회한다. 
					if (reps > 100) {
						int page = 1;
						handlingCampMaster(100, result);
						reps = reps - 100;
						while ((reps / 100) != 0) {
							++page;
							result = serviceWeb.getApiReq("campaignId", page);
							handlingCampMaster(100, result);
							reps = reps - 100;
						}
						++page;
						result = serviceWeb.getApiReq("campaignId", page);
						reps = reps % 100;
						handlingCampMaster(reps, result);

					} else {
						handlingCampMaster(reps, result);
					}

				}
			} catch (Exception e) {
				log.error("에러 메시지 : {}", e.getMessage());
				errorLogger.error(e.getMessage(),e);
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
	public Mono<ResponseEntity<String>> updateOrDelCampMa(@RequestBody String msg, HttpServletRequest request)
			throws Exception {

		String row_result = "";
		Entity_CampMa enCampMa = null;

		try {
			log.info("====== Method : updateOrDelCampMa ======");

			String ipAddress = request.getRemoteAddr();
			int port = request.getRemotePort();
			log.info("IP주소 : {}, 포트 : {}로 부터 api가 호출되었습니다.", ipAddress, port);// 어디서 이 api를 불렀는지 ip와 port 번호를 찍어본다.

			row_result = ServiceJson.extractStrVal("ExtractCampMaUpdateOrDel", msg); // row_result >> cpid::coid::cpna::divisionid::action
			String division = row_result.split("::")[3];
			String action = row_result.split("::")[4];

			enCampMa = serviceDb.createEnCampMa(row_result);
			String cpid = row_result.split("::")[0];
			String cpna = row_result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");// src/main/resources 경로의 application.properties 참조, division 아이디를 키로하여 값 조회.

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
					msg = msgucrm.maMessage(enCampMa, action);

					log.info("'update'를 위한 변경할 대상 cpid : {}, 새로운 캠페인명 : {} ", cpid, cpna);

					try {

						serviceDb.updateCampMa(cpid, cpna); // 캠페인 아이디를 기준으로 해당 레코드의 캠페인명 업데이트 (DB)

						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg); // update 내용 kafka-producer 메시지 전송 

					} catch (EntityNotFoundException ex) { // update 실행 전 cpid로 CAMPMA TABLE 조회, 조회가 되지 않을 경우 Exception 발생
						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
					}

					return Mono.just(
							ResponseEntity.ok().body(String.format("UCRM, cpid가 %s인 레코드가 성공적으로 업데이트 완료되었습니다.", cpid)));

				} else {// update가 아닌 delete일 때
					log.info("'delete' event - 삭제 대상 cpid : {}", cpid);
					serviceDb.delCampMaById(cpid);
					return Mono
							.just(ResponseEntity.ok().body(String.format("UCRM, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
				}

			case "Callbot":

				MsgCallbot msgcallbot = new MsgCallbot();
				msg = msgcallbot.maMessage(enCampMa, action);

				// 테이블에 Update, Delete logic 추가.
				if (action.equals("update")) {

					log.info("'update'를 위한 변경할 대상 cpid : {}, 새로운 캠페인명 : {} ", cpid, cpna);

					try {
						serviceDb.updateCampMa(cpid, cpna);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg);	// update 내용 kafka-producer 메시지 전송 

					} catch (EntityNotFoundException ex) {	// update 실행 전 cpid로 CAMPMA TABLE 조회, 조회가 되지 않을 경우 Exception 발생
						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
						errorLogger.error(ex.getMessage(), ex);
					}

					return Mono.just(ResponseEntity.ok()
							.body(String.format("Callbot, cpid가 %s인 레코드가 성공적으로 업데이트 완료되었습니다.", cpid)));

				} else {
					log.info("'delete' event - 삭제 대상 cpid : {}", cpid);
					serviceDb.delCampMaById(cpid);
					return Mono.just(
							ResponseEntity.ok().body(String.format("Callbot, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
				}

			default:
				// APIM application으로 전송 
				MsgApim msgapim = new MsgApim();
				msg = msgapim.maMessage(enCampMa, action);

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

					return Mono.just(
							ResponseEntity.ok().body(String.format("Apim, cpid가 %s인 레코드가 성공적으로 업데이트가 완료되었습니다.", cpid)));
				} else {
					serviceDb.delCampMaById(cpid);
					endpoint = "/cmpnMstrRegist";
					apim.sendMsgToApim(endpoint, msg);
					log.info("'delete' event - 삭제 대상 cpid : {}, APIM으로 보냄. : {}", cpid, msg);
				}
				return Mono
						.just(ResponseEntity.ok().body(String.format("\"Apim, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
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

				Map<String, String> mapcontactltId = new HashMap<String, String>();
				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				String contactLtId = "";
				String divisionName = "";
				String cpid = "";

				for (int i = 0; i < reps; i++) {

					Entity_ApimRt enApimRt = entitylist.getContent().get(i);

					cpid = enApimRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					String cpsq = enApimRt.getId().getCpsq(); // 첫번째 레코드부터 cpsq를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid) != null ? mapcontactltId.get(cpid) : "";
					divisionName = mapdivision.get(contactLtId) != null ? mapdivision.get(contactLtId) : "";

					if (contactLtId == null || contactLtId.equals("")) {// cpid로 매치가 되는 contactltId가 있는지 조회 했는데 그것에 대응하는
																		// contactltId가 없다면,
						log.info("일치하는 contactLtId 없음");
						String result = serviceWeb.getCampaignsApiReq("campaigns", cpid);
						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];

						String division = enApimRt.getDivisionid(); // 첫번째 레코드부터 cpid를 가지고 온다.
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");

						mapcontactltId.put(cpid, contactLtId);
						mapdivision.put(contactLtId, divisionName);
					} else {
						log.info("일치하는 contactId 있음");
					}

					if (!contactlists.containsKey(contactLtId)) {// 맵(contactlists)에 키값(contactLtId)이 없다면.
						contactlists.put(contactLtId, new ArrayList<>());// 'contactLtId'로 된 키 값 추가.
					}
					contactlists.get(contactLtId).add(cpsq);// 'contactLtId'키의 배열에 고객번호(cpsq) 넣음.
					serviceDb.delApimRtById(enApimRt.getId());

					log.info("Arraylist에 추가 - ContactListId : '{}', ", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionName = mapdivision.get(entry.getKey());
						// 캠페인 발신결과 전송 (G.C API add contact bulk 데이터는 한번에 최대 50개까지 add 가능)
						// 배열 크기가 50이상일 때, 배열 안의 인수 개수가 50개 이상일 때는 일단 발신 결과 전송 로직을 탄다. 50개 이상 쌓이고 하게 되면 에러남.
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
		String contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
		String cpid = contactsresult.split("::")[2];
		
		Entity_CampMa enCampMa = serviceDb.findCampMaByCpid(cpid);
		Entity_CampRt entityCmRt = null;
		int rlsq = serviceDb.findCampRtMaxRlsq().intValue();

		MsgApim msgapim = new MsgApim();
		Entity_ToApim enToApim = new Entity_ToApim();
		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, i);

			entityCmRt = serviceDb.createCampRtMsg(contactsresult, enCampMa,rlsq);
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


	public void handlingCampMaster(int reps, String result) throws Exception {

		String row_result = "";
		String division = "";
		String business = "";
		String topic_id = "";
		String endpoint = "";
		String msg = "";
		MessageToProducer producer = null;

		while (reps-- > 0) {// reps가 0이 될 때까지 reps를 줄여나가면서 반복.

			log.info("{}번째 인덱스 ", reps);

			row_result = ServiceJson.extractStrVal("ExtractValCrm", result, reps);// 결과 값 : cpid::coid::cpna::division
																					// ->
																					// 캠페인아이디::테넌트아이디::캠페인명::디비전

			division = row_result.split("::")[3];

			Map<String, String> businessLogic = BusinessLogic.selectedBusiness(division);

			business = businessLogic.get("business");
			topic_id = businessLogic.get("topic_id");

			Entity_CampMa enCampMa = serviceDb.createEnCampMa(row_result);

			switch (business.trim()) {// 여기서 비즈니스 로직 구분. default는 'apim'
			case "UCRM":

				MsgUcrm msgucrm = new MsgUcrm();
				msg = msgucrm.maMessage(enCampMa, "insert"); // 카프카 프로듀서로 보내기 위한 메시지 조립작업.

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
				msg = msgcallbot.maMessage(enCampMa, "insert");

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
				msg = msgapim.maMessage(enCampMa, "insert");

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
