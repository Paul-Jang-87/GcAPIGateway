package kafka.gcClient.kafkamessages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = "/gcapi/post/"+towhere;

	    System.out.println("Endpoint : "+ endpointUrl);
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .block(); 

	    
	    System.out.println("Entity_CampMa as JSON: " + jsonString);
	}
	
	

}
