package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.kafMsges.MsgCallbot;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.CreateEntity;
import gc.apiClient.service.ServiceInstCmpRt;
import gc.apiClient.service.ServiceJson;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j

/**
 *	이 클래스에는 크게 2가지 로직이 존재한다. 
 *  첫번째, from 카프카 to 제네시스 
 *  두번째, from 제네시스 to 카프카
 *  첫번째 같은 경우에는 카프카로 부터 아웃바운데 발신 대상자들을 받아서 제네시스쪽으로 푸쉬를한다
 *  두번째 같은 경우는 아웃바운드 발신이 끝나면 그 결과를 제네시스로부터 받아 카프카로 보낸다. 
 *  엄밀히 말하면 카프카서버와 메시지를 주고, 받는 역할은 각각 우리가 만든 producer, consumer 앱들이 전적으로 담당하고 
 *  그 두 앱은 오직 그 역할만 한다. 따라서 GcApiGateWay 앱은 producer 앱과 consumer앱을 통해 카프카 서버와 간접적으로 통신한다.
 * 
 */

public class ControllerCallBot {

	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

	private final InterfaceDBPostgreSQL serviceDb;
	private final ServiceInstCmpRt serviceInstCmpRt;
	private final InterfaceWebClient serviceWeb;
	private final CreateEntity createEntity;

