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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.BusinessLogic;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.Entity_CampMaJsonUcrm;
import gc.apiClient.entity.Entity_ToApim;
import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_DataCallCustomer;
import gc.apiClient.entity.oracleH.Entity_DataCallService;
import gc.apiClient.entity.oracleH.Entity_MasterServiceCode;
import gc.apiClient.entity.oracleH.Entity_WaDataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;
import gc.apiClient.entity.oracleH.Entity_WaDataCallTrace;
import gc.apiClient.entity.oracleH.Entity_WaMTracecode;
import gc.apiClient.entity.oracleM.Entity_MDataCall;
import gc.apiClient.entity.oracleM.Entity_MDataCallCustomer;
import gc.apiClient.entity.oracleM.Entity_MDataCallService;
import gc.apiClient.entity.oracleM.Entity_MMasterServiceCode;
import gc.apiClient.entity.oracleM.Entity_MWaDataCall;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallTrace;
import gc.apiClient.entity.oracleM.Entity_MWaMTracecode;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageTo360View;
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
public class ControllerUCRM extends ServiceJson {

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceDBOracle serviceOracle;
	private final InterfaceMsgObjOrcl serviceMsgObjOrcl;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb, InterfaceDBOracle serviceOracle,
			InterfaceWebClient serviceWeb, CustomProperties customProperties, InterfaceMsgObjOrcl serviceMsgObjOrcl) {
		this.serviceDb = serviceDb;
		this.serviceOracle = serviceOracle;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
		this.serviceMsgObjOrcl = serviceMsgObjOrcl;
	}

	@Scheduled(fixedRate = 86400 * 1000)
	public void RefreshToken() {
		WebClientApp.EmptyTockenlt();
	}

	@Scheduled(fixedRate = 5000)
	public void UcrmContactlt() {
//		Mono.fromCallable(() -> UcrmMsgFrmCnsmer()).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@Scheduled(fixedRate = 60000)
	public void scheduledMethod() {

//		Mono.fromCallable(() -> ReceiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();

//		Mono.fromCallable(() -> Msg360Datacall())
//		.subscribeOn(Schedulers.boundedElastic())
//		.subscribe();
//
//		Mono.fromCallable(() -> Msg360DataCallCustomer())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360DataCallService())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MDatacall())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MDataCallCustomer())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MDataCallService())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MMstrsSvcCd())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MstrsSvcCd())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MWaDataCall())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MWaDataCallOptional())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MWaDataCallTrace())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360MWaMTrCode())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360WaDataCall())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360WaDataCallOptional())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360WaDataCallTrace())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
//		
//		Mono.fromCallable(() -> Msg360WaMTrCode())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();

	}

	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> ReceiveMessage(@PathVariable("topic") String tranId) {

		log.info(" ");
		log.info("====== Class : ControllerUCRM - Method : ReceiveMessage ======");

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
	public Mono<Void> UpdateOrDelCampMa(@RequestBody String msg, HttpServletRequest request) throws Exception {

		String row_result = "";
		Entity_CampMa enCampMa = null;

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : UpdateOrDelCampMa ======");

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
				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}

				break;
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

				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}

				break;

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

				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}
				break;
			}
		} catch (EntityNotFoundException ex) {
			log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
			enCampMa = serviceDb.CreateEnCampMa(row_result);
			serviceDb.InsertCampMa(enCampMa);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Messge : {}", e.getMessage());
		}

		return Mono.empty();
	}

	@PostMapping("/contactlt/{topic}")
	public Mono<ResponseEntity<String>> CallbotMsgFrmCnsumer(@PathVariable("topic") String tranId,
			@RequestBody String msg) {

		log.info(" ");
		log.info("====== Class : ControllerUCRM - Method : CallbotMsgFrmCnsumer ======");

		String jsonResponse = msg;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int casenum = 0;

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			casenum = jsonNode.path("cmpnItemDto").size();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		int cntofmsg = casenum;
		log.info("case count : {}", cntofmsg);

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

				row_result = ExtractValCallBot(msg, 0); // 뽑아온다.cpid::cpsq::cske::csno::tkda::flag

				Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
				// Entity_ContactLt 객체에 매핑시킨다.
				cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.

				result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);// 캠페인 아이디로
