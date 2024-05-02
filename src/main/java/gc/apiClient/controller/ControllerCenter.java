package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reactor.core.scheduler.Schedulers;
import org.springframework.data.domain.Page;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.Entity_CampMaJsonUcrm;
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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerCenter extends ServiceJson {

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerCenter(InterfaceDBPostgreSQL serviceDb,
			InterfaceWebClient serviceWeb, CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 86400 * 1000)
	public void RefreshToken() {
		WebClientApp.EmptyTockenlt();
		
	}


	@Scheduled(fixedRate = 60000)
	public void scheduledMethod() {
		
		Mono.fromCallable(() -> ReceiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();
		Mono.fromCallable(() -> SendApimRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}

	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> ReceiveMessage(@PathVariable("topic") String tranId) {

		log.info(" ");
		log.info("====== Class : ControllerCenter - Method : ReceiveMessage ======");

		String row_result = "";
		String result = "";
		String topic_id = tranId;
		String division = "";
		String business = "";
		String endpoint = "/gcapi/post/" + topic_id;
		ObjectMapper objectMapper = null;
		int size = 0;
		int numberOfRecords = 0;

		log.info("topic_id : {}", topic_id);

		switch (topic_id) {

		case "campma":

			try {

				result = serviceWeb.GetApiRequet("campaignId");
				size = CampaignListSize(result); // G.C에서 불러온 캠페인 갯수.
				numberOfRecords = serviceDb.getRecordCount(); // 현재 레코드 갯수.

				if (size == numberOfRecords) {// campma 테이블에 이미 있는 캠페인이라면 pass.

				} else {

					int reps = size - numberOfRecords;
					log.info("{}번 반복", reps);
					while (reps-- > 0) {
						log.info("{}번째 인덱스 ", reps);
						row_result = ExtractValCrm12(result, reps); // cpid::coid::cpna::division ->
						// 캠페인아이디::테넌트아이디::캠페인명::디비전

						division = row_result.split("::")[3];

						Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(division);

						business = businessLogic.get("business");
						topic_id = businessLogic.get("topic_id");

						Entity_CampMa enCampMa = serviceDb.CreateEnCampMa(row_result);

						switch (business) {
						case "UCRM":
							objectMapper = new ObjectMapper();
							Entity_CampMaJsonUcrm enCampMaJson1 = serviceDb.JsonCampMaUcrm(enCampMa, "insert");
							try {

								try {
									serviceDb.InsertCampMa(enCampMa);
								} catch (DataIntegrityViolationException ex) {
									log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
									continue;
								} catch (DataAccessException ex) {
									log.error("DataAccessException 발생 : {}", ex.getMessage());
									continue;
								}

								String jsonString = objectMapper.writeValueAsString(enCampMaJson1);
								log.info("jsonString : {}", jsonString);
								MessageToProducer producer = new MessageToProducer();
								endpoint = "/gcapi/post/" + topic_id;
								producer.sendMsgToProducer(endpoint, jsonString);

							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}

							break;

						case "Callbot":

							objectMapper = new ObjectMapper();
							Entity_CampMaJson enCampMaJson = serviceDb.JsonCampMaCallbot(enCampMa, "insert");
							try {

								try {
									serviceDb.InsertCampMa(enCampMa);
								} catch (DataIntegrityViolationException ex) {
									log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
									continue;
								} catch (DataAccessException ex) {
									log.error("DataAccessException 발생 : {}", ex.getMessage());
									continue;
								}

								String jsonString = objectMapper.writeValueAsString(enCampMaJson);
								log.info("jsonString : {}", jsonString);
								MessageToProducer producer = new MessageToProducer();
								endpoint = "/gcapi/post/" + topic_id;
								producer.sendMsgToProducer(endpoint, jsonString);

							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}

							break;

						default:

							try {
								serviceDb.InsertCampMa(enCampMa);
							} catch (DataIntegrityViolationException ex) {
								log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
								continue;
							} catch (DataAccessException ex) {
								log.error("DataAccessException 발생 : {}", ex.getMessage());
								continue;
							}

							String jsonString = serviceDb.createMaMsgApim(enCampMa, "insert").toString();
							log.info("jsonString : {}", jsonString);
							// localhost:8084/dspRslt
							// 192.168.219.134:8084/dspRslt
							MessageToApim apim = new MessageToApim();
							endpoint = "/cmpnMstrRegist";
							apim.sendMsgToApim(endpoint, jsonString);
							log.info("CAMPMA 로직, APIM으로 보냄. : {}", jsonString);

							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error Messge : {}", e.getMessage());
			}
		}

		return Mono.empty();
	}

	@PostMapping("/updateOrDelCampma")
	public  Mono<ResponseEntity<String>> UpdateOrDelCampMa(@RequestBody String msg, HttpServletRequest request) throws Exception {

		String row_result = "";
		Entity_CampMa enCampMa = null;

		try {
			log.info(" ");
			log.info("====== Class : ControllerCenter - Method : UpdateOrDelCampMa ======");

			String ipAddress = request.getRemoteAddr();
			int port = request.getRemotePort();
			log.info("Request received from IP address and Port => {}:{}", ipAddress, port);

			row_result = ExtractCampMaUpdateOrDel(msg); // cpid::coid::cpna::divisionid::action
			String division = row_result.split("::")[3];
			String action = row_result.split("::")[4];

			enCampMa = serviceDb.CreateEnCampMa(row_result);
			String cpid = row_result.split("::")[0];
			String cpna = row_result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "couldn't find division");

			Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(divisionName);

			String endpoint = "";
			String business = businessLogic.get("business");
			String topic_id = businessLogic.get("topic_id");
			ObjectMapper objectMapper = null;

			switch (business) {
			case "UCRM":

				if (action.equals("update")) {

					objectMapper = new ObjectMapper();
					Entity_CampMaJsonUcrm enCampMaJson1 = serviceDb.JsonCampMaUcrm(enCampMa, action);
					try {

						String jsonString = objectMapper.writeValueAsString(enCampMaJson1);
						log.info("jsonString : {}", jsonString);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, jsonString);

					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					log.info("Cpid of target record for updating : {}", cpid);
					log.info("New value of Campaign name : {}", cpna);

					serviceDb.UpdateCampMa(cpid, cpna);
					return Mono.just(ResponseEntity.ok().body(String.format("UCRM, A record with cpid : '%s' has been updated successfully", cpid)));
					
				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
					return Mono.just(ResponseEntity.ok().body(String.format("UCRM, A record with cpid : '%s' has been deleted successfully", cpid)));
				}
				
			case "Callbot":

				objectMapper = new ObjectMapper();

				Entity_CampMaJson enCampMaJson = serviceDb.JsonCampMaCallbot(enCampMa, action);

				try {

					String jsonString = objectMapper.writeValueAsString(enCampMaJson);
					log.info("jsonString : {}", jsonString);
					MessageToProducer producer = new MessageToProducer();
					endpoint = "/gcapi/post/" + topic_id;
					producer.sendMsgToProducer(endpoint, jsonString);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				// 테이블에 Update, Delete logic 추가.
				log.info(action);
				if (action.equals("update")) {

					log.info("Cpid of target record for updating : {}", cpid);
					log.info("New value of Campaign name : {}", cpna);

					serviceDb.UpdateCampMa(cpid, cpna);
					return Mono.just(ResponseEntity.ok().body(String.format("Callbot, A record with cpid : '%s' has been updated successfully", cpid)));

				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
					return Mono.just(ResponseEntity.ok().body(String.format("Callbot, A record with cpid : '%s' has been deleted successfully", cpid)));
				}

			default:

				String jsonString = serviceDb.createMaMsgApim(enCampMa, action).toString();
				log.info("jsonString : {}", jsonString);
				// localhost:8084/dspRslt
				// 192.168.219.134:8084/dspRslt
				MessageToApim apim = new MessageToApim();
				endpoint = "/cmpnMstrRegist";
				apim.sendMsgToApim(endpoint, jsonString);
				log.info("CAMPMA UPDATE로직,  APIM으로 보냄. : {}", jsonString);

				// 테이블에 Update, Delete logic 추가.
				log.info(action);
				if (action.equals("update")) {

					log.info("Cpid of target record for updating : {}", cpid);
					log.info("New value of Campaign name : {}", cpna);

					serviceDb.UpdateCampMa(cpid, cpna);
					return Mono.just(ResponseEntity.ok().body(String.format("Apim, A record with cpid : '%s' has been updated successfully", cpid)));
				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}
				return Mono.just(ResponseEntity.ok().body(String.format("Apim, A record with cpid : '%s' has been deleted successfully", cpid)));
			}
		} catch (EntityNotFoundException ex) {
			log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
			enCampMa = serviceDb.CreateEnCampMa(row_result);
			serviceDb.InsertCampMa(enCampMa);
			return Mono.just(ResponseEntity.ok("There is no record which matched with request cpid. so it just has been inserted."));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Messge : {}", e.getMessage());
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : {}", e.getMessage())));
		}
	}


	@PostMapping("/SaveRtData")
	public Mono<ResponseEntity<String>> SaveRtData(@RequestBody String msg) {

		log.info(" ");
		log.info("====== Class : ControllerCenter - Method : SaveRtData ======");

		try {

			String result = ExtrSaveRtData(msg);
			String division = result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "couldn't find division");
			log.info("DivisionName : {}", divisionName);

			switch (divisionName) {
			case "Home":
			case "Mobile":

				Entity_UcrmRt enUcrmrt = serviceDb.createUcrmRt(result);
				serviceDb.InsertUcrmRt(enUcrmrt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Ucrm data has been inserted successfully"));

			case "CallbotHome":
			case "CallbotMobile":

				Entity_CallbotRt enCallBotRt = serviceDb.createCallbotRt(result);
				serviceDb.InsertCallbotRt(enCallBotRt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Callbot data has been inserted successfully"));
			default:
				Entity_ApimRt enApimRt = serviceDb.createApimRt(result);
				serviceDb.InsertApimRt(enApimRt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Apim data has been inserted successfully"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("====== End SaveRtData ======");
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : {}", e.getMessage())));
		}

	}


	@GetMapping("/sendapimrt")
	public Mono<ResponseEntity<String>> SendApimRt() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerCenter - Method : SendApimRt ======");

			Page<Entity_ApimRt> entitylist = serviceDb.getAllApimRt();

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

					Entity_ApimRt enApimRt = entitylist.getContent().get(i);

					cpid = enApimRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					String cqsq = enApimRt.getId().getCpsq(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid) != null ? mapcontactltId.get(cpid) : "";
					divisionName = mapdivision.get(contactLtId) != null ? mapdivision.get(contactLtId) : "";

					if (contactLtId == null || contactLtId.equals("")) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("Nomatch contactId");
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];

						String division = enApimRt.getDivisionid(); // 첫번째 레코드부터 cpid를 가지고 온다.
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
					serviceDb.DelApimRtById(enApimRt.getId());
					
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

		log.info("====== End SendApimRt ======");
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
