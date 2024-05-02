package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		
		log.info(" ");
		log.info("====== ClassName : MessageToProducer & Method : sendMsgToProducer ======");
		log.info("Producer로 보낼 EndPoint & Msg : '{}' / {}",towhere,jsonString);
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = towhere;  
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .doOnError(error -> {
	                log.error("Error making API request: {}", error.getMessage());
	                error.printStackTrace();
	            })
	            .subscribe(responseBody -> {
	                log.info("Response received : {}", responseBody);
	            }, error -> {
	                log.error("Error in handling response: {}", error.getMessage());
	            }, () -> {
	                log.info("Request completed successfully.");
	            });
	    
		log.info("====== End sendMsgToProducer ======");
	}
	
	

}
