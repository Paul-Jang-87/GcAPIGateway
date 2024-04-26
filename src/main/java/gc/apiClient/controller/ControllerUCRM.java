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

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.Entity_ToApim;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageToApim;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerUCRM extends ServiceJson {

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb,
			InterfaceWebClient serviceWeb, CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 60000)
	public void scheduledMethod() {
		
		Mono.fromCallable(() -> SendUcrmRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}

	@Scheduled(fixedRate = 5000)
	public void UcrmContactlt() {
		Mono.fromCallable(() -> UcrmMsgFrmCnsmer()).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}


	@PostMapping("/saveucrmdata")
	public Mono<ResponseEntity<String>> SaveUcrmData(@RequestBody String msg) {

		try {

			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SaveUcrmData ======");
			Entity_Ucrm enUcrm = serviceDb.createUcrm(msg);

			try {
				serviceDb.InsertUcrm(enUcrm);
				log.info("Saved Message : {}",msg);
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : {}", e.getMessage())));
		}

		log.info("====== End SaveUcrmData ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}


	public Mono<ResponseEntity<String>> UcrmMsgFrmCnsmer() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : UcrmMsgFrmCnsmer ======");

			Page<Entity_Ucrm> entitylist = serviceDb.getAll();

			if (entitylist.isEmpty()) {
				log.info("All records from DB : Nothing");
			} else {
				log.info("All records from DB : {}", entitylist.toString());
				int reps = entitylist.getNumberOfElements();
				log.info("number of records : {}", reps);
				log.info("{}만큼 반복,", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();
				Map<String, String> mapquetId = new HashMap<String, String>();
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();
				Map<String, List<String>> delcontactlists = new HashMap<String, List<String>>();
				String contactLtId = "";

				for (int i = 0; i < reps; i++) {

					Entity_ContactLt enContactLt = serviceDb.createContactUcrm(entitylist.getContent().get(i));

					String cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid);
					String queid = mapquetId.get(cpid);

					if (contactLtId == null || contactLtId.equals("")) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];
						queid = res.split("::")[1];

						mapcontactltId.put(cpid, contactLtId);
						mapquetId.put(cpid, res.split("::")[1]);
					} else {
					}

					String row_result = ExtractRawUcrm(entitylist.getContent().get(i));
					row_result = row_result + "::" + contactLtId + "::" + queid;
					String contactltMapper = serviceDb.createContactLtGC(row_result);

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					contactlists.get(contactLtId).add(contactltMapper);

					// db인서트
					try {
						serviceDb.InsertContactLt(enContactLt);

					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
						if(enContactLt.getFlag().equals("D")) {
							log.error("flag is 'D', delete record");
							
							if (!delcontactlists.containsKey(contactLtId)) {
								delcontactlists.put(contactLtId, new ArrayList<>());
							}
							delcontactlists.get(contactLtId).add(row_result.split("::")[1]);
							
							serviceDb.DelContactltById(enContactLt.getId());//contactlt테이블에서 삭제
						}
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}

					try {
					} catch (Exception e) {
						log.error("Error Message", e.getMessage());
						e.printStackTrace();
					}

					//컨슈머에서 던져줘서 임시로 적재해 두는 UCRM 테이블에서 삭제 
					serviceDb.DelUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());

				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					log.info("Now the size of Arraylist '{}': {}", entry.getKey(), entry.getValue().size());
					serviceWeb.PostContactLtApiRequet("contact", entry.getKey(), entry.getValue());
				}
				
				for (Map.Entry<String, List<String>> entry : delcontactlists.entrySet()) {

					log.info("Now the size of Arraylist '{}': {}", entry.getKey(), entry.getValue().size());
					serviceWeb.DelContacts("delcontacts", entry.getKey(), entry.getValue());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
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
				log.info("All records from DB : Nothing");
			} else {
				log.info("All records from DB : {}", entitylist.toString());
				int reps = entitylist.getNumberOfElements();
				log.info("number of records : {}", reps);
				log.info("{}만큼 반복,", reps);

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
						log.info("Nomatch contactId");
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];

						String division = enUcrmRt.getDivisionid();  // 첫번째 레코드부터 cpid를 가지고 온다.
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "couldn't find division");

						mapcontactltId.put(cpid, contactLtId);
						mapdivision.put(contactLtId, divisionName);
					} else {
						log.info("Matched contactId");
					}

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					contactlists.get(contactLtId).add(cqsq);
					serviceDb.DelUcrmRtById(enUcrmRt.getId());
					
					log.info("Add value into Arraylist named '{}'", contactLtId);

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
			log.error("Error Message : {}", e.getMessage());
		}

		log.info("====== End SendUcrmRt ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	
	public Mono<Void> Roop(String contactLtId, List<String> values, String divisionName) throws Exception {

		ObjectMapper objectMapper = null;
		String result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			log.info("No result, skip to next");
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.
		String contactsresult = ExtractContacts56(result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로
																// 넣는다.
		Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// contactsresult값으로
																				// entity하나를 만든다.
		Character tkda = entityCmRt.getTkda().charAt(0);// 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(tkda, divisionName);
		String business = businessLogic.get("business");
		String topic_id = businessLogic.get("topic_id");

		switch (business) {
		case "UCRM": // UCRM일 경우.
		case "CALLBOT": // 콜봇일 경우.

			for (int i = 0; i < values.size(); i++) {

				contactsresult = ExtractContacts56(result, i);
				if (contactsresult.equals("")) {
					log.info("No value, skip to next");
					continue;
				}

				entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

				int dirt = entityCmRt.getDirt();// 응답코드

				if ((business.equals("UCRM")) && (dirt == 1)) {// URM이면서 정상일 때.
					log.info("UCRM : dirt(responsed code) is '1'. skip sending message to kafka ");
				} else {
					JSONObject toproducer = serviceDb.createCampRtJson(entityCmRt, business);// producer로
																								// 보내기
																								// 위한
					// entity.
					objectMapper = new ObjectMapper();

					try {
						String jsonString = toproducer.toString();
						log.info("JsonString Data : {}번째 {}", i, jsonString);

						MessageToProducer producer = new MessageToProducer();
						String endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, jsonString);

					} catch (Exception e) {
						e.printStackTrace();
					}
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

		default:

			for (int i = 0; i < values.size(); i++) {

				int dirt = entityCmRt.getDirt();// 응답코드
				String tokendata = entityCmRt.getTkda();// 토큰데이터

				Entity_ToApim enToApim = new Entity_ToApim();
				enToApim.setDirt(dirt);
				enToApim.setTkda(tokendata);

				apimEntitylt.add(enToApim);
			}

			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(apimEntitylt);

				// localhost:8084/dspRslt
				// 192.168.219.134:8084/dspRslt
				MessageToApim apim = new MessageToApim();
				String endpoint = "/dspRslt";
				apim.sendMsgToApim(endpoint, jsonString);
				log.info("CAMPRT 로직, APIM으로 보냄. : {} ", jsonString);

			} catch (Exception e) {
				e.printStackTrace();
			}
			values.clear();
			return Mono.empty();
		}

	}


}
