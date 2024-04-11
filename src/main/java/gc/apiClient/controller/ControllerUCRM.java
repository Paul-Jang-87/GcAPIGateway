package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import reactor.core.scheduler.Schedulers;

import org.json.JSONArray;
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
import gc.apiClient.entity.Entity_ContactltMapper;
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
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageTo360View;
import gc.apiClient.messages.MessageToApim;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import gc.apiClient.service.ServicePostgre;
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

	@Scheduled(fixedRate = 60000)
	public void scheduledMethod() {

		Mono.fromCallable(() -> ReceiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();

//		Mono.fromCallable(() -> Msg360Datacall())
//        .subscribeOn(Schedulers.boundedElastic())
//        .subscribe();
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
		String cpid = "";
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

						Entity_CampMa enCampMa = serviceDb.createCampMaMsg(row_result, "insert");

						switch (business) {
						case "UCRM":
						case "Callbot":

							objectMapper = new ObjectMapper();
							Entity_CampMaJson enCampMaJson = serviceDb.createCampMaJson(enCampMa, "insert");
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

							objectMapper = new ObjectMapper();

							try {
								String jsonString = objectMapper.writeValueAsString(enCampMa);

								// localhost:8084/dspRslt
								// 192.168.219.134:8084/dspRslt
								MessageToApim apim = new MessageToApim();
								endpoint = "/cmpnMstrRegist";
//							apim.sendMsgToApim(endpoint, jsonString);
								log.info("CAMPMA 로직, APIM으로 보냄. : {}", jsonString);

							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}

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
	public Mono<Void> UpdateOrDelCampMa(@RequestBody String msg) {

		try {
			log.info("Class : ControllerUCRM - Method : UpdateOrDelCampMa");
			String row_result = ExtractCampMaUpdateOrDel(msg); // cpid::coid::cpna::divisionid::action
			String division = row_result.split("::")[3];
			String action = row_result.split("::")[4];

			Entity_CampMa enCampMa = serviceDb.createCampMaMsg(row_result, action);
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
			case "Callbot":

				objectMapper = new ObjectMapper();
				Entity_CampMaJson enCampMaJson = serviceDb.createCampMaJson(enCampMa, action);
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

					log.info("cpid of target record for updating : {}", cpid);
					log.info("new value of Campaign name : {}", cpna);

					serviceDb.UpdateCampMa(cpid, cpna);

				} else {
					log.info("cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}

				break;

			default:

				objectMapper = new ObjectMapper();

				try {
					String jsonString = objectMapper.writeValueAsString(enCampMa);

					// localhost:8084/dspRslt
					// 192.168.219.134:8084/dspRslt
					MessageToApim apim = new MessageToApim();
					endpoint = "/cmpnMstrRegist";
//					apim.sendMsgToApim(endpoint, jsonString);
					log.info("CAMPMA UPDATE로직,  APIM으로 보냄. : {}", jsonString);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				break;
			}
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

				serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
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
			} catch (DataIntegrityViolationException ex) {
				log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
			} catch (DataAccessException ex) {
				log.error("DataAccessException 발생 : {}", ex.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@PostMapping("/testucrm")
	public Mono<ResponseEntity<String>> testUcrmMsgFrmCnsmer(@RequestBody String msg) {

		try {
			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : testUcrmMsgFrmCnsmer ======");

			List<Entity_Ucrm> entitylist = serviceDb.getAll();
			log.info("All records from DB : {}", entitylist.toString());
			int reps = entitylist.size(); // 반복 횟수 저장.
			log.info("number of records : {}", reps);
			log.info("{}만큼 반복,", reps);

			List<String> arr = new ArrayList<String>();

			Map<String, String> mapcontactltId = new HashMap<String, String>();
			Map<String, String> mapquetId = new HashMap<String, String>();

			for (int i = 0; i < reps; i++) {

				Entity_ContactLt enContactLt = serviceDb.createContactUcrm(entitylist.get(i));
				log.info("{}번째 레코드 : {},", i, entitylist.get(i).toString());

				String cpid = entitylist.get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
				log.info("cpid : {}", cpid);

				String contactLtId = mapcontactltId.get(cpid);
				String queid = mapquetId.get(cpid);
				log.info("contactLtId : {}", contactLtId);
				log.info("queid : {}", queid);

				if (contactLtId.equals("") || contactLtId == null) {// cpid를 조회 했는데 그것에 대응하는 contactltId가 없다면,
					log.info("nomatch contactId");
					String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
					String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
					contactLtId = res.split("::")[0];
					queid = res.split("::")[1];

					log.info("contactLtId : {}", contactLtId);
					log.info("queid : {}", queid);

					mapcontactltId.put(cpid, contactLtId);
					mapquetId.put(cpid, res.split("::")[1]);
				}

				String row_result = ExtractRawUcrm(entitylist.get(i));
				row_result = row_result + "::" + contactLtId + "::" + queid;
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

				try {
					serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
					serviceWeb.PostContactLtApiRequet("contact", contactLtId, arr);
				} catch (Exception e) {
					log.error("Error Message", e.getMessage());
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@PostMapping("/contactltucrm/{topic}")
	public Mono<ResponseEntity<String>> UcrmMsgFrmCnsmer(@PathVariable("topic") String tranId,
			@RequestBody String msg) {

		log.info(" ");
		log.info("====== Class : ControllerUCRM - Method : UcrmMsgFrmCnsmer ======");
		String row_result = "";
		String result = "";
		String cpid = "";
		String topic_id = tranId;
		List<String> arr = new ArrayList<String>();

		log.info("topic_id : {}", topic_id);

		switch (topic_id) {

		case "ucrmhome":// IF-CRM_003
		case "ucrmmobile":// IF-CRM_004

			try {
				row_result = ExtractValUcrm(msg);

				Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
				// Entity_ContactLt 객체에 매핑시킨다.
				cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.

				result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);// 캠페인 아이디로
//																				// "/api/v2/outbound/campaigns/{campaignId}"호출
//																				// 후 결과 가져온다.

				String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid,queueid만 추출.
				String contactLtId = res.split("::")[0];
//				// "api/v2/outbound/contactlists/{contactListId}/contacts"로 request body값 보내기 위한
//				// 객체
//				// 객체 안의 속성들(키)은 변동 될 수 있음.

				row_result = row_result + "::" + res;
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

				try {
					serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
					serviceWeb.PostContactLtApiRequet("contact", contactLtId, arr);
				} catch (Exception e) {
					log.error("Error Message", e.getMessage());
					e.printStackTrace();
				}

				return Mono.just(ResponseEntity.ok("Successfully processed the message."));
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error Message : {}", e.getMessage());
			}

		default:
			log.info("====== End UcrmMsgFrmCnsmer ======");
			return Mono.just(ResponseEntity.badRequest().body("Invalid topic_id provided."));
		}
	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<ResponseEntity<String>> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		try {

			log.info(" ");
			log.info("====== Class : ControllerUCRM - Method : receiveMessage ======");
			String row_result = "";
			String result = "";
			String cpid = "";
			String topic_id = tranId;
			String division = "";
			String business = "";
			String endpoint = "/gcapi/post/" + topic_id;
			ObjectMapper objectMapper = null;

			log.info("topic_id : {}", topic_id);

			switch (topic_id) {

			case "ucrm":
			case "callbot":

				if (topic_id.equals("callbot")) {
//					row_result = ExtractValCallBot(msg); // 뽑아온다.cpid::cpsq::cske::csna::tkda::flag
				} else {
					row_result = ExtractValUcrm(msg);
				}

				Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
				// Entity_ContactLt 객체에 매핑시킨다.
				cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.

				result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);// 캠페인 아이디로
//																				// "/api/v2/outbound/campaigns/{campaignId}"호출
//																				// 후 결과 가져온다.
				//
				String res = ExtractContactLtId(result); // 가져온 결과에서 contactlistid만 추출.
				String contactLtId = res.split("::")[0];
//				// "api/v2/outbound/contactlists/{contactListId}/contacts"로 request body값 보내기 위한
//				// 객체
//				// 객체 안의 속성들(키)은 변동 될 수 있음.
				row_result = row_result + "::" + res;
				String contactltMapper = serviceDb.createContactLtGC(row_result);

				objectMapper = new ObjectMapper();

				try {
					String jsonString = objectMapper.writeValueAsString(contactltMapper); // 매핑한 객체를 jsonString으로 변환.
					log.info("JsonString Data : {}", jsonString);

					// "api/v2/outbound/contactlists/{contactListId}/contacts"로 보냄.
					// 첫번째 인자 : 어떤 api를 호출 할 건지 지정.
					// 두번째 인자 : path parameter
					// 세번째 인자 : request body.

					serviceWeb.PostContactLtClearReq("contactltclear", contactLtId);
//					serviceWeb.PostContactLtApiRequet("contact", contactLtId, jsonString);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				// db인서트
				try {
					serviceDb.InsertContactLt(enContactLt);

				} catch (DataIntegrityViolationException ex) {
					log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
				} catch (DataAccessException ex) {
					log.error("DataAccessException 발생 : {}", ex.getMessage());
				}

				return Mono.empty();

			case "camprtMsg":// "from_clcc_campnrs_h_message" , "from_clcc_campnrs_m_message"

				result = ExtractVal56(msg);// request body로 들어온 json에서 필요 데이터 추출
				log.info("result : {}", result);

				String parts[] = result.split("::");

				int dirt = 0;
				cpid = parts[0];
				contactLtId = parts[1];
				division = parts[2];

				log.info("cpid : {}", cpid);
				log.info("contactLtId : {}", contactLtId);
				log.info("Division Info : {}", division);

				// appliction.properties 파일에서 division와 매치되는 divisionName을 가지고 옴.
				Map<String, String> properties = customProperties.getDivision();
				String divisionName = properties.getOrDefault(division, "couldn't find division");
				log.info("DivisionName : {}", divisionName);

				// contactlt테이블에서 cpid가 같은 모든 레코드들을 엔티티 오브젝트로 리스트 형태로 가지고 온다.
				List<Entity_ContactLt> enContactList = new ArrayList<Entity_ContactLt>();
				enContactList = serviceDb.findContactLtByCpid(cpid);

				// 가지고 온 모든 엔티티들의 숫자만큼 for문들 돌면서 레코드들 각각의 cske(고객키)들을 가지고 온다. 그리고 values리스트에
				// 담는다.
				List<String> values = new ArrayList<String>();
				for (int i = 0; i < enContactList.size(); i++) {
					values.add(enContactList.get(i).getCske());
				}

				log.info("고객키들 (cske) : {} ", values.toString());
				log.info("enContactList size : {}", enContactList);

				// contactLtId를 키로 하여 제네시스의 api를 호출한다. 호출할 때는 values리스트 담겨져 있던 cske(고객키)들 각각에 맞는
				// 결과 값들을
				// jsonString문자열로 한꺼번에 받는다.
				result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);

				// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
				// 왜냐면 나머지는 똑같을테니.
				String contactsresult = ExtractContacts56(result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
				Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// contactsresult값으로 entity하나를 만든다.
				Character tkda = entityCmRt.getTkda().charAt(0);// 그리고 비즈니스 로직을 구분하게 해줄 수 있는 토큰데이터를 구해온다.

				// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
				Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(tkda, divisionName);
				business = businessLogic.get("business");
				topic_id = businessLogic.get("topic_id");

				switch (business) {
				case "UCRM": // UCRM일 경우.
				case "CALLBOT": // 콜봇일 경우.

					for (int i = 0; i < enContactList.size(); i++) {

						contactsresult = ExtractContacts56(result, i);
						if (contactsresult.equals("")) {
							log.info("No value, skip to next");
							continue;
						}

						entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

						dirt = entityCmRt.getDirt();// 응답코드

						if ((business.equals("UCRM")) && (dirt == 1)) {// URM이면서 정상일 때.

						} else {
							JSONObject toproducer = serviceDb.createCampRtJson(entityCmRt, business);// producer로 보내기
																										// 위한
							// entity.
							objectMapper = new ObjectMapper();

							try {
								String jsonString = toproducer.toString();
								log.info("JsonString Data : {}번째 {}", i, jsonString);

								MessageToProducer producer = new MessageToProducer();
								endpoint = "/gcapi/post/" + topic_id;
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

					break;

				default:

					for (int i = 0; i < enContactList.size(); i++) {

						contactsresult = ExtractContacts56(result, i);
						contactsresult = contactsresult + "::" + cpid;// contactid(고객키)::contactListId::didt::dirt::cpid
						entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

						dirt = entityCmRt.getDirt();// 응답코드
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
						endpoint = "/dspRslt";
//						apim.sendMsgToApim(endpoint, jsonString);
						apim.sendMsgToApim(endpoint, apimEntitylt);
						log.info("CAMPRT 로직, APIM으로 보냄. : {} ", jsonString);

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}

				return Mono.empty();

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {} ", e.getMessage());

		}

		return Mono.just(ResponseEntity.ok("'receiveMessage' got message successfully."));
	}

	@GetMapping("/360view1")
	public Mono<ResponseEntity<String>> Msg360Datacall() {

		try {

			String topic_id = "from_clcc_hmcepcalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
				// 2. crud 구분해서 메시지 키를 정한다.
				// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCall> entitylist = serviceOracle.getAll(Entity_DataCall.class);

				log.info("entitylist : {}", entitylist.toString());
				log.info("entitylist size : {}", entitylist.size());

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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
			log.info("the number of records : {}", numberOfRecords);

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

}
