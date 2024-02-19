package gc.apiClient.kafkamessages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		log.info("===== sendMsgToProducer =====");
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = towhere;

	    log.info("Endpoint : {}",endpointUrl);
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .block(); 

	    log.info("Entity as JSON: {}",jsonString);
	}
	
	

}
