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
		log.info("Producer로 보낼 EndPoint & 메시지 : '{}' / {}",towhere,jsonString);
		
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

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
	                log.error("API로 요청을 보내는 과정에서 에러가 발생했습니다. : {}", error.getMessage());
	            }).onErrorResume(e -> {
	            	 log.error("카프카 프로듀서 APP에서 받은 에러 메시지 : {}", e.getMessage());
	            	 return Mono.empty();
				})
	            .subscribe(responseBody -> {
	                log.info("카프카 프로듀서로 부터 받은 응답 메시지 : {}", responseBody);
	            });
	    
		log.info("====== End sendMsgToProducer ======");
	}
	
	

}
