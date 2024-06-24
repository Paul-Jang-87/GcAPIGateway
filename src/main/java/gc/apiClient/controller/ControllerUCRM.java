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

import org.springframework.data.domain.Page;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import kafMsges.MsgUcrm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerUCRM {

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb,
			CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 60000)// 1분 간격으로 함수 'SendUcrmRt' 스케줄 돌림.
	public void scheduledMethod() {

		Mono.fromCallable(() -> SendUcrmRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}

	@Scheduled(fixedRate = 5000)// 5초 간격으로 함수 'UcrmMsgFrmCnsmer' 스케줄 돌림. 
	public void UcrmContactlt() {
		Mono.fromCallable(() -> UcrmMsgFrmCnsmer()).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@PostMapping("/saveucrmdata")//이것을 카프카 컨슈머에서 호출하는 api. 컨슈머 앱에서 특정 토픽을 구독하면서 메시지를 받는다. 메시지를 받은 컨슈머는 이 api를 호출하여 받은 메시지를 전달해준다. 
	public Mono<ResponseEntity<String>> SaveUcrmData(@RequestBody String msg) {

		try {

			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SaveUcrmData ======");
			Entity_Ucrm enUcrm = serviceDb.createUcrm(msg); //전달 받은 String 형태의 메시지를 쉐도우테이블('UCRMLT')에 인서트 하기 위해서 Entity 형태로 제가공해준다. 

			try {
				serviceDb.InsertUcrm(enUcrm);
				log.info("저장된 메시지 : {}", msg);
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : %s", e.getMessage())));
		}

		log.info("====== End SaveUcrmData ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	
	public Mono<ResponseEntity<String>> UcrmMsgFrmCnsmer() {//이 함수는 스케줄러에 의해 5초마다 실행되면서 쉐도우 테이블('UCRMLT')에 있는 데이터들을 처리해주는 작업을 수행한다. 

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : UcrmMsgFrmCnsmer ======");

			Page<Entity_Ucrm> entitylist = serviceDb.getAll();//바로 위의 함수 'SaveUcrmData'에 의해 테이블에 적재된 데이터들은 최대 1000개씩 불러온다. 

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				int reps = entitylist.getNumberOfElements();
				log.info("number of records from 'UCRMLT' table : {}", reps);
				log.info("'UCRMLT'테블에서 조회된 레코드 개수 : {}", reps);
				log.info("{}만큼 반복", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();//키 : cpid, 값 : contactLtId
				Map<String, String> mapquetId = new HashMap<String, String>();//키 : cpid, 값 : queid
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();//키 : contactLtId, 값 : 발신대상자 배열
				Map<String, List<String>> delcontactlists = new HashMap<String, List<String>>();
				String contactLtId = "";

				for (int i = 0; i < reps; i++) {//가져온 레코드 갯수만큼 반복. 

					Entity_ContactLt enContactLt = serviceDb.createContactUcrm(entitylist.getContent().get(i));

					String cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid);
					String queid = mapquetId.get(cpid);

					if (contactLtId == null || contactLtId.equals("")) {// cpid로 Map(mapcontactltId)을 조회했는데 Map 안에 그것(cpid)에 대응하는 contactltId가 없다면,
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);//cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다.
						if(result.equals("")) {
							//쉐도우 테이블에서 삭제 테이블명 'UCRMLT'
							for (int j = 0; j < reps; j++) {
								serviceDb.DelUcrmLtById(entitylist.getContent().get(j).getTopcDataIsueSno());
							}
							return null;
						}
						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); // 가져온 결과에서
																								// contactlistid,queueid만
																								// 추출. 변수 'res' 형식의 예 ) contactlistid::queueid
						contactLtId = res.split("::")[0];
						queid = res.split("::")[1];

						mapcontactltId.put(cpid, contactLtId);
						mapquetId.put(cpid, res.split("::")[1]);
					} 

					String row_result = ServiceJson.extractStrVal("ExtractRawUcrm", entitylist.getContent().get(i));
					row_result = row_result + "::" + contactLtId + "::" + queid; 
					String contactltMapper = serviceDb.createContactLtGC(row_result); // row_result = cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					contactlists.get(contactLtId).add(contactltMapper);

					// db인서트
					try {
						serviceDb.InsertContactLt(enContactLt);//테이블명 'contactlt'

					} catch (DataIntegrityViolationException ex) {//인서트 하려고 했는데 이미 있는 데이터여서 에러가 발생한 경우
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
						
						// 해당 레코드의 flag 값이 'D'이면 회수하라는 의미 -> 즉 삭제하라는 의미.
						if (enContactLt.getFlag().equals("D")) {
							log.error("flag가 'D', 레코드 삭제 cpid '{}' /cpsq '{}'",cpid, row_result.split("::")[1]);

							//============  Genesys contact list 삭제를 위한 Map 세팅
							// 삭제가 필요한 contact들만 따로 Map으로 모아둔다.
							if (!delcontactlists.containsKey(contactLtId)) {
								delcontactlists.put(contactLtId, new ArrayList<>());
							}
							delcontactlists.get(contactLtId).add(row_result.split("::")[1]);
							
							// DB 'CONTACTLT' 테이블에서 row 데이터 삭제
							serviceDb.DelContactltById(enContactLt.getId());
						}
						
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}

					//쉐도우 테이블에서 삭제 테이블명 'UCRMLT'
					serviceDb.DelUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());

				}

				// 캠페인 컨택리스트 적재를 위한 Genesys API 호출 (add contact)
				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					log.info("Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
					serviceWeb.PostContactLtApiRequet("contact", entry.getKey(), entry.getValue());
				}

				// 캠페인 컨택리스트 삭제를 위한 Genesys API 호출 (delete contact)
				for (Map.Entry<String, List<String>> entry : delcontactlists.entrySet()) {

					log.info("Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
					serviceWeb.DelContacts("delcontacts", entry.getKey(), entry.getValue());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@GetMapping("/senducrmrt")
	public Mono<ResponseEntity<String>> SendUcrmRt() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SendUcrmRt ======");

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
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
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
					serviceDb.DelUcrmRtById(enUcrmRt.getId());

					log.info("해당 키 값이 '{}'인 Arraylist에 값 추가", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionName = mapdivision.get(entry.getKey());

						if (entry.getValue().size() >= 50) {
							Roop(entry.getKey(), entry.getValue(), divisionName);
						}
					}

				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					divisionName = mapdivision.get(entry.getKey());
					Roop(entry.getKey(), entry.getValue(), divisionName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
		}

		log.info("====== End SendUcrmRt ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	public Mono<Void> Roop(String contactLtId, List<String> values, String divisionName) throws Exception {

		String result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			log.info("결과 없음, 다음으로 건너 뜀.");
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.

		String contactsresult = ServiceJson.extractStrVal("ExtractContacts56", result, 0);// JsonString 결과값과 조회하고 싶은
																							// 인덱스(첫번째)를 인자로
		// 넣는다.
		Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// contactsresult값으로
																				// entity하나를 만든다.
		Character tkda = entityCmRt.getTkda().charAt(0);// 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(tkda, divisionName);
		String topic_id = businessLogic.get("topic_id");

		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractStrVal("ExtractContacts56", result, i);
			if (contactsresult.equals("")) {
				log.info("결과 없음, 다음으로 건너 뜀.");
				continue;
			}

			entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

			MsgUcrm msgucrm = new MsgUcrm(serviceDb);
			String msg = msgucrm.rtMassage(entityCmRt);

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
				serviceDb.InsertCampRt(entityCmRt);
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
