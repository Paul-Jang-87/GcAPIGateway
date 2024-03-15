package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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
import gc.apiClient.embeddable.oracle.DataCall;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.entity.Entity_ToApim;
import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceJsonOracle;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageTo360View;
import gc.apiClient.messages.MessageToApim;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.repository.oracle.Repository_WaDataCallOptional;
import gc.apiClient.service.ServiceJson;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerUCRM extends ServiceJson {

	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceDBOracle serviceOracle;
	private final InterfaceJsonOracle serviceJsonOracle;
	private final InterfaceMsgObjOrcl serviceMsgObjOrcl;
	private final InterfaceWebClient serviceWeb;
	private final CustomProperties customProperties;
	private static List<Entity_ToApim> apimEntitylt = new ArrayList<Entity_ToApim>();

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb, InterfaceDBOracle serviceOracle,
			InterfaceWebClient serviceWeb, InterfaceJsonOracle serviceJsonOracle, CustomProperties customProperties,
			InterfaceMsgObjOrcl serviceMsgObjOrcl) {
		this.serviceDb = serviceDb;
		this.serviceOracle = serviceOracle;
		this.serviceJsonOracle = serviceJsonOracle;
		this.serviceWeb = serviceWeb;
		this.customProperties = customProperties;
		this.serviceMsgObjOrcl = serviceMsgObjOrcl;
	}

