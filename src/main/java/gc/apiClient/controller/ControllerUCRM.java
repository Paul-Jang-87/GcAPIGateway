package gc.apiClient.controller;

//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.Random;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import jakarta.transaction.Transactional;
import kafMsges.MsgUcrm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@Slf4j
public class ControllerUCRM {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb,
			CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 60000) // 1분 간격으로 함수 'SendUcrmRt' 스케줄 돌림.
	public void scheduledMethod() {

		Mono.fromCallable(() -> sendUcrmRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}

	@Scheduled(fixedRate = 5000) // 5초 간격으로 함수 'UcrmMsgFrmCnsmer' 스케줄 돌림.
	public void ucrmContactlt() {
		Mono.fromCallable(() -> ucrmMsgFrmCnsmer()).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	// 이것을 카프카 컨슈머에서 호출하는 api. 컨슈머 앱에서 특정 토픽을 구독하면서 메시지를 받는다. 메시지를 받은 컨슈머는 이 api를 호출하여 받은 메시지를 전달해준다.
	@PostMapping("/saveucrmdata")
	public Mono<ResponseEntity<String>> saveUcrmData(@RequestBody String msg) {

		try {

			log.info("====== Method : saveUcrmData ======");
			Entity_Ucrm enUcrm = serviceDb.createUcrm(msg); // 전달 받은 String 형태의 메시지를 쉐도우테이블('UCRMLT')에 인서트 하기 위해서 Entity 형태로 제가공해준다.
			try {
				serviceDb.insertUcrm(enUcrm);
				log.info("저장된 메시지 : {}", msg);
			} catch (Exception ex) {
				log.error("saveUcrmData Exception 발생 : {}", ex.getMessage());
				errorLogger.error(ex.getMessage(), ex);
			}

		} catch (Exception e) {
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : %s", e.getMessage())));
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@Transactional
	public Mono<ResponseEntity<String>> ucrmMsgFrmCnsmer() {// 이 함수는 스케줄러에 의해 5초마다 실행되면서 쉐도우 테이블('UCRMLT')에 있는 데이터들을 처리해주는 작업을 수행한다.
		try {
			log.info("====== Method : ucrmMsgFrmCnsmer ======");

			Page<Entity_Ucrm> entitylist = serviceDb.getAll();// 바로 위의 함수 'SaveUcrmData'에 의해 테이블에 적재된 데이터들은 최대 1000개씩 불러온다.

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				int reps = entitylist.getNumberOfElements();
				log.info("number of records from 'UCRMLT' table : {}", reps);
				log.info("'UCRMLT'테블에서 조회된 레코드 개수 : {}", reps);
				log.info("{}만큼 반복", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();// 키 : cpid, 값 : contactLtId
				Map<String, String> mapquetId = new HashMap<String, String>();// 키 : cpid, 값 : queid
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();// 키 : contactLtId, 값
																								// :발신대상자 배열
				Map<String, List<String>> delcontactlists = new HashMap<String, List<String>>();
				String contactLtId = "";
				String flag = "";
				String cpid = "";
				String queid = "";

				for (int i = 0; i < reps; i++) {// 가져온 레코드 갯수만큼 반복.

					// UCRM에서 보내준 데이터를 우리쪽 contactlt테이블에 넣기 위해 재가공한 엔티티.
					Entity_ContactLt enContactLt = serviceDb.createContactUcrm(entitylist.getContent().get(i));

					cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					flag = entitylist.getContent().get(i).getWorkDivsCd();

					contactLtId = mapcontactltId.get(cpid);
					queid = mapquetId.get(cpid);

					if (contactLtId == null || contactLtId.equals("")) {// cpid로 Map(mapcontactltId)을 조회했는데 Map 안에 그것(cpid)에 대응하는 contactltId가 없다면,

						String result = serviceWeb.getCampaignsApiReq("campaigns", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

						if (result.equals("")) {// cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아내려고 했는데 결과 값이 없다면,
							// 쉐도우 테이블에서 삭제. 테이블명 'UCRMLT'
							serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
							log.info("캠페인 조회 결과 유효한 캠페인 아이디 ({})가 아닙니다", cpid);
							// 밑의 로직을 수행하지 않고 다음 i번째로 넘어간다.
							continue;
						}

						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예 )contactlistid::queueid
						contactLtId = res.split("::")[0];
						queid = res.split("::")[1];

						mapcontactltId.put(cpid, contactLtId);
						mapquetId.put(cpid, res.split("::")[1]);
					}

					// UCRM에서 보내 준 데이터를 가지고 제네시스에 PUSH하기 위해서 필요한 데이터들을 추출한다.
					String row_result = ServiceJson.extractStrVal("ExtractRawUcrm", entitylist.getContent().get(i));
					row_result = row_result + "::" + contactLtId + "::" + queid;
					String contactltMapper = serviceDb.createContactLtGC(row_result); // row_result = cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

					// 제네시스에 인서트 하기 위한 배열 준비
					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}

					// 제네시스에서 회수 하기 위한 배열 준비
					if (!delcontactlists.containsKey(contactLtId)) {
						delcontactlists.put(contactLtId, new ArrayList<>());
					}

					
					try {
						if (flag.equals("A")) { // Add Contact
							contactlists.get(contactLtId).add(contactltMapper);
//							serviceDb.insertContactLt(enContactLt); // 테이블명 'contactlt'
						} else if (flag.equals("D")) { // Delete Contact
							
							JSONObject jsonObject = new JSONObject(contactltMapper);
							JSONObject dataObject = jsonObject.getJSONObject("data");

							// CPID와 CPSQ 값을 추출
							cpid = dataObject.getString("CPID");
							String cpsq = dataObject.getString("CPSQ");
							delcontactlists.get(contactLtId).add(contactltMapper);
							serviceDb.delUcrmltRecord(cpid, cpsq);//ucrmlt (쉐도우테이블에서 삭제)
							serviceDb.delContactltRecord(cpid, cpsq);//contactlt 테이블 삭제
//							serviceDb.delContactltById(enContactLt.getId());
						} else {
							log.info("FLAG A or D가 아님");
						}

					} catch (Exception ex) { // 인서트 하려고 했는데 이미 있는 데이터여서 에러가 발생한 경우
						log.error("Exception 발생 : {}, {}", ex.getMessage(), enContactLt.toString());
						errorLogger.error(ex.getMessage(), ex);
					}

					// 쉐도우 테이블에서 삭제 테이블명 'UCRMLT'
//					serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
				}   

				// 캠페인 컨택리스트 적재를 위한 Genesys API 호출 (add contact)  
				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
					log.info("(Add)Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
					// entry.getValue().size()=0 일때 API 호출을 할 필요가 없음.
					if (entry.getValue().size() > 0) {
						serviceWeb.postContactLtApiReq("contact", entry.getKey(), entry.getValue());

						for (int i = 0; i < entry.getValue().size(); i++) {

							JSONObject jsonObject = new JSONObject(entry.getValue().get(i));
							JSONObject dataObject = jsonObject.getJSONObject("data");

							// CPID와 CPSQ 값을 추출
							cpid = dataObject.getString("CPID");
							String cpsq = dataObject.getString("CPSQ");
							Entity_ContactLt enContactLt = serviceDb.createContactUcrm(jsonObject);
							
							serviceDb.delUcrmltRecord(cpid, cpsq);//ucrmlt (쉐도우테이블에서 삭제)
							serviceDb.insertContactLt(enContactLt); //contactlt테이블에 인서트
						}

					}
				}

				// 캠페인 컨택리스트 삭제를 위한 Genesys API 호출 (delete contact)
				for (Map.Entry<String, List<String>> entry : delcontactlists.entrySet()) {
					log.info("(delete)Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
					// entry.getValue().size()=0 일때 API 호출을 할 필요가 없음.
					if (entry.getValue().size() > 0) {
						serviceWeb.delContacts("delcontacts", entry.getKey(), entry.getValue());
						
//						for (int i = 0; i < entry.getValue().size(); i++) {
//
//							JSONObject jsonObject = new JSONObject(entry.getValue().get(i));
//							JSONObject dataObject = jsonObject.getJSONObject("data");
//
//							// CPID와 CPSQ 값을 추출
//							String cpid = dataObject.getString("CPID");
//							String cpsq = dataObject.getString("CPSQ");
//							
//							serviceDb.delUcrmltRecord(cpid, cpsq);//ucrmlt (쉐도우테이블에서 삭제)
//							serviceDb.delContactltRecord(cpid, cpsq);//contactlt테이블에서 삭제
//
//						}
					}
				}

			}

		} catch (Exception e) {
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@GetMapping("/senducrmrt")
	public Mono<ResponseEntity<String>> sendUcrmRt() {

		try {
			log.info("====== Method : sendUcrmRt ======");

			Page<Entity_UcrmRt> entitylist = serviceDb.getAllUcrmRt();

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				log.info("DB에서 조회 된 모든 레코드 : {}", entitylist.toString());
				int reps = entitylist.getNumberOfElements();
				log.info("'CAMPRT_UCRM_W'' table에서 조회 된 레코드 개수 : {}", reps);
				log.info("{}만큼 반복", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();
				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				String contactLtId = "";
				String divisionName = "";
				String cpid = "";

				for (int i = 0; i < reps; i++) {

					Entity_UcrmRt enUcrmRt = entitylist.getContent().get(i);

					cpid = enUcrmRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					String cqsq = enUcrmRt.getId().getCpsq(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid) != null ? mapcontactltId.get(cpid) : "";
					divisionName = mapdivision.get(contactLtId) != null ? mapdivision.get(contactLtId) : "";

					if (contactLtId == null || contactLtId.equals("")) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("일치하는 contactLtId 없음");
						String result = serviceWeb.getCampaignsApiReq("campaigns", cpid);
						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); // 가져온 결과에서
																								// contactlistid,queueid만
																								// 추출.
						contactLtId = res.split("::")[0];

						String division = enUcrmRt.getDivisionid(); // 첫번째 레코드부터 cpid를 가지고 온다.
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");

						mapcontactltId.put(cpid, contactLtId);
						mapdivision.put(contactLtId, divisionName);
					} else {
						log.info("일치하는 contactLtId 있음");
					}

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					contactlists.get(contactLtId).add(cqsq);
					serviceDb.delUcrmRtById(enUcrmRt.getId());

					log.info("해당 키 값이 '{}'인 Arraylist에 값 추가", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionName = mapdivision.get(entry.getKey());

						if (entry.getValue().size() >= 50) {
							sendCampRtToCUcrm(entry.getKey(), entry.getValue(), divisionName);
						}
					}

				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					divisionName = mapdivision.get(entry.getKey());
					sendCampRtToCUcrm(entry.getKey(), entry.getValue(), divisionName);
				}

			}

		} catch (Exception e) {
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	public Mono<Void> sendCampRtToCUcrm(String contactLtId, List<String> values, String divisionName) throws Exception {

		String result = serviceWeb.postContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			log.info("결과 없음, 다음으로 건너 뜀.");
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.

		String contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, 0);// JsonString 결과값과 조회하고
																						// 싶은인덱스(첫번째)를 인자로 넣는다.
		String cpid = contactsresult.split("::")[2];
		String full_tkda = contactsresult.split("::")[5];

		Entity_CampMa enCampMa = serviceDb.findCampMaByCpid(cpid);
		Entity_CampRt entityCmRt = null;

		Character tkda = full_tkda.charAt(0); // 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.selectedBusiness(tkda, divisionName);
		String topic_id = businessLogic.get("topic_id");

		for (int i = 0; i < values.size(); i++) {
			contactsresult = ServiceJson.extractStrVal("ExtractContacts", result, i);
			if (contactsresult.equals("")) {
				log.info("결과 없음, 다음으로 건너 뜀.");
				continue;
			}

			entityCmRt = serviceDb.createCampRtMsg(contactsresult, enCampMa); // db 인서트 하기 위한 entity.

			MsgUcrm msgucrm = new MsgUcrm(serviceDb);
			String msg = msgucrm.rtMessage(entityCmRt);

			int dirt = entityCmRt.getDirt();// 응답코드

			if (dirt == 1) {// URM이면서 정상일 때.
				log.info("UCRM : dirt(응답코드)가 '1'이므로 카프카로 발신결과 메시지를 보내지 않습니다.");
			} else {
				MessageToProducer producer = new MessageToProducer();
				String endpoint = "/gcapi/post/" + topic_id;
				producer.sendMsgToProducer(endpoint, msg);
			}

			// db인서트
			try {
				serviceDb.insertCampRt(entityCmRt);
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
				errorLogger.error(ex.getMessage(), ex);
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
				errorLogger.error(ex.getMessage(), ex);
			}
		}
		values.clear();
		return Mono.empty();

	}

}
