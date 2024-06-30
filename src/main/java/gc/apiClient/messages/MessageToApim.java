package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gc.apiClient.AppConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageToApim {
	
	private String domain = AppConfig.getDomain(); 
	private String port="8084"; 
	
	public void sendMsgToApim (String towhere, String entity) {
		
		log.info("====== Method : sendMsgToApim ======");
		
		WebClient webClient = WebClient.builder()
				.baseUrl(domain+":"+port)
				.defaultHeader("Accept", "application/json")
				.defaultHeader("Content-Type", "application/json").build();

	    String endpointUrl = towhere;

	    log.info("ToApim Endpoint : {}",endpointUrl);
	    log.info("Apim으로 보낼 메시지: {}",entity);
	    
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

	}
	
}
