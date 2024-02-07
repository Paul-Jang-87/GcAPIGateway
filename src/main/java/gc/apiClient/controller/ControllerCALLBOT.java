package gc.apiClient.controller;

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
import gc.apiClient.interfaceCollection.InterfaceDB;
import gc.apiClient.kafkamessages.MessageToProducer;
import gc.apiClient.service.ServiceJson;
import reactor.core.publisher.Mono;

@RestController
public class ControllerCALLBOT extends ServiceJson {

	private final InterfaceDB serviceDb;

	public ControllerCALLBOT(InterfaceDB serviceDb) {
		this.serviceDb = serviceDb;
	}

	// APIM

	// GC API

//	@GetMapping("/apicallbot/get/{topic}")
//	public String getApiData(@PathVariable("topic") String tranId) {
//
//		String result = "";
//		return result;
//	}

	@PostMapping("/apicallbot/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		String result = "";
		String topic_id = tranId;
		ObjectMapper objectMapper = null;

		switch (topic_id) {

		case "firsttopic":// IF-CRM_001
		case "secondtopic":// IF-CRM_002

			String cpid = ExtractValCallbot12(msg);
			System.out.println(cpid);

			Entity_CampMa entityMa = serviceDb.createCampMaMsg(cpid);
			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(entityMa);
				System.out.println(jsonString);
				MessageToProducer producer = new MessageToProducer();
				producer.sendMsgToProducer(topic_id, jsonString);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

//			serviceDb.InsertCampMa(entityMa);

			return Mono.empty();

		case "thirdtopic":// IF-CRM_003
		case "forthtopic":// IF-CRM_004
			
			//{"id":null,"cpid":"97e6b32d-c266-4d33-92b4-01ddf33898cd","cpsq":109284,"cske":"customerkey","tn01":"tn01","tn02":"tn02","tn03":"tn03","csna":"카리나","tkda":"custid,111","flag":"HO2"}
			//간단한 테스트를 하기 위한 샘플 json 데이터. msg로 위 데이터가 들어 온 것으로 가정. 
			
			result = ExtractValCallbot34(msg);

			Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(result);
			serviceDb.InsertContactLt(enContactLt);

			return Mono.empty();

		case "fifthtopic":// IF-CRM_005
		case "sixthtopic":// IF-CRM_006

			result = ExtractVal56(msg);// request body로 들어돈 json에서 필요 데이터 추출
			System.out.println(result);

			Entity_CampRt entityCmRt = serviceDb.createCampRtMsgCallbot(result);// db 인서트 하기 위한 entity.
				
				Entity_CampRtJson toproducer = serviceDb.createCampRtJsonCallbot(result);// producer로 보내기 위한 entity.
				objectMapper = new ObjectMapper();
				
				try {
					String jsonString = objectMapper.writeValueAsString(toproducer);
					System.out.println("JsonString Data : ==" + jsonString);
					
					MessageToProducer producer = new MessageToProducer();
					producer.sendMsgToProducer(topic_id, jsonString);
					
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				serviceDb.InsertCampRt(entityCmRt);
				

			return Mono.empty();

		default:
			break;
		}

		return Mono.empty();
	}
	
	

}
