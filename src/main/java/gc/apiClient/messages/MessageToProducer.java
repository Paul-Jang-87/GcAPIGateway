package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gc.apiClient.AppConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
/**
 * webClient이용 카프카 서버로 메시지 보낼 때를 위한 클래스이다. 엄밀히 말하면 Producer App으로 보낸다. 직접적으로
 * 메시지를 보내는 건 'Producer App' 어디로 메시지를 보낼지(토픽명-towhere), 어떤 내용을
 * 보낼지(메시지-jsonString)를 매개변수로 전달한다.
 */
public class MessageToProducer {

	private String domain = AppConfig.getDomain();
	private String port = "8081";

	public void sendMsgToProducer(String towhere, String jsonString) {

		if (jsonString.equals("{}") || jsonString.equals("")) {
			log.info("카프카로 보낼 메시지가 없습니다.");
			return;
		} else {
			
			log.info("====== Method : sendMsgToProducer ======");
			log.info("Producer로 보낼 EndPoint & 메시지 : '{}' / {}", towhere, jsonString);

			WebClient webClient = WebClient.builder().baseUrl(domain + ":" + port).build();

			String endpointUrl = towhere;

			webClient.post().uri(endpointUrl).body(BodyInserters.fromValue(jsonString)).retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> response.bodyToMono(String.class).flatMap(body -> {
				return Mono.error(new RuntimeException("HTTP error: " + response.statusCode() + ", " + body));
			})).bodyToMono(String.class).doOnError(error -> {
			}).onErrorResume(e -> {
				return Mono.empty();
			}).subscribe(responseBody -> {
			});

		}

		

	}

}
