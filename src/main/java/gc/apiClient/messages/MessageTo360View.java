package gc.apiClient.messages;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gc.apiClient.AppConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
/**
 * webClient이용 360View(으)로 메시지 보낼 때를 위한 클래스이다. 엄밀히 말하면 Producer App으로 보낸다.
 * 카프카서버로 직접적으로 메시지를 보내는 건 'Producer App' 어디로 메시지를 보낼지(토픽명-towhere), 어떤 내용을
 * 보낼지(메시지-massage)를 매개변수로 전달한다.
 */

public class MessageTo360View {

	private static String domain = AppConfig.getDomain();;
	private static String port = "8081";

	public static WebClient webClient = null;
	public static ConnectionProvider connectionProvider = null;
	public static ReactorClientHttpConnector clientHttpConnector = null;

	public static void sendMsgTo360View(String towhere, String massage) {

		if (massage.equals("{}") || massage.equals("")) {
			log.info("카프카로 보낼 메시지가 없습니다.");
			return;
		}else {
			log.info("====== Method : sendMsgTo360View ======");

			String jsonString = massage;

			log.info("'{}' - Data : {}", towhere, jsonString);

			connectionProvider = ConnectionProvider.builder("myConnectionPool").maxConnections(100000).pendingAcquireMaxCount(100000).build();
			clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));

			webClient = WebClient.builder().clientConnector(clientHttpConnector).baseUrl(domain + ":" + port).defaultHeader("Accept", "application/json").defaultHeader("Content-Type", "application/json").build();

			String endpointUrl = "/360view/" + towhere;

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
