package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageToApim {
	
	public void sendMsgToApim (String towhere, String entity) {
		
		log.info("====== ClassName : 'MessageToApim' & Method : 'ToApim' ======");
		
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8084")
				.defaultHeader("Accept", "application/json")
				.defaultHeader("Content-Type", "application/json").build();

	    String endpointUrl = towhere;

	    log.info("ToApim Endpoint : {}",endpointUrl);
	    log.info("Message for Apim: {}",entity);
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(entity))
	            .retrieve()
	            .bodyToMono(String.class)
	            .onErrorResume(error -> {
	                log.error("Error making API request: {}",error.getMessage()) ;
	                return Mono.empty();
	            })
	            .block(); // Wait for the result

	    log.info("====== End ToApim ======");
	}
	
}
