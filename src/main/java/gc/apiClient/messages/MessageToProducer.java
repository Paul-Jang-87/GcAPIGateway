package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gc.apiClient.AppConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageToProducer {
	
	private  String domain = AppConfig.getDomain();
	private  String port="8081"; 
	
	public void sendMsgToProducer (String towhere, String jsonString) {
		
		log.info("====== Method : sendMsgToProducer ======");
		log.info("Producer로 보낼 EndPoint & 메시지 : '{}' / {}",towhere,jsonString);
		
		WebClient webClient = WebClient.builder().baseUrl(domain+":"+port).build();

	    String endpointUrl = towhere;  
	    
	    webClient.post()
	            .uri(endpointUrl)
	            .body(BodyInserters.fromValue(jsonString))
	            .retrieve()
	            .onStatus(
	                    status -> status.is4xxClientError() || status.is5xxServerError(),
	                    response -> response.bodyToMono(String.class).flatMap(body -> {
	                        return Mono.error(new RuntimeException("HTTP error: " + response.statusCode() + ", " + body));
	                    })
	                )
	            .bodyToMono(String.class)
	            .doOnError(error -> {
	            }).onErrorResume(e -> {
	            	 return Mono.empty();
				})
	            .subscribe(responseBody -> {
	            });
	    
	}
	
	

}
