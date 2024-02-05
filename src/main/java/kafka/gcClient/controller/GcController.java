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
import kafka.gcClient.service.CrmSv01;
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
		String topic_name = tranId.toUpperCase();

		switch (topic_name) {
		case "IF-CRM-001":
			CrmSv01 api1 = new CrmSv01(serviceDb);
			api1.GetApiRequet("campaignId");
			break;

		case "IF-CRM-002":
			break;

		case "IF-CRM-005":
			CrmSv05 api5 = new CrmSv05();
			api5.GetApiRequet(topic_name);
			break;

		case "IF-CRM-006":
			break;
		default:
			result = "ERROR";
			break;
		}
		return result;
	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		String result = "";
		String topic_id = tranId;

		switch (topic_id) {

		case "firsttopic": // 시나리오 : 어떤 api호출 후 그 결과 값에서 특정 값을 뽑아 제가공하여 entity 메시지를 만들고 db에 인서트
			CrmSv01 crmapi1 = new CrmSv01(serviceDb);
			result = crmapi1.GetApiRequet("campaignId");// api호출 후 결과 값을 받음.

			System.out.println(result);

			Entity_CampMa entity = serviceDb.createCampMaMsg(result);// CMAPMA테이블에 인터트 하기위해 json data 가공작업.
			serviceDb.InsertCampMa(entity);// CMAPMA테이블에 매핑할 수 있는 entity 객체를 테이블에 인서트
			return Mono.empty();

		case "secondtopic":// 시나리오 : 들어온 cpid로 MapCoid테이블 조회, cpid와 매칭되는 coid가져와
							// kafkaProducer로 보내고 CampMa테이블에 insert

			String cpid = ExtractCpid(msg);
			System.out.println(cpid);

			Entity_CampMa entityMa = serviceDb.createCampMaMsg(cpid);
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				String jsonString = objectMapper.writeValueAsString(entityMa);
				System.out.println(jsonString);
				MessageToProducer producer = new MessageToProducer();
				producer.sendMsgToProducer("secondtopic", jsonString);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			serviceDb.InsertCampMa(entityMa);

			return Mono.empty();

		case "thirdtopic":

			String cpid_3 = ExtractCpidfromThird(msg);//request body로 들어돈 json에서 필요 데이터 추출
			System.out.println(cpid_3);
			
			
			Entity_CampRt entityCmRt_3 = serviceDb.createCampRtMsg(cpid_3);// db 인서트 하기 위한 entity.
			Entity_CampRt toproducer = serviceDb.createCampRtToJson(cpid_3);// producer로 보내기 위한 entity.
			ObjectMapper objectMapper_3 = new ObjectMapper(); 
			
			

			try {
				String jsonString = objectMapper_3.writeValueAsString(toproducer);
				System.out.println("JsonString Data : =="+jsonString);
				
				MessageToProducer producer = new MessageToProducer();
				producer.sendMsgToProducer("thirdtopic", jsonString);//'thirdtopic'토픽으로 메시지 보냄.

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			serviceDb.InsertCampRt(entityCmRt_3);

			return Mono.empty();

		default:
			break;
		}

		return Mono.empty();
	}

}
