package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import kafMsges.MsgCallbot;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@Slf4j
public class ControllerCallBot {

	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;

	public ControllerCallBot(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb,
			CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 60000) // 1분 간격으로 'SendCallBotRt' 비동기적으로 실행.
	public void scheduledMethod() {
		/*
		 * SendCallBotRt() 메서드를 비동기 방식 스케쥴링을 위한 메서드 Mono.fromCallable : 주어진 callable을
		 * 호출하고 그 결과를 발행하는 Mono를 생성 subscribeOn(Schedulers.boundedElastic() : 작업을 실행할
		 * 스케쥴러를 설정. (스레드풀 제공) subscribe() : 'Mono'를 구독하여 실제로 작업이 수행 됨.
		 */
		Mono.fromCallable(() -> sendCallBotRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}

	/**
	 * 
	 * 캠페인 대상자 전송 To Genesys (from kafka-consumer)
	 * 
	 * @param tranId
	 * @param msg
	 * @return
	 */
	@PostMapping("/contactlt/{topic}")
	public Mono<ResponseEntity<String>> callbotMsgFrmCnsumer(@PathVariable("topic") String tranId,
			@RequestBody String msg) {

		log.info("====== Method : callbotMsgFrmCnsumer ======");

		String jsonResponse = msg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int casenum = 0;

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			casenum = jsonNode.path("cmpnItemDto").size();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			errorLogger.error(e.getMessage(), e);
		}

		int cntofmsg = casenum;
		log.info("컨슈머로 부터 받은 발신 대상자 건수 : {}", cntofmsg);

		String row_result = "";
		String result = "";
		String cpid = "";
		String topic_id = tranId;
		String res = "";
		String contactLtId = "";
		List<String> arr = new ArrayList<String>();

		log.info("topic_id : {}", topic_id);

		switch (topic_id) {

		case "callbothome":// IF-CRM_003
		case "callbotmobile":// IF-CRM_004

			try {

				row_result = ServiceJson.extractStrVal("ExtractValCallBot", msg, 0);// 뽑아온다.cpid::cpsq::cske::csno::tkda::flag

				Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
				// Entity_ContactLt 객체에 매핑시킨다.
				cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.

				result = serviceWeb.getCampaignsApiReq("campaigns", cpid);// 캠페인 아이디로
																				// "/api/v2/outbound/campaigns/{campaignId}"호출
																				// 후 결과 가져온다.
				res = ServiceJson.extractStrVal("ExtractContactLtId", result);
				contactLtId = res.split("::")[0];
				log.info("컨텍리스트 아이디 : {}", contactLtId);

				for (int i = 0; i < cntofmsg; i++) {

					row_result = ServiceJson.extractStrVal("ExtractValCallBot", msg, i); // 뽑아온다.cpid::cpsq::cske::csno::tkda::flag

					enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
					// Entity_ContactLt 객체에 매핑시킨다.
					row_result = row_result + "::" + res; // 뽑아온다.cpid::cpsq::cske::csna::tkda::flag::contactLtId
					String contactltMapper = serviceDb.createContactLtGC(row_result);

					arr.add(contactltMapper);

					// db인서트
					try {
						serviceDb.insertContactLt(enContactLt);

					} catch (DataIntegrityViolationException ex) {
//						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());

					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}
				}

				serviceWeb.postContactLtApiReq("contact", contactLtId, arr);

				return Mono.just(ResponseEntity.ok("Successfully processed the message."));

			} catch (Exception e) {
				log.error("에러 메시지 : {}", e.getMessage());
				return Mono.just(ResponseEntity.ok().body(String.format("에러가 발생했습니다 : %s", e.getMessage())));
			}

		default:

			return Mono.just(ResponseEntity.badRequest().body("유효하지 않은 topic_id"));
		}
	}

