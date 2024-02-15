package gc.apiClient.kafkamessages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		System.out.println("===sendMsgToProducer===");
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = towhere;

	    System.out.println("Endpoint : "+ endpointUrl);
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .block(); 

	    
	    System.out.println("Entity as JSON: " + jsonString);
	}
	
	

}
