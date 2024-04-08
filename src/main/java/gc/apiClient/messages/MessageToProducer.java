package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageToProducer {
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		log.info(" ");
		log.info("====== ClassName : MessageToProducer & Method : sendMsgToProducer ======");
		log.info("Producer로 보낼 EndPoint : {}",towhere);
		log.info("Producer로 보낼 Message : {}",jsonString);
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

	    String endpointUrl = towhere;  
	    String result = "";
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .bodyToMono(String.class)
	            .onErrorResume(error -> {
	                log.error("Error making API request: {}",error.getMessage()) ;
	                return Mono.empty();
	            })
	            .subscribe(responseBody -> {
	                log.info("Response received: {}", responseBody);
	                
	            }); 
	    
		log.info("====== End sendMsgToProducer ======");
	}
	
	

}