	/**
	 * 1분 배치 (비동기 방식 스케쥴링) DB TABLE : CAMPRT_CALLBOT_W
	 * 
	 * @return
	 */
	@GetMapping("/sendcallbotrt")
	public Mono<ResponseEntity<String>> sendCallBotRt() {

		try {
			log.info("====== Method : sendCallBotRt ======");

			Page<Entity_CallbotRt> entitylist = serviceDb.getAllCallBotRt();

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				log.info("DB에서 조회 된 모든 레코드 : {}", entitylist.toString());
				int reps = entitylist.getNumberOfElements();
				log.info("'CAMPRT_CALLBOT_W' table에서 조회 된 레코드 개수 : {}", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();
				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				String contactLtId = "";
				String divisionName = "";
				String cpid = "";

				for (int i = 0; i < reps; i++) {

					Entity_CallbotRt enCallbotRt = entitylist.getContent().get(i);

					cpid = enCallbotRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					String cqsq = enCallbotRt.getId().getCpsq(); // 첫번째 레코드부터 cpsq를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid) != null ? mapcontactltId.get(cpid) : "";
					divisionName = mapdivision.get(contactLtId) != null ? mapdivision.get(contactLtId) : "";

					if (contactLtId == null || contactLtId.equals("")) { // cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("일치하는 contactLtId 없음");
						String result = serviceWeb.getCampaignsApiReq("campaigns", cpid);
						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); // result 에서
																								// contactlistid,queueid만
																								// 추출.
						contactLtId = res.split("::")[0];

						String division = enCallbotRt.getDivisionid();
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");

						mapcontactltId.put(cpid, contactLtId);
						mapdivision.put(contactLtId, divisionName.trim());
					}

					// contactList ID Mapping
					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					// contactListId 별로 cpsq 세팅
					contactlists.get(contactLtId).add(cqsq);
					// 복합키(camp_id, camp_seq)로 delete row
					serviceDb.delCallBotRtById(enCallbotRt.getId());

					log.info("아이디가 '{}'인 contactListId에 값 추가", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
						divisionName = mapdivision.get(entry.getKey());
						// 캠페인 발신결과 전송 (G.C API add contact bulk 데이터는 한번에 최대 50개까지 add 가능)
						if (entry.getValue().size() >= 50) {
							sendCampRtToCallbot(entry.getKey(), entry.getValue(), divisionName);
						}
					}
				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
					divisionName = mapdivision.get(entry.getKey());
					sendCampRtToCallbot(entry.getKey(), entry.getValue(), divisionName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	/**
	 * contact data List가 50건 이상인 경우 bulk 데이터를 50건씩 나눠서 API 호출 캠페인 결과 발신에 필요한 데이터 세팅
	 * kafka-producer 메세지 세팅 후 전송
	 * 
	 * @param contactLtId
	 * @param values(List) : contact data (cpsq)
	 * @param divisionName
	 * @return
	 * @throws Exception
	 */
	public Mono<Void> sendCampRtToCallbot(String contactLtId, List<String> values, String divisionName) throws Exception {

		
		log.info("====== Method : sendCampRtToCallbot ======");
		// 컨택리스트(contactListId)별 컨택데이터(contact-cpsq)를 Genesys Cloud로 전송 (Bulk)
		String result = serviceWeb.postContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			values.clear();
			return Mono.empty();
		}

		/* 캠페인 결과 발신에 필요한 데이터 세팅 */
		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.
		// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
		String contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, 0);

		Entity_CampMa enCampMa = new Entity_CampMa();
		enCampMa = serviceDb.findCampMaByCpid(contactsresult.split("::")[2]);
		
		// contacts result값으로 entity하나를 만든다.
		Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult, enCampMa);
		Character tkda = entityCmRt.getTkda().charAt(0); // 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.selectedBusiness(tkda, divisionName);
		String topic_id = businessLogic.get("topic_id");

		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, i);
			if (contactsresult.equals("")) {
				log.info("결과 없음, 다음으로 건너 뜀.");
				continue;
			}

			entityCmRt = serviceDb.createCampRtMsg(contactsresult, enCampMa);// db 인서트 하기 위한 entity.

			MsgCallbot msgcallbot = new MsgCallbot(serviceDb);
			String msg = msgcallbot.rtMessage(entityCmRt);

			MessageToProducer producer = new MessageToProducer();
			String endpoint = "/gcapi/post/" + topic_id;
			producer.sendMsgToProducer(endpoint, msg);

			// db인서트
			try {
				serviceDb.insertCampRt(entityCmRt);
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
			}
		}
		values.clear();
		return Mono.empty();

	}
}
