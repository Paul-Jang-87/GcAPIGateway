package kafka.gcClient.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mypurecloud.sdk.v2.ApiException;

import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_MapCoid;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.service.CrmSv01;
import kafka.gcClient.service.CrmSv05;
import reactor.core.publisher.Mono;

@RestController
public class GcController {
	
	private final InterfaceDB serviceDb;

	public GcController(InterfaceDB serviceDb) {
		this.serviceDb = serviceDb;
	}

	// APIM

	// GC API

	
	@GetMapping("/gcapi/get/{topic}")
	public String getApiData(@PathVariable("topic") String tranId) throws IOException, ApiException {
		
		String result = "";
		String topic_name = tranId.toUpperCase();
		
		switch (topic_name) {
		case "IF-CRM-001":
			CrmSv01 api1 = new CrmSv01();
			api1.GetApiRequet(topic_name);
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
		String topic_id = tranId.toUpperCase(); 

	    switch (topic_id) {
	    
	        case "IF-CRM-003": //시나리오 : 어떤 api호출 후 그 결과 값에서 특정 값을 뽑아 제가공하여 entity 메시지를 만들고 db에 인서트
	           CrmSv01 crmapi1 = new CrmSv01();
	           result = crmapi1.GetApiRequet("campaignId");//api호출 후 결과 값을 받음. 
	        	
	            Entity_CampMa entity = serviceDb.createCampMaMsg(result);//CMAPMA테이블에 인터트 하기위해 json data 가공작업.
	            return serviceDb.InsertCampMa(entity)//CMAPMA테이블에 매핑할 수 있는 entity 객체를 테이블에 인서트 
	                    .flatMap(savedEntity -> {
	                        return Mono.empty(); 
	                    });
	            
	        case "IF-CRM-002":
	        		
	        		Entity_MapCoid temp = serviceDb.createMapCoIdMsg();
	        		return serviceDb.InsertMapCoId(temp) 
	        				.flatMap(savedEntity -> {
	        					return Mono.empty(); 
	        				});
	        default:
	            break;
	    }
	    
	    return  Mono.empty();

	}



	@PostMapping("/gcapi/kafka/receive-message")
	public String receiveMessage(@RequestBody String message) {
	    System.out.println("Received message from the first application: " + message);
	    return "a";
	}

	
//	// Kafka
//	@PostMapping("/api/receive-message")
//	public String receiveMessage(@RequestBody String message) {
//		System.out.println("Received message from the first application: " + message);
//		KafkaMsgEntity testEmployee = kafkaMsgService.createTestMsg();
//		return "a";
//		return kafkaMsgService.insertMsg(testEmployee).thenReturn("Message received and inserted successfully");
//	}

}
