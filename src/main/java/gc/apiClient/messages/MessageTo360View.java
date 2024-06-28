package gc.apiClient.messages;


import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
public class MessageTo360View {

	public static WebClient webClient = null;
	public static ConnectionProvider connectionProvider = null;
	public static ReactorClientHttpConnector clientHttpConnector = null;

	public static void SendMsgTo360View(String towhere, String massage) {

		log.info("====== Method : SendMsgTo360View ======");

		String jsonString = massage;

		log.info("'{}' - Data : {}", towhere, jsonString);

		connectionProvider = ConnectionProvider.builder("myConnectionPool").maxConnections(100000)
				.pendingAcquireMaxCount(100000).build();
		clientHttpConnector = new ReactorClientHttpConnector(
				HttpClient.create(connectionProvider));

		webClient = WebClient.builder().clientConnector(clientHttpConnector).baseUrl("http://gckafka.lguplus.co.kr:8081")
				.defaultHeader("Accept", "application/json").defaultHeader("Content-Type", "application/json").build();

		String endpointUrl = "/360view/" + towhere;

		webClient.post()
				.uri(endpointUrl)
				.body(BodyInserters.fromValue(jsonString)).retrieve()
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
				}).subscribe(responseBody -> {
	            });

	}

}