//																					// "/api/v2/outbound/campaigns/{campaignId}"호출
//																					// 후 결과 가져온다.
				res = ExtractContactLtId(result);
				contactLtId = res.split("::")[0];

				for (int i = 0; i < cntofmsg; i++) {

					log.info("res{} : {}", i, res);
					log.info("받아온 리스트 안의 {}번째 메시지", i);

					row_result = ExtractValCallBot(msg, i); // 뽑아온다.cpid::cpsq::cske::csno::tkda::flag

					enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
					// Entity_ContactLt 객체에 매핑시킨다.
					row_result = row_result + "::" + res; // 뽑아온다.cpid::cpsq::cske::csna::tkda::flag::contactLtId
					String contactltMapper = serviceDb.createContactLtGC(row_result);

					arr.add(contactltMapper);

					// db인서트
					try {
						serviceDb.InsertContactLt(enContactLt);

					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}
				}

//				serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
				serviceWeb.PostContactLtApiRequet("contact", contactLtId, arr);

				log.info("====== End CallbotMsgFrmCnsumer ======");

				return Mono.just(ResponseEntity.ok("Successfully processed the message."));

			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error Message : {}", e.getMessage());
			}

		default:

			log.info("====== End CallbotMsgFrmCnsumer ======");
			return Mono.just(ResponseEntity.badRequest().body("Invalid topic_id provided."));
		}
	}

	@PostMapping("/saveucrmdata")
	public Mono<ResponseEntity<String>> SaveUcrmData(@RequestBody String msg) {

		try {

			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SaveUcrmData ======");
			Entity_Ucrm enUcrm = serviceDb.createUcrm(msg);

			try {
				serviceDb.InsertUcrm(enUcrm);
//				log.info("Saved Message : {}",msg);
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		log.info("====== End SaveUcrmData ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@PostMapping("/SaveRtData")
	public Mono<ResponseEntity<String>> SaveRtData(@RequestBody String msg) {

		log.info(" ");
		log.info("====== Class : ControllerUCRM - Method : SaveRtData ======");

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

				break;

			case "CallbotHome":
			case "CallbotMobile":

				Entity_CallbotRt enCallBotRt = serviceDb.createCallbotRt(result);
				serviceDb.InsertCallbotRt(enCallBotRt);

				break;
			default:
				Entity_ApimRt enApimRt = serviceDb.createApimRt(result);
				serviceDb.InsertApimRt(enApimRt);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		log.info("====== End SaveRtData ======");
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
				String contactLtId = "";

				for (int i = 0; i < reps; i++) {

					Entity_ContactLt enContactLt = serviceDb.createContactUcrm(entitylist.getContent().get(i));

					String cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid);
					String queid = mapquetId.get(cpid);

					if (contactLtId == null || contactLtId.equals("")) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("Nomatch contactId");
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];
						queid = res.split("::")[1];

						mapcontactltId.put(cpid, contactLtId);
						mapquetId.put(cpid, res.split("::")[1]);
					} else {
						log.info("Matched contactId");
					}

					String row_result = ExtractRawUcrm(entitylist.getContent().get(i));
					row_result = row_result + "::" + contactLtId + "::" + queid;
					String contactltMapper = serviceDb.createContactLtGC(row_result);

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}
					contactlists.get(contactLtId).add(contactltMapper);

					log.info("Add value into Arraylist named '{}'", contactLtId);

					// db인서트
					try {
						serviceDb.InsertContactLt(enContactLt);

					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}

					try {
					} catch (Exception e) {
						log.error("Error Message", e.getMessage());
						e.printStackTrace();
					}

					serviceDb.DelUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());

				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					log.info("Now the size of Arraylist '{}': {}", entry.getKey(), entry.getValue().size());
//					serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
					serviceWeb.PostContactLtApiRequet("contact", entry.getKey(), entry.getValue());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@GetMapping("/sendapimrt")
	public Mono<ResponseEntity<String>> SendApimRt() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SendApimRt ======");

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
	
	@GetMapping("/sendcallbotrt")
	public Mono<ResponseEntity<String>> SendCallBotRt() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : SendCallBotRt ======");

			Page<Entity_CallbotRt> entitylist = serviceDb.getAllCallBotRt();

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

					Entity_CallbotRt enCallbotRt = entitylist.getContent().get(i);

					cpid = enCallbotRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					String cqsq = enCallbotRt.getId().getCpsq(); // 첫번째 레코드부터 cpid를 가지고 온다.

					contactLtId = mapcontactltId.get(cpid) != null ? mapcontactltId.get(cpid) : "";
					divisionName = mapdivision.get(contactLtId) != null ? mapdivision.get(contactLtId) : "";

					if (contactLtId == null || contactLtId.equals("")) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("Nomatch contactId");
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
						contactLtId = res.split("::")[0];

						String division = enCallbotRt.getDivisionid();
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
					serviceDb.DelCallBotRtById(enCallbotRt.getId());
					
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

		log.info("====== End SendCallBotRt ======");
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

	
//	@PostMapping("/gcapi/post/{topic}")
//	public Mono<ResponseEntity<String>> ReturnCallResult(@PathVariable("topic") String tranId, @RequestBody String msg,
//			HttpServletRequest request) {
//
//		try {
//
//			log.info(" ");
//			log.info("====== Class : ControllerUCRM - Method : ReturnCallResult ======");
//
//			String ipAddress = request.getRemoteAddr();
//			int port = request.getRemotePort();
//			log.info("Request received from IP address and Port => {}:{}", ipAddress, port);
//
//			String result = "";
//			String cpid = "";
//			String topic_id = tranId;
//			String division = "";
//			String business = "";
//			String endpoint = "/gcapi/post/" + topic_id;
//
//			log.info("topic_id : {}", topic_id);
//
//			switch (topic_id) {
//
//			case "camprtMsg":// "from_clcc_campnrs_h_message" , "from_clcc_campnrs_m_message"
//
//				result = ExtractVal56(msg);// request body로 들어온 json에서 필요 데이터 추출
//				log.info("result : {}", result);
//
//				String parts[] = result.split("::");
//
//				int dirt = 0;
//				cpid = parts[0];
//				String contactLtId = parts[1];
//				division = parts[2];
//
//				log.info("cpid : {}", cpid);
//				log.info("contactLtId : {}", contactLtId);
//				log.info("Division Info : {}", division);
//
//				// appliction.properties 파일에서 division와 매치되는 divisionName을 가지고 옴.
//				Map<String, String> properties = customProperties.getDivision();
//				String divisionName = properties.getOrDefault(division, "couldn't find division");
//				log.info("DivisionName : {}", divisionName);
//
//				// contactlt테이블에서 cpid가 같은 모든 레코드들을 엔티티 오브젝트로 리스트 형태로 가지고 온다.
//				List<Entity_ContactLt> enContactList = new ArrayList<Entity_ContactLt>();
//				enContactList = serviceDb.findContactLtByCpid(cpid);
//				log.info("Total number of All Entities : {}", enContactList.size());
//				// 가지고 온 모든 엔티티들의 숫자만큼 for문들 돌면서 레코드들 각각의 cske(고객키)들을 가지고 온다. 그리고 values리스트에
//				// 담는다.
//
//				List<String> values = new ArrayList<String>();
//				for (int k = 0; k < enContactList.size(); k++) {
//					values.add(Integer.toString(enContactList.get(k).getId().getCpsq()));
//
//					if (values.size() >= 50) {
//
//						Roop(result, contactLtId, values, divisionName, business, topic_id, dirt, endpoint, cpid);
//
//					}
//				}
//
//				Roop(result, contactLtId, values, divisionName, business, topic_id, dirt, endpoint, cpid);
//
//				return Mono.empty();
//
//			default:
//				break;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Error Message : {} ", e.getMessage());
//
//		}
//
//		return Mono.just(ResponseEntity.ok("'ReturnCallResult' got message successfully."));
//	}

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

//	public Mono<Void> Roop(String result, String contactLtId, List<String> values, String divisionName, String business,
//			String topic_id, int dirt, String endpoint, String cpid
//
//	) throws Exception {
//
//		ObjectMapper objectMapper = null;
//		result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);
//
//		if (result.equals("[]")) {
//			log.info("No result, skip to next");
//			values.clear();
//			return Mono.empty();
//		}
//
//		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
//		// 왜냐면 나머지는 똑같을테니.
//		String contactsresult = ExtractContacts56(result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로
//																// 넣는다.
//		Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// contactsresult값으로
//																				// entity하나를 만든다.
//		Character tkda = entityCmRt.getTkda().charAt(0);// 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.
//
//		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
//		Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(tkda, divisionName);
//		business = businessLogic.get("business");
//		topic_id = businessLogic.get("topic_id");
//
//		switch (business) {
//		case "UCRM": // UCRM일 경우.
//		case "CALLBOT": // 콜봇일 경우.
//
//			for (int i = 0; i < values.size(); i++) {
//
//				contactsresult = ExtractContacts56(result, i);
//				if (contactsresult.equals("")) {
//					log.info("No value, skip to next");
//					continue;
//				}
//
//				entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.
//
//				dirt = entityCmRt.getDirt();// 응답코드
//
//				if ((business.equals("UCRM")) && (dirt == 1)) {// URM이면서 정상일 때.
//
//				} else {
//					JSONObject toproducer = serviceDb.createCampRtJson(entityCmRt, business);// producer로
//																								// 보내기
//																								// 위한
//					// entity.
//					objectMapper = new ObjectMapper();
//
//					try {
//						String jsonString = toproducer.toString();
//						log.info("JsonString Data : {}번째 {}", i, jsonString);
//
//						MessageToProducer producer = new MessageToProducer();
//						endpoint = "/gcapi/post/" + topic_id;
//						producer.sendMsgToProducer(endpoint, jsonString);
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//				// db인서트
//				try {
//					serviceDb.InsertCampRt(entityCmRt);
//				} catch (DataIntegrityViolationException ex) {
//					log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
//				} catch (DataAccessException ex) {
//					log.error("DataAccessException 발생 : {}", ex.getMessage());
//				}
//			}
//			values.clear();
//			return Mono.empty();
//
//		default:
//
//			for (int i = 0; i < values.size(); i++) {
//
//				contactsresult = ExtractContacts56(result, i);
//				contactsresult = contactsresult + "::" + cpid;// contactid(고객키)::contactListId::didt::dirt::cpid
//				entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.
//
//				dirt = entityCmRt.getDirt();// 응답코드
//				String tokendata = entityCmRt.getTkda();// 토큰데이터
//
//				Entity_ToApim enToApim = new Entity_ToApim();
//				enToApim.setDirt(dirt);
//				enToApim.setTkda(tokendata);
//
//				apimEntitylt.add(enToApim);
//			}
//
//			objectMapper = new ObjectMapper();
//
//			try {
//				String jsonString = objectMapper.writeValueAsString(apimEntitylt);
//
//				// localhost:8084/dspRslt
//				// 192.168.219.134:8084/dspRslt
//				MessageToApim apim = new MessageToApim();
//				endpoint = "/dspRslt";
//				apim.sendMsgToApim(endpoint, jsonString);
//				log.info("CAMPRT 로직, APIM으로 보냄. : {} ", jsonString);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			values.clear();
//			return Mono.empty();
//		}
//
//	}

	@GetMapping("/360view1")
	public Mono<ResponseEntity<String>> Msg360Datacall() {

		try {

			String topic_id = "from_clcc_hmcepcalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(DataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
				// 2. crud 구분해서 메시지 키를 정한다.
				// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCall> entitylist = serviceOracle.getAll(Entity_DataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_DataCall.class, orderid);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

		}
		return Mono.just(ResponseEntity.ok("'Msg360Datacall' got message successfully."));
	}

	@GetMapping("/360view2")
	public Mono<ResponseEntity<String>> Msg360MDatacall() {

		try {
			String topic_id = "from_clcc_mblcepcalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCall> entitylist = serviceOracle.getAll(Entity_MDataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCall.class, orderid);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MDatacall' got message successfully."));
	}

	@GetMapping("/360view3")

	public Mono<ResponseEntity<String>> Msg360DataCallCustomer() {

		try {
			String topic_id = "from_clcc_hmcepcalldtcust_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(DataCallCustomer) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCallCustomer> entitylist = serviceOracle.getAll(Entity_DataCallCustomer.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallCustomerMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_DataCallCustomer.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360DataCallCustomer' got message successfully."));
	}

	@GetMapping("/360view4")
	public Mono<ResponseEntity<String>> Msg360MDataCallCustomer() {

		try {

			String topic_id = "from_clcc_mblcepcalldtcust_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCallCustomer) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCallCustomer> entitylist = serviceOracle.getAll(Entity_MDataCallCustomer.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallCustomerMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCallCustomer.class, orderid);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MDataCallCustomer' got message successfully."));
	}

	@GetMapping("/360view5")
	public Mono<ResponseEntity<String>> Msg360DataCallService() {

		try {
			String topic_id = "from_clcc_hmcepcallsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(DataCallService) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCallService> entitylist = serviceOracle.getAll(Entity_DataCallService.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallService(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_DataCallService.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360DataCallService' got message successfully."));
	}

	@GetMapping("/360view6")
	public Mono<ResponseEntity<String>> Msg360MDataCallService() {

		try {
			String topic_id = "from_clcc_mblcepcallsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCallService) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCallService> entitylist = serviceOracle.getAll(Entity_MDataCallService.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallService(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCallService.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MDataCallService' got message successfully."));
	}

	@GetMapping("/360view7")
	public Mono<ResponseEntity<String>> Msg360MstrsSvcCd() {
		try {
			String topic_id = "from_clcc_hmcepcallmstrsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MasterServiceCode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MasterServiceCode> entitylist = serviceOracle.getAll(Entity_MasterServiceCode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.MstrSvcCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MasterServiceCode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MstrsSvcCd' got message successfully."));
	}

	@GetMapping("/360view8")
	public Mono<ResponseEntity<String>> Msg360MMstrsSvcCd() {

		try {
			String topic_id = "from_clcc_mblcepcallmstrsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MMasterServiceCode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MMasterServiceCode> entitylist = serviceOracle.getAll(Entity_MMasterServiceCode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.MstrSvcCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MMasterServiceCode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MMstrsSvcCd' got message successfully."));
	}

	@GetMapping("/360view9")
	public Mono<ResponseEntity<String>> Msg360WaDataCall() {

		try {
			String topic_id = "from_clcc_hmcepwacalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCall> entitylist = serviceOracle.getAll(Entity_WaDataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaDataCall.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaDataCall' got message successfully."));
	}

	@GetMapping("/360view10")
	public Mono<ResponseEntity<String>> Msg360MWaDataCall() {

		try {
			String topic_id = "from_clcc_mblcepwacalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCall> entitylist = serviceOracle.getAll(Entity_MWaDataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCall.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCall' got message successfully."));
	}

	@GetMapping("/360view11")
	public Mono<ResponseEntity<String>> Msg360WaDataCallOptional() {

		try {
			String topic_id = "from_clcc_hmcepwacallopt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCallOptional) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCallOptional> entitylist = serviceOracle.getAll(Entity_WaDataCallOptional.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallOptionalMsg(entitylist.get(i), crudtype));

					serviceOracle.deleteAll(Entity_WaDataCallOptional.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaDataCallOptional' got message successfully."));
	}

	@GetMapping("/360view12")
	public Mono<ResponseEntity<String>> Msg360MWaDataCallOptional() {

		try {
			String topic_id = "from_clcc_mblcepwacallopt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCallOptional) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCallOptional> entitylist = serviceOracle.getAll(Entity_MWaDataCallOptional.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallOptionalMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCallOptional.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCallOptional' got message successfully."));
	}

	@GetMapping("/360view13")
	public Mono<ResponseEntity<String>> Msg360WaDataCallTrace() {

		try {

			String topic_id = "from_clcc_hmcepwacalltr_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCallTrace) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCallTrace> entitylist = serviceOracle.getAll(Entity_WaDataCallTrace.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallTraceMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaDataCallTrace.class, orderid);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360WaDataCallTrace' got message successfully."));
	}

	@GetMapping("/360view14")
	public Mono<ResponseEntity<String>> Msg360MWaDataCallTrace() {

		try {
			String topic_id = "from_clcc_mblcepwacalltr_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCallTrace) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCallTrace> entitylist = serviceOracle.getAll(Entity_MWaDataCallTrace.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallTraceMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCallTrace.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCallTrace' got message successfully."));
	}

	@GetMapping("/360view15")
	public Mono<ResponseEntity<String>> Msg360WaMTrCode() {

		try {
			String topic_id = "from_clcc_hmcepwatrcd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaMTracecode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaMTracecode> entitylist = serviceOracle.getAll(Entity_WaMTracecode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaMTraceCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaMTracecode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaMTrCode' got message successfully."));
	}

	@GetMapping("/360view16")
	public Mono<ResponseEntity<String>> Msg360MWaMTrCode() {

		try {
			String topic_id = "from_clcc_mblcepwatrcd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaMTracecode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaMTracecode> entitylist = serviceOracle.getAll(Entity_MWaMTracecode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaMTraceCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaMTracecode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MWaMTrCode' got message successfully."));
	}

	@GetMapping("/gethc")
	public String gealthCheck() throws Exception {
		return "TEST RESPONSE";
	}

}