	public ControllerCallBot(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb, CreateEntity createEntity, ServiceInstCmpRt serviceInstCmpRt) {
		this.serviceDb = serviceDb;
		this.createEntity = createEntity;
		this.serviceWeb = serviceWeb;
		this.serviceInstCmpRt = serviceInstCmpRt;
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
	public Mono<ResponseEntity<String>> callbotMsgFrmCnsumer(@PathVariable("topic") String tranId, @RequestBody String msg) {

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

		JSONObject contactltObj = null;
		String cpid = "";
		String topic_id = tranId;
		String contactLtId = "";
		Entity_CampMa enCpma = null;
		List<String> arr = new ArrayList<String>();

		log.info("topic_id : {}", topic_id);

		switch (topic_id) {

		case "callbothome":// IF-CRM_003
		case "callbotmobile":// IF-CRM_004

			try {

				contactltObj = ServiceJson.extractObjVal("ExtractValCallBot", msg, 0);// 뽑아온다.cpid::cpsq::cske::csno::tkda::flag

				Entity_ContactLt enContactLt = createEntity.createContactLtMsg(contactltObj);// ContactLt 테이블에 들어갈 값들을
				// Entity_ContactLt 객체에 매핑시킨다.
				cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.
				enCpma = serviceDb.findCampMaByCpid(cpid);
				contactLtId = enCpma.getContactltid();

				log.info("컨텍리스트 아이디 : {}", contactLtId);

				for (int i = 0; i < cntofmsg; i++) {

					contactltObj = ServiceJson.extractObjVal("ExtractValCallBot", msg, i); // 뽑아온다.cpid::cpsq::cske::csno::tkda::flag
					contactltObj.put("contactltid", contactLtId);
					
					enContactLt = createEntity.createContactLtMsg(contactltObj);// ContactLt 테이블에 들어갈 값들을
					String contactltMapper = createEntity.createContactLtGC(contactltObj);

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

				serviceWeb.postContactLtApiReq("contact", contactLtId, arr);// api : "/api/v2/outbound/contactlists/{contactListId}/contacts";

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

				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, String> mapcontactltId = new HashMap<String, String>();// 키 : cpid, 값 : contactLtId
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				
				List<String> invalid_camp = new ArrayList<String>();
				Entity_CampMa_D enCpma_D = null;
				String contactLtId = "";
				String divisionid = "";
				String cpid = "";
				String cqsq = "";

				for (int i = 0; i < reps; i++) {

					Entity_CallbotRt enCallbotRt = entitylist.getContent().get(i);
					cpid = enCallbotRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					cqsq = enCallbotRt.getId().getCpsq(); // 첫번째 레코드부터 cpsq를 가지고 온다.
					contactLtId = mapcontactltId.get(cpid);
					divisionid = enCallbotRt.getDivisionid();

					if (contactLtId == null || contactLtId.equals("")) {

						if (invalid_camp.contains(cpid)) {// 지금 레코드에 있는 캠페인 아이디가 유효하지 않은 캠페인 아이디라면 api호출 없이 DB에서 해당 레코드 삭제 후 그냥 다음 레코드로
							// 넘어감.
							serviceDb.delCallBotRtById(enCallbotRt.getId());
							continue;
						}

						String result = serviceWeb.getCampaignsApiReq("campaignId", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

						if (result.equals("")) {// cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아내려고 했는데 결과 값이 없다면, 혹시 campma_d테이블에
												// 존재하는지 조회.

							try {
								log.info("캠페인 아이디 ({})로 api호출 결과 결과가 없습니다. 마스터D 테이블을 조회합니다.", cpid);
								enCpma_D = serviceDb.findCampMa_DByCpid(cpid);

								contactLtId = enCpma_D.getContactltid();
								mapcontactltId.put(cpid, contactLtId);
								mapdivision.put(contactLtId, divisionid);

							} catch (Exception e) {// campma_d테이블을 조회했는데도 없다면 유효하지 않은 캠페인으로 처리하고 해당레코드 삭제
								log.info("마스터D 테이블 조회 결과 유효한 캠페인 아이디 ({})가 아닙니다", cpid);
								invalid_camp.add(cpid); // 유효하지 않은 캠페인 저장.
								serviceDb.delCallBotRtById(enCallbotRt.getId());
								// 밑의 로직을 수행하지 않고 다음 i번째로 넘어간다.
								continue;
							}

						} else {

							JSONObject res = ServiceJson.extractObjVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예 )contactlistid::queueid
							contactLtId = res.getString("contactltid");
							mapcontactltId.put(cpid, contactLtId);
							mapdivision.put(contactLtId, divisionid);
						}

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
						divisionid = mapdivision.get(entry.getKey());
						// 캠페인 발신결과 전송 (G.C API add contact bulk 데이터는 한번에 최대 50개까지 add 가능)
						if (entry.getValue().size() >= 50) {
							sendCampRtToCallbot(entry.getKey(), entry.getValue(), divisionid);
						}
					}
				}
				
				invalid_camp.clear();

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
					divisionid = mapdivision.get(entry.getKey());
					sendCampRtToCallbot(entry.getKey(), entry.getValue(), divisionid);
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
	public Mono<Void> sendCampRtToCallbot(String contactLtId, List<String> values, String divisionid) throws Exception {

		log.info("====== Method : sendCampRtToCallbot ======");
		// 컨택리스트(contactListId)별 컨택데이터(contact-cpsq)를 Genesys Cloud로 전송 (Bulk)
		String result = serviceWeb.postContactLtApiBulk("contactList", contactLtId, values); // api : "/api/v2/outbound/contactlists/{contactListId}/contacts/bulk";

		if (result.equals("[]")) {
			values.clear();
			return Mono.empty();
		}

		/* 캠페인 결과 발신에 필요한 데이터 세팅 */
		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.
		// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
		JSONObject contactsresult = ServiceJson.extractObjVal("ExtractContacts", result, 0);
		String cpid = contactsresult.getString("cpid");

		Entity_CampMa enCampMa = serviceDb.findCampMaByCpid(cpid);
		Entity_CampRt entityCmRt = null;

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.rtSelectedBusiness(divisionid);
		String topic_id = businessLogic.get("topic_id");

		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractObjVal("ExtractContacts", result, i);
			if (contactsresult == null) {
				log.info("결과 없음, 다음으로 건너 뜀.");
				continue;
			}

			entityCmRt = createEntity.createCampRtMsg(contactsresult, enCampMa);// db 인서트 하기 위한 entity.

			MsgCallbot msgcallbot = new MsgCallbot(serviceDb);
			String msg = msgcallbot.makeRtMsg(entityCmRt);

			MessageToProducer producer = new MessageToProducer();
			String endpoint = "/gcapi/post/" + topic_id;
			producer.sendMsgToProducer(endpoint, msg);

			// db인서트
			try {
				serviceInstCmpRt.insrtCmpRt(contactsresult, enCampMa);
//				serviceDb.insertCampRt(entityCmRt);
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