//	@Scheduled(fixedRate = 60000)
//	public void scheduledMethod() {
//		log.info("Scheduled method started...");
//		ReceiveMessage("campma");
//	}

	@GetMapping("/gcapi/get/{topic}")
	public Mono<Void> ReceiveMessage(@PathVariable("topic") String tranId) {

		log.info("Class : ControllerUCRM - Method : ReceiveMessage");
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

		case "campma":

//		{
//		    "cpid":"e89ccef6-0328-6646-eacc-fa80c605fb99", or "97e6b32d-c266-4d33-92b4-01ddf33898cd"
//			"coid": "22", or "23"
//			"cpna":"카리나" or "장원영" 
//		}

			result = serviceWeb.GetApiRequet("campaignId");

			row_result = ExtractValCrm12(result); // cpid::cpna::division -> 캠페인아이디::캠페인명
			cpid = row_result.split("::")[0];
			division = row_result.split("::")[2];

			if (serviceDb.findCampMaByCpid(cpid) != null) {// campma 테이블에 이미 있는 캠페인이라면 pass.

			} else {

				Map<String, String> businessLogic = BusinessLogic.SelectedBusiness(division);

				business = businessLogic.get("business");
				topic_id = businessLogic.get("topic_id");

				int coid = serviceDb.findMapcoidByCpid(cpid).getCoid();// cpid를 가지고 Mapcoid테이블에서 일치하는 레코드 검색 후 coid 추출.
				row_result = row_result + "::" + coid;
				Entity_CampMa entityMa = serviceDb.createCampMaMsg(row_result);

				switch (business) {
				case "UCRM":
				case "Callbot":

					objectMapper = new ObjectMapper();

					try {
						String jsonString = objectMapper.writeValueAsString(entityMa);
						log.info("jsonString : {}", jsonString);
						MessageToProducer producer = new MessageToProducer();
						endpoint = "/gcapi/post/" + topic_id;
						producer.sendMsgToProducer(endpoint, jsonString);

					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					// db인서트
					try {
						serviceDb.InsertCampMa(entityMa);
					} catch (DataIntegrityViolationException ex) {
						log.error("DataIntegrityViolationException 발생 : {}", ex.getMessage());
					} catch (DataAccessException ex) {
						log.error("DataAccessException 발생 : {}", ex.getMessage());
					}

					break;

				default:

					objectMapper = new ObjectMapper();

					try {
						String jsonString = objectMapper.writeValueAsString(entityMa);

						// localhost:8084/dspRslt
						// 192.168.219.134:8084/dspRslt
						MessageToApim apim = new MessageToApim();
						endpoint = "/cmpnMstrRegist";
						apim.sendMsgToApim(endpoint, jsonString);
						log.info("CAMPMA 로직 : {} APIM으로 보냄. : {}", jsonString);

					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					break;
				}
			}
		}

		return Mono.empty();
	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		log.info("Class : ControllerUCRM - Method : receiveMessage");
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

		case "thirdtopic":// IF-CRM_003
		case "forthtopic":// IF-CRM_004

//			{
//			"cpid":"97e6b32d-c266-4d33-92b4-01ddf33898cd",
//			"cpsq":892012,209481
//			"cske":"83b85d7ff68cb7f0b7b3c59212abefff",  or   "0b241f9bef1df80679bfba58582c8505",
//			"tno1":"tno1",
//			"tno2":"tno2",
//			"tno3":"tno3",
//			"csna":"카리나",
//			"tkda":"C,111,custid", or  "A||gg||dfe||feq||ere||666",
//			"flag":"HO2"
//			}

			// 간단한 테스트를 하기 위한 샘플 json 데이터. msg로 위 데이터가 들어 온 것으로 가정.

			row_result = ExtractValCrm34(msg); // ContactLt 테이블에 들어갈 값들만
			// 뽑아온다.cpid::cpsq::cske::csna::flag::tkda::tno1::tno2::tno3
			Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(row_result);// ContactLt 테이블에 들어갈 값들을
			// Entity_ContactLt 객체에 매핑시킨다.
			cpid = enContactLt.getId().getCpid();// 캠페인 아이디를 가져온다.

			result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);// 캠페인 아이디로
																			// "/api/v2/outbound/campaigns/{campaignId}"호출
																			// 후 결과 가져온다.

			String contactLtId = ExtractContactLtId(result); // 가져온 결과에서 contactlistid만 추출.
			log.info("contactLtId : {}", contactLtId);

			// "api/v2/outbound/contactlists/{contactListId}/contacts"로 request body값 보내기 위한
			// 객체
			// 객체 안의 속성들(키)은 변동 될 수 있음.
			Entity_ContactltMapper contactltMapper = serviceDb.createContactLtGC(row_result);

			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(contactltMapper); // 매핑한 객체를 jsonString으로 변환.
				log.info("JsonString Data : {}", jsonString);

				// "api/v2/outbound/contactlists/{contactListId}/contacts"로 보냄.
				// 첫번째 인자 : 어떤 api를 호출 할 건지 지정.
				// 두번째 인자 : path parameter
				// 세번째 인자 : request body.

				serviceWeb.PostContactLtApiRequet("contact", contactLtId, jsonString);

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
			log.info("result : {}", result); // campaignid, contactlistid, division 추출

			String parts[] = result.split("::");

			int dirt = 0;
			cpid = parts[0];
			contactLtId = parts[1];
			division = parts[2];

			// appliction.properties 파일에서 division와 매치되는 divisionName을 가지고 옴.
			Map<String, String> properties = customProperties.getDivision();
			String divisionName = properties.getOrDefault(division, "couldn't find division");
			log.info("division : {}", divisionName);

			// contactlt테이블에서 cpid가 같은 모든 레코드들을 엔티티 오브젝트로 리스트 형태로 가지고 온다.
			List<Entity_ContactLt> enContactList = new ArrayList<Entity_ContactLt>();
			enContactList = serviceDb.findContactLtByCpid(cpid);

			// 가지고 온 모든 엔티티들의 숫자만큼 for문들 돌면서 레코드들 각각의 cske(고객키)들을 가지고 온다. 그리고 values리스트에
			// 담는다.
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < enContactList.size(); i++) {
				values.add(enContactList.get(i).getCske());
			}

			// contactLtId를 키로 하여 제네시스의 api를 호출한다. 호출할 때는 values리스트 담겨져 있던 cske(고객키)들 각각에 맞는
			// 결과 값들을
			// jsonString문자열로 한꺼번에 받는다.
			result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);

			// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다.
			// 왜냐면 나머지는 똑같을테니.
			String contactsresult = ExtractContacts56(result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
			contactsresult = contactsresult + "::" + cpid;// contactid(고객키)::contactListId::didt::dirt::cpid
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
					contactsresult = contactsresult + "::" + cpid; // contactid(고객키)::contactListId::didt::dirt::cpid
					entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

					dirt = entityCmRt.getDirt();// 응답코드

					if ((business.equals("UCRM")) && (dirt == 1)) {// URM이면서 정상일 때.

					} else {
						Entity_CampRtJson toproducer = serviceDb.createCampRtJson(contactsresult);// producer로 보내기 위한
						// entity.
						objectMapper = new ObjectMapper();

						try {
							String jsonString = objectMapper.writeValueAsString(toproducer);
							log.info("JsonString Data : {}번째 {}", i, jsonString);

							MessageToProducer producer = new MessageToProducer();
							endpoint = "/gcapi/post/" + topic_id;
							producer.sendMsgToProducer(endpoint, jsonString);

						} catch (JsonProcessingException e) {
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
					apim.sendMsgToApim(endpoint, jsonString);
					log.info("CAMPRT 로직, APIM으로 보냄. : {} ", jsonString);

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				break;
			}

			return Mono.empty();

		default:
			break;
		}

		return Mono.empty();
	}

	
	@GetMapping("/360view/datacalloptional")
	public Mono<Void> Msgfor360view1() {
		String topic_id = "datacalloptional";
		int numberOfRecords = serviceOracle.getRecordCount(topic_id);
		log.info("the number of records : {}", numberOfRecords);

		if (numberOfRecords < 1) {

		} else {// 1.테이블에 있는 레코드들을 다 긁어 온다.
			List<Entity_WaDataCallOptional> entitylist = serviceOracle.getAllWaDataCallOptional();
				// 2. 구분해서 토픽으로 보낸다.

			for (int i = 0; i < entitylist.size(); i++) {
				MessageTo360View.sendMsgTo360View(topic_id, serviceMsgObjOrcl.msg(entitylist.get(i)));
			}
		}
		return Mono.empty();
	}

	
	@GetMapping("/360view/datacall")
	public Mono<Void> Msgfor360view2() {
		String topic_id = "datacall";
		int numberOfRecords = serviceOracle.getRecordCount(topic_id);
		log.info("the number of records : {}", numberOfRecords);

		if (numberOfRecords < 1) {

		} else {// 1.테이블에 있는 레코드들을 다 긁어 온다.
			List<Entity_DataCall> entitylist = serviceOracle.getAllDataCall();
				// 2. 구분해서 토픽으로 보낸다.

			for (int i = 0; i < entitylist.size(); i++) {
				MessageTo360View.sendMsgTo360View(topic_id, serviceMsgObjOrcl.msg(entitylist.get(i)));
			}
		}
		return Mono.empty();
	}
	
	
}
