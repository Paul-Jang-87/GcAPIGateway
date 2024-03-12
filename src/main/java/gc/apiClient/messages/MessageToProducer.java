package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		log.info("===== sendMsgToProducer =====");
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = towhere; //ex)"/gcapi/post/firsttopic" 

	    log.info("Endpoint : {}",endpointUrl);
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .onErrorResume(error -> {
	                log.error("Error making API request: {}",error.getMessage()) ;
	                return Mono.empty();
	            })
	            .block(); // Wait for the result

	    log.info("Entity as JSON: {}",jsonString);
	}
	
	

}
