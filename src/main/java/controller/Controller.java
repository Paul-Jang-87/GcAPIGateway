package controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mypurecloud.sdk.v2.ApiException;

import entity.Entity_CampRt;
import interfaceCollection.InterfaceDB;
import reactor.core.publisher.Mono;
import service.CrmSv01;
import service.CrmSv05;

@RestController
public class Controller {
	
	private final InterfaceDB serviceDb;


	public Controller(InterfaceDB serviceDb) {
		this.serviceDb = serviceDb;
	}

	// APIM

	// GC API

	@GetMapping("/")
    public String home() {
        return "Hello, World!";
    }
	
	@GetMapping("/gcapi/get/{topic}")
	public String getApiData(@PathVariable("topic") String tranId) throws IOException, ApiException {
		
		System.out.println(tranId);
		String result = "";
		String topic_name = tranId.toUpperCase();
		System.out.println(topic_name);
		switch (topic_name) {
		case "IF-CRM-001":
			System.out.println("1");
			CrmSv01 api = new CrmSv01();
			api.GetApiRequet(topic_name);
			System.out.println("1end");
			break;
		case "IF-CRM-002":
			break;
		case "IF-CRM-005":
			System.out.println("5");
			CrmSv05 ng = new CrmSv05();
			ng.GetApiRequet(topic_name);
			System.out.println("5end");
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
	public Mono<String> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {
		
		switch (tranId.toUpperCase()) {
		case "IF-CRM-003":
			break;
		case "IF-CRM-004":
			break;
		default:
			break;
		}
		
		
		Entity_CampRt testEmployee = serviceDb.createCampRtMsg();
		
		return serviceDb.insertMsg(testEmployee).thenReturn("Message received and inserted successfully");
	}


//	// Kafka
	//@PostMapping("/api/receive-message")
//	public Mono<String> receiveMessage(@RequestBody String message) {
//		System.out.println("Received message from the first application: " + message);
//		KafkaMsgEntity testEmployee = kafkaMsgService.createTestMsg();
//		return kafkaMsgService.insertMsg(testEmployee).thenReturn("Message received and inserted successfully");
//	}

}
