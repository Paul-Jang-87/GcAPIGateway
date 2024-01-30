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

	    switch (tranId.toUpperCase()) {
	        case "IF-CRM-003":
	           CrmSv01 crmapi1 = new CrmSv01();
	           result = crmapi1.GetApiRequet("campaignId");
	        	
	            Entity_CampMa entity = serviceDb.createCampMaMsg(result);
	            
	            
	            return serviceDb.InsertCampMa(entity)
	                    .flatMap(savedEntity -> {
	                        return Mono.empty(); // or any other response
	                    });
	            
	        case "IF-CRM-002":
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
