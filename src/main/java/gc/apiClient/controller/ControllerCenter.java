package gc.apiClient.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import reactor.core.scheduler.Schedulers;
import org.springframework.data.domain.Page;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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

@RestController
@Slf4j
public class ControllerCenter {                       

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerCenter(InterfaceDBPostgreSQL serviceDb, InterfaceWebClient serviceWeb,
			CustomProperties customProperties) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
	}

	@Scheduled(fixedRate = 86400 * 1000) // 24시간 마다 토큰 초기화.
	public void RefreshToken() {

		WebClientApp.EmptyTockenlt();

	}

//	@Scheduled(fixedRate = 60000)
//	public void scheduledMethod() {// 1분 간격으로 안의 함수들 비동기적으로 실행
//
//		Mono.fromCallable(() -> ReceiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();
//		Mono.fromCallable(() -> SendApimRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();
//
//	}

	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> ReceiveMessage(@PathVariable("topic") String tranId) {

		log.info(" ");
		log.info("====== Class : ControllerCenter - Method : ReceiveMessage ======");

		String result = "";
		String topic_id = tranId;
		int size = 0;
		int numberOfRecords = 0;

		switch (topic_id) {

		case "campma":

			try {

				result = serviceWeb.GetApiRequet("campaignId",1);// 제네시스 api 호출. 'campaignId'는 'WebClientApp'클래스에 미리 정의 해둔
															   // endpoint.

				size = ServiceJson.extractIntVal("CampaignListSize", result);// G.C에서 불러온 캠페인 개수.

				numberOfRecords = serviceDb.getRecordCount(); // 현재 레코드 갯수.

				if (size == numberOfRecords) {// 조회된 캠페인의 개수와 현재 db에 저장된 캠페인 정보의 숫자가 동일하다. 즉, 새로 생성된 캠페인이 없다.

				} else {// 조회된 캠페인의 개수와 현재 db에 저장된 캠페인 정보의 숫자가 다르다. 즉, api 호출로 캠페인들을 조회 해본 결과 새로 생성된
						// 캠페인이 있다.

					int reps = size - numberOfRecords;// 몇 개의 캠페인이 새로 생성됐는지.
					log.info("{}번 반복", reps);
					
					if(reps > 100) {
						
						int page = 1; 
						handlingCampMaster(100, result);
						reps = reps - 100;
						while( ( reps/100 )!=0 ) {
							++page;
							result = serviceWeb.GetApiRequet("campaignId",page);
							handlingCampMaster(100, result);
							reps = reps - 100;
						}
						++page;
						result = serviceWeb.GetApiRequet("campaignId",page);
						reps = reps % 100;
						handlingCampMaster(reps, result);
						
					}else {
						handlingCampMaster(reps, result);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("에러 메시지 : {}", e.getMessage());
			}
		}

		return Mono.empty();
	}

	@PostMapping("/updateOrDelCampma")//이 api는 제네시스 이벤트 브릿지를 통해 update, delete 이벤트 발생 시 불려진다. 
	public Mono<ResponseEntity<String>> UpdateOrDelCampMa(@RequestBody String msg, HttpServletRequest request)
			throws Exception {

		String row_result = "";
		Entity_CampMa enCampMa = null;

		try {
			log.info(" ");
			log.info("====== Class : ControllerCenter - Method : UpdateOrDelCampMa ======");

			String ipAddress = request.getRemoteAddr();
			int port = request.getRemotePort();
			log.info("IP주소 : {}, 포트 : {}로 부터 api가 호출되었습니다.", ipAddress, port);//어디서 이 api를 불렀는지 ip와 port 번호를 찍어본다. 

			row_result = ServiceJson.extractStrVal("ExtractCampMaUpdateOrDel", msg); // cpid::coid::cpna::divisionid::action 이 String을 결과 값으로 받는다. 
			String division = row_result.split("::")[3];
			String action = row_result.split("::")[4];

			enCampMa = serviceDb.CreateEnCampMa(row_result);
			String cpid = row_result.split("::")[0];
			String cpna = row_result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");//src/main/resources 경로의 application.properties 참조, division 아이디를 키로하여 값 조회.

			Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(divisionName);

			String endpoint = "";
			String business = businessLogic.get("business");
			String topic_id = businessLogic.get("topic_id");

			switch (business) {
			case "UCRM":

				if (action.equals("update")) {
					
					MsgUcrm msgucrm = new MsgUcrm();
					msg = msgucrm.maMassage(enCampMa, action);

					log.info("업데이트를 위한 변경할 대상 cpid : {}", cpid);
					log.info("새로운 캠페인명 : {}", cpna);

					try {

						serviceDb.UpdateCampMa(cpid, cpna);//캠페인 아이디를 기준으로 해당 레코드의 캠페인명 업데이트

						log.info("jsonString : {}", msg);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg); //그리고 카프카로 메시지를 보낸다. 

					} catch (EntityNotFoundException ex) {//조회가 되지 않았을 경우

						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
//						serviceDb.InsertCampMa(enCampMa);//인서트 해버린다. 
//
//						msg = msgucrm.maMassage(enCampMa, "insert");//이후 인서트 형식으로 메시지 보냄
//						
//						log.info("jsonString : {}", msg);
//						MessageToProducer producer = new MessageToProducer();
//						endpoint = "/gcapi/post/" + topic_id;
//						producer.sendMsgToProducer(endpoint, msg);
//
//						return Mono.just(ResponseEntity.ok(
//								"There is no record which matched with request cpid. so it just has been inserted."));
					}

					return Mono.just(ResponseEntity.ok()
							.body(String.format("UCRM, A record with cpid : %s has been updated successfully", cpid)));

				} else {//update가 아닌 delete일 때 
					log.info("업데이트를 위한 변경할 대상 cpid : {}", cpid);
					serviceDb.DelCampMaById(cpid);
					return Mono.just(ResponseEntity.ok()
							.body(String.format("UCRM, A record with cpid : %s has been deleted successfully", cpid)));
				}

			case "Callbot":


				MsgCallbot msgcallbot = new MsgCallbot();
				msg = msgcallbot.maMassage(enCampMa, action);

				// 테이블에 Update, Delete logic 추가.
				log.info(action);
				if (action.equals("update")) {

					log.info("업데이트를 위한 변경할 대상 cpid : {}", cpid);
					log.info("새로운 캠페인명 : {}", cpna);

					try {

						serviceDb.UpdateCampMa(cpid, cpna);
						log.info("jsonString : {}", msg);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, msg);

					} catch (EntityNotFoundException ex) {

						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
//						enCampMa = serviceDb.CreateEnCampMa(row_result);
//						serviceDb.InsertCampMa(enCampMa);
//
//						msg = msgcallbot.maMassage(enCampMa, "insert");
//
//						log.info("jsonString : {}", msg);
//						MessageToProducer producer = new MessageToProducer();
//						endpoint = "/gcapi/post/" + topic_id;
//						producer.sendMsgToProducer(endpoint, msg);
//
//						return Mono.just(ResponseEntity.ok(
//								"There is no record which matched with request cpid. so it just has been inserted."));
					}

					return Mono.just(ResponseEntity.ok().body(
							String.format("Callbot, A record with cpid : %s has been updated successfully", cpid)));

				} else {
					log.info("Cpid of target record for deleting : {}", cpid);
					serviceDb.DelCampMaById(cpid);
					return Mono.just(ResponseEntity.ok().body(
							String.format("Callbot, A record with cpid : %s has been deleted successfully", cpid)));
				}

			default:
				
				MsgApim msgapim = new MsgApim();
				msg = msgapim.maMassage(enCampMa, action);

				MessageToApim apim = new MessageToApim();
				log.info("jsonString : {}", msg);

				if (action.equals("update")) {

					log.info("업데이트를 위한 변경할 대상 cpid : {}", cpid);
					log.info("새로운 캠페인명 : {}", cpna);

					try {

						serviceDb.UpdateCampMa(cpid, cpna);
						endpoint = "/cmpnMstrRegist";
						apim.sendMsgToApim(endpoint, msg);
						log.info("CAMPMA UPDATE로직,  APIM으로 보냄. : {}", msg);

					} catch (EntityNotFoundException ex) {

						log.error("EntityNotFoundException occurred: {} ", ex.getMessage());
//						enCampMa = serviceDb.CreateEnCampMa(row_result);
//						serviceDb.InsertCampMa(enCampMa);
//
//						msg = msgapim.maMassage(enCampMa, "insert");
//						log.info("jsonString : {}", msg);
//						// localhost:8084/dspRslt
//						// 192.168.219.134:8084/dspRslt
//						apim = new MessageToApim();
//						endpoint = "/cmpnMstrRegist";
//						apim.sendMsgToApim(endpoint, msg);
//
//						return Mono.just(ResponseEntity.ok("요청받은 cpid로 DB를 조회할 결과 조회된 레코드가 없습니다. 그러므로 DB에 인서트 합니다."));
					}

					return Mono.just(ResponseEntity.ok()
							.body(String.format("Apim, cpid가 %s인 레코드가 성공적으로 업데이트가 완료되었습니다.", cpid)));
				} else {
					log.info("삭제할 대상 cpid : {}", cpid);
					serviceDb.DelCampMaById(cpid);
				}
				return Mono.just(ResponseEntity.ok()
						.body(String.format("\"Apim, cpid가 %s인 레코드가 성공적으로 삭제되었습니다.", cpid)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Messge : {}", e.getMessage());
			return Mono.just(ResponseEntity.ok().body(String.format("에러가 발생하였습니다. : %s", e.getMessage())));
		}
	}

	@PostMapping("/SaveRtData")//제네시스의 이벤트를 통해 이 api가 불려짐. 
	public Mono<ResponseEntity<String>> SaveRtData(@RequestBody String msg) {

		log.info(" ");
		log.info("====== Class : ControllerCenter - Method : SaveRtData ======");

		try {

			String result = ServiceJson.extractStrVal("ExtrSaveRtData", msg);//cpid::cpsq::divisionid 리턴 값으로 이 String 받음.  
			String division = result.split("::")[2];

			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");
			log.info("디비전 이름 : {}", divisionName);

			switch (divisionName) {
			case "Home":
			case "Mobile":

				Entity_UcrmRt enUcrmrt = serviceDb.createUcrmRt(result);
				serviceDb.InsertUcrmRt(enUcrmrt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Ucrm 데이터가 성공적으로 인서트 되었습니다."));

			case "CallbotHome":
			case "CallbotMobile":

				Entity_CallbotRt enCallBotRt = serviceDb.createCallbotRt(result);
				serviceDb.InsertCallbotRt(enCallBotRt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Callbot 데이터가 성공적으로 인서트 되었습니다."));
			default:
				Entity_ApimRt enApimRt = serviceDb.createApimRt(result);
				serviceDb.InsertApimRt(enApimRt);
				log.info("====== End SaveRtData ======");
				return Mono.just(ResponseEntity.ok("Apim 데이터가 성공적으로 인서트 되었습니다."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("====== End SaveRtData ======");
			return Mono.just(ResponseEntity.ok().body(String.format("에러가 발생했습니다. : %s", e.getMessage())));
		}

	}

	@GetMapping("/sendapimrt")
	public Mono<ResponseEntity<String>> SendApimRt() {

		try {
			log.info(" ");
			log.info("====== Class : ControllerCenter - Method : SendApimRt ======");

			Page<Entity_ApimRt> entitylist = serviceDb.getAllApimRt();//apim 발신 결과와 관련된 테이블의 레코드들을 최대 1000개까지 가지고 온다. 

			if (entitylist.isEmpty()) {
				log.info("DB에서 조회 된 모든 레코드 : 없음");
			} else {
				int reps = entitylist.getNumberOfElements();//몇개의 레코드를 가지고 왔는지.
				log.info("'CAMPRT_UCUBE_W' table에서 조회 된 레코드 개수 : {}", reps);
				log.info("{}만큼 반복", reps);

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

					if (contactLtId == null || contactLtId.equals("")) {// cpid로 매치가 되는 contactltId가 있는지 조회 했는데 그것에 대응하는 contactltId가 없다면,
						log.info("일치하는 contactLtId 없음");
						String result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);
						String res = ServiceJson.extractStrVal("ExtractContactLtId", result); 	// 가져온 결과에서
																								// contactlistid,queueid만
																								// 추출.
						contactLtId = res.split("::")[0];

						String division = enApimRt.getDivisionid(); // 첫번째 레코드부터 cpid를 가지고 온다.
						Map<String, String> properties = customProperties.getDivision();
						divisionName = properties.getOrDefault(division, "디비전을 찾을 수 없습니다.");

						mapcontactltId.put(cpid, contactLtId); 
						mapdivision.put(contactLtId, divisionName);
					} else {
						log.info("일치하는 contactId 있음");
					}

					if (!contactlists.containsKey(contactLtId)) {//맵(contactlists)에 키값(contactLtId)이 없다면. 
						contactlists.put(contactLtId, new ArrayList<>());//'contactLtId'로 된 키 값 추가. 
					}
					contactlists.get(contactLtId).add(cpsq);//'contactLtId'키의 배열에 고객번호(cpsq) 넣음. 
					serviceDb.DelApimRtById(enApimRt.getId());

					log.info("해당 키 값이 '{}'인 Arraylist에 값 추가", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionName = mapdivision.get(entry.getKey());

						if (entry.getValue().size() >= 50) {//배열 크기가 50이상일 때, 배열 안의 인수 개수가 50개 이상일 때는 일단 발신 결과 전송 로직을 탄다. 50개 이상 쌓이고 하게 되면 에러남. 
							Roop(entry.getKey(), entry.getValue(), divisionName);//contactLtId 와 contactLtId에 해당하는 배열, divisionName
						}
					}
				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {//배열 안의 크기가 50개 이상이 아닐 때, 즉 50개 이상 전송하고 남은 나머지들 전송. 

					divisionName = mapdivision.get(entry.getKey());
					Roop(entry.getKey(), entry.getValue(), divisionName);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("에러 메시지 : {}", e.getMessage());
		}

		log.info("====== End SendApimRt ======");
		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	public Mono<Void> Roop(String contactLtId, List<String> values, String divisionName) throws Exception {

		log.info(" ");
		log.info("====== Class : ControllerCenter - Method : Roop ======");
		log.info("고객키의 개수 : {}", values.size());
		ObjectMapper objectMapper = null;
		String result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("[]")) {
			log.info("결과 없음, 다음으로 건너 뜀.");
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
		// 왜냐면 나머지는 똑같을테니.
		String contactsresult = ServiceJson.extractStrVal("ExtractContacts56", result, 0);// JsonString 결과값과 조회하고 싶은
																							// 인덱스(첫번째)를 인자로 넣는다.
		Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// contactsresult값으로
																				// entity하나를 만든다.

		MsgApim msgapim = new MsgApim();
		Entity_ToApim enToApim = new Entity_ToApim();
		for (int i = 0; i < values.size(); i++) {

			contactsresult = ServiceJson.extractStrVal("ExtractContacts56", result, i);

			entityCmRt = serviceDb.createCampRtMsg(contactsresult);
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

	@GetMapping("/putput/{topic}")
	public void k(@PathVariable("topic") int tranId) {

		int till = tranId;
		Entity_CampMa enCampMa = new Entity_CampMa();
		try {

			for (int i = 0; i < till; i++) {
				enCampMa.setCpid(Integer.toString(i));
				enCampMa.setCoid(i);
				enCampMa.setCpna("ggplay");
				serviceDb.InsertCampMa(enCampMa);
			}

		} catch (Exception e) {

		}
	}
	
	
	@GetMapping("/delete/{topic}")
	public void d(@PathVariable("topic") int tranId) {

		int till = tranId;
		try {

			for (int i = 0; i < till; i++) {
				String k = Integer.toString(i);
				serviceDb.DelCampMaById(k);
			}

		} catch (Exception e) {

		}
	}
	
	
	public void handlingCampMaster(int reps, String result) throws Exception{
		
		String row_result = "";
		String division = "";
		String business = "";
		String topic_id = "";
		String endpoint = "";
		String msg = "";
		MessageToProducer producer = null;
		
		while (reps-- > 0) {// reps가 0이 될 때까지 reps를 줄여나가면서 반복.

			log.info("{}번째 인덱스 ", reps);

			row_result = ServiceJson.extractStrVal("ExtractValCrm12", result, reps);// 결과 값 : cpid::coid::cpna::division ->
																					// 캠페인아이디::테넌트아이디::캠페인명::디비전

			division = row_result.split("::")[3];

			Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(division);

			business = businessLogic.get("business");
			topic_id = businessLogic.get("topic_id");

			Entity_CampMa enCampMa = serviceDb.CreateEnCampMa(row_result);

			switch (business) {//여기서 비즈니스 로직 구분. default는 'apim'
			case "UCRM":

				MsgUcrm msgucrm = new MsgUcrm();
				msg = msgucrm.maMassage(enCampMa, "insert"); //카프카 프로듀서로 보내기 위한 메시지 조립작업.

				try {
					serviceDb.InsertCampMa(enCampMa);//'enCampMa' db에 인서트 
				} catch (DataIntegrityViolationException ex) {
					log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
					continue;
				} catch (DataAccessException ex) {
					log.error("DataAccessException 발생 : {}", ex.getMessage());
					continue;
				}

				producer = new MessageToProducer();
				endpoint = "/gcapi/post/" + topic_id;
				producer.sendMsgToProducer(endpoint, msg);

				break;

			case "Callbot":

				MsgCallbot msgcallbot = new MsgCallbot();
				msg = msgcallbot.maMassage(enCampMa, "insert");

				try {
					serviceDb.InsertCampMa(enCampMa);
				} catch (DataIntegrityViolationException ex) {
					log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
					continue;
				} catch (DataAccessException ex) {
					log.error("DataAccessException 발생 : {}", ex.getMessage());
					continue;
				}

				producer = new MessageToProducer();
				endpoint = "/gcapi/post/" + topic_id;
				producer.sendMsgToProducer(endpoint, msg);

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

				MsgApim msgapim = new MsgApim();
				msg = msgapim.maMassage(enCampMa, "insert");

				// localhost:8084/dspRslt
				// 192.168.219.134:8084/dspRslt
				MessageToApim apim = new MessageToApim();
				endpoint = "/cmpnMstrRegist";
				apim.sendMsgToApim(endpoint, msg);
				log.info("CAMPMA 로직, APIM으로 보냄. : {}", msg);

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

	/**
	 * [EKS] POD LivenessProbe 헬스체크
	 */
	private final Instant started = Instant.now();

	@GetMapping("/healthz")
	public ResponseEntity<String> healthCheck() {
		Duration duration = Duration.between(started, Instant.now());
		if (duration.getSeconds() > 10) {
			return ResponseEntity.status(500).body("에러 : " + duration.getSeconds());
		} else {
			return ResponseEntity.ok("ok");
		}
	}

}
