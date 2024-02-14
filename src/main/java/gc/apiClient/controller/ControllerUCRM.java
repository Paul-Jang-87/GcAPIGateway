package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.interfaceCollection.InterfaceDB;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.kafkamessages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import jakarta.persistence.Column;
import reactor.core.publisher.Mono;

@RestController
public class ControllerUCRM extends ServiceJson {

	private final InterfaceDB serviceDb;
	private final InterfaceWebClient serviceWeb;

	public ControllerUCRM(InterfaceDB serviceDb, InterfaceWebClient serviceWeb) {
		this.serviceDb = serviceDb;
		this.serviceWeb = serviceWeb;
	}

	// APIM

	// GC API

//	@GetMapping("/gcapi/get/{topic}")
//	public String getApiData(@PathVariable("topic") String tranId) {
//
//		String result = "";
//		return result;
//	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		System.out.println("Class : ControllerUCRM\nMethod : receiveMessage");
		String result = "";
		String cpid = "";
		String topic_id = tranId;
		ObjectMapper objectMapper = null;

		System.out.println("topic_id : " + topic_id);

		switch (topic_id) {

		case "firsttopic":// IF-CRM_001
		case "secondtopic":// IF-CRM_002

			cpid = ExtractValCrm12(msg);
			System.out.println("cpid : " + cpid);

			Entity_CampMa entityMa = serviceDb.createCampMaMsg(cpid);
			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(entityMa);
				System.out.println("jsonString : " + jsonString);
				MessageToProducer producer = new MessageToProducer();
				producer.sendMsgToProducer(topic_id, jsonString);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

//			serviceDb.InsertCampMa(entityMa);

			return Mono.empty();

		case "thirdtopic":// IF-CRM_003
		case "forthtopic":// IF-CRM_004

			// {"id":null,"cpid":"97e6b32d-c266-4d33-92b4-01ddf33898cd","cpsq":109284,"cske":"customerkey","tn01":"tn01","tn02":"tn02","tn03":"tn03","csna":"카리나","tkda":"custid,111","flag":"HO2"}
			// 간단한 테스트를 하기 위한 샘플 json 데이터. msg로 위 데이터가 들어 온 것으로 가정.

			result = ExtractValCrm34(msg); // ContactLt 테이블에 들어갈 값들만 뽑아온다.
			System.out.println("result : " + result);
			Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(result);// ContactLt 테이블에 들어갈 값들을
																				// Entity_ContactLt 객체에 매핑시킨다.
			cpid = enContactLt.getCpid();// 캠페인 아이디를 가져온다.

			result = serviceWeb.GetCampaignsApiRequet("campaigns", cpid);// 캠페인 아이디로
																			// "/api/v2/outbound/campaigns/{campaignId}"호출
																			// 후 결과 가져온다.
			String contactLtId = ExtractContactLtId(result); // 가져온 결과에서 contactlistid만 추출.
			System.out.println("contactLtId : " + contactLtId);

			// "api/v2/outbound/contactlists/{contactListId}/contacts"로 request body값 보내기 위한
			// 객체
			// 객체 안의 속성들(키)은 변동 될 수 있음.
			Entity_ContactltMapper contactltMapper = new Entity_ContactltMapper();

			// 현재는 이름, 전화번호만 있다고 가정 후 세팅.
			contactltMapper.setName("장원영");
			contactltMapper.setNumber("01099992222");

			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(contactltMapper); // 매핑한 객체를 jsonString으로 변환.
				System.out.println("JsonString Data : ==" + jsonString);

				// "api/v2/outbound/contactlists/{contactListId}/contacts"로 보냄.
				// 첫번째 인자 : 어떤 api를 호출 할 건지 지정.
				// 두번째 인자 : path parameter
				// 세번째 인자 : request body.
				serviceWeb.PostContactLtApiRequet("contact", contactLtId, jsonString);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			// DB인서트
			serviceDb.InsertContactLt(enContactLt);

			return Mono.empty();

		case "fifthtopic":// IF-CRM_005
		case "sixthtopic":// IF-CRM_006

			result = ExtractVal56(msg);// request body로 들어돈 json에서 필요 데이터 추출
			System.out.println("result : " + result); // campaignid, contactlistid 추출

			String parts[] = result.split("\\|");

			cpid = parts[0];
			contactLtId = parts[1];

			List<Entity_ContactLt> enContactList = new ArrayList<Entity_ContactLt>();
			enContactList = serviceDb.findContactLtByCpid(cpid);// campaignid가 같은 모든 엔티디들을 리스트로 가지고 온다.

			List<String> values = new ArrayList<String>();// cske(고객키)들을 담을 list타입 변수.

			for (int i = 0; i < enContactList.size(); i++) {//
				values.add(enContactList.get(i).getCske());
			}

			result = serviceWeb.PostContactLtApiBulk("contactList", contactLtId, values);// 고객키 list를 request body 담아서
																							// api bulk호출.

			for (int i = 0; i < enContactList.size(); i++) {
				String contactsresult = ExtractContacts56(result, i);
				contactsresult = contactsresult + "|" + cpid;// contactid(고객키)|contactListId|didt|dirt|cpid
				Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(contactsresult);// db 인서트 하기 위한 entity.

				int dirt = entityCmRt.getDirt();// 응답코드

				if (dirt > 1) {// 2이상이면 에러.

					Entity_CampRtJson toproducer = serviceDb.createCampRtJson(contactsresult);// producer로 보내기 위한 entity.
					objectMapper = new ObjectMapper();

					try {
						String jsonString = objectMapper.writeValueAsString(toproducer);
						System.out.println("JsonString Data : " + i + "번째" + jsonString);

						MessageToProducer producer = new MessageToProducer();
						producer.sendMsgToProducer(topic_id, jsonString);

					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					serviceDb.InsertCampRt(entityCmRt);

				}

			}


			return Mono.empty();

		default:
			break;
		}

		return Mono.empty();
	}

}
