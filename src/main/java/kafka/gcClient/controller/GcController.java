package kafka.gcClient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.gcClient.encryptdecrypt.AESDecryption;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import kafka.gcClient.entity.Entity_MapCoid;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.kafkamessages.MessageToProducer;
import kafka.gcClient.service.ServiceWebClient;
import kafka.gcClient.service.CrmSv05;
import kafka.gcClient.service.ServiceJson;
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

		switch (topic_id) {

		case "firsttopic"://IF-CRM_001 
		case "secondtopic"://IF-CRM_002
			
			String cpid = ExtractValCrm12(msg);
			System.out.println(cpid);

			Entity_CampMa entityMa = serviceDb.createCampMaMsg(cpid);
			ObjectMapper objectMapper = new ObjectMapper();

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
			
		case "thirdtopic"://IF-CRM_003 
		case "forthtopic"://IF-CRM_004

			Entity_ContactLt enContactLt = serviceDb.createContactLtMsg(msg);
			serviceDb.InsertContactLt(enContactLt);

			return Mono.empty();


		case "fifthtopic"://IF-CRM_005
		case "sixthtopic"://IF-CRM_006

			String cpid_3 = ExtractCrm56(msg);//request body로 들어돈 json에서 필요 데이터 추출
			System.out.println(cpid_3);
			
			
			Entity_CampRt entityCmRt_3 = serviceDb.createCampRtMsg(cpid_3);// db 인서트 하기 위한 entity.
			Entity_CampRt toproducer = serviceDb.createCampRtToJson(cpid_3);// producer로 보내기 위한 entity.
			ObjectMapper objectMapper_3 = new ObjectMapper(); 
			
			try {
				String jsonString = objectMapper_3.writeValueAsString(toproducer);
				System.out.println("JsonString Data : =="+jsonString);
				
//				MessageToProducer producer = new MessageToProducer();
//				producer.sendMsgToProducer(topic_id, jsonString);//'thirdtopic'토픽으로 메시지 보냄.

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

//			serviceDb.InsertCampRt(entityCmRt_3);

			return Mono.empty();

		default:
			break;
		}

		return Mono.empty();
	}

}
