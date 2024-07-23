package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gc.apiClient.AppConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
/**
 * webClient이용
 * Apim(으)로 메시지 보낼 때를 위한 클래스이다. 
 * 어디로 메시지를 보낼지(토픽명-towhere), 어떤 내용을 보낼지(메시지-entity)를 매개변수로 전달한다. 
 */
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
