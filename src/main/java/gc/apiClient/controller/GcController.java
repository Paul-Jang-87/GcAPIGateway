package gc.apiClient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.encryptdecrypt.AESDecryption;
import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_MapCoid;
import gc.apiClient.interfaceCollection.InterfaceDB;
import gc.apiClient.kafkamessages.MessageToProducer;
import gc.apiClient.service.CrmSv05;
import gc.apiClient.service.ServiceJson;
import gc.apiClient.service.ServiceWebClient;
import reactor.core.publisher.Mono;

@RestController
public class GcController extends ServiceJson {

	private final InterfaceDB serviceDb;

	public GcController(InterfaceDB serviceDb) {
		this.serviceDb = serviceDb;
	}

	// APIM

	// GC API

	@GetMapping("/gcapi/get/{topic}")
	public String getApiData(@PathVariable("topic") String tranId) {

		String result = "";
		return result;
	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		String result = "";
		String topic_id = tranId;
		ObjectMapper objectMapper = null;

		switch (topic_id) {

		case "firsttopic":// IF-CRM_001
		case "secondtopic":// IF-CRM_002

			String cpid = ExtractValCrm12(msg);
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
			
			result = ExtractValCrm34(msg);

			Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(result);
			serviceDb.InsertContactLt(enContactLt);

			return Mono.empty();

		case "fifthtopic":// IF-CRM_005
		case "sixthtopic":// IF-CRM_006

			result = ExtractCrm56(msg);// request body로 들어돈 json에서 필요 데이터 추출
			System.out.println(result);

			Entity_CampRt entityCmRt = serviceDb.createCampRtMsg(result);// db 인서트 하기 위한 entity.
			Entity_CampRtJson toproducer = serviceDb.createCampRtJson(result);// producer로 보내기 위한 entity.
			objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(toproducer);
				System.out.println("JsonString Data : ==" + jsonString);

				MessageToProducer producer = new MessageToProducer();
				producer.sendMsgToProducer(topic_id, jsonString);//'thirdtopic'토픽으로 메시지 보냄.

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
	
	
	
	
	@PostMapping("/gcapi/fromkafka/{topic}")
	public Mono<Void> MessageFormKafka(@PathVariable("topic") String tranId, @RequestBody String msg) {

		String result = "";
		String topic_id = tranId;

		switch (topic_id) {

		case "thirdtopic":// IF-CRM_003
		case "forthtopic":// IF-CRM_004

			result = ExtractValCrm34(msg);
			System.out.println("===MessageFormKafka===");
			System.out.println("message from consumer : "+msg);
			System.out.println("after extraction : "+result);
			

		default:
			break;
		}

		return Mono.empty();
	}

}
