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

		log.info(" ");
		log.info("====== ClassName : MessageTo360View & Method : SendMsgTo360View ======");

		String jsonString = massage;

		log.info("'{}' - Data : {}",towhere, jsonString);

		connectionProvider = ConnectionProvider.builder("myConnectionPool").maxConnections(100000)
				.pendingAcquireMaxCount(100000).build();
		clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));

		webClient = WebClient.builder()
				.clientConnector(clientHttpConnector)
				.baseUrl("http://localhost:8081")
				.defaultHeader("Accept", "application/json")
				.defaultHeader("Content-Type", "application/json").build();

		String endpointUrl = "/360view/" + towhere;

		Mono<String> result = webClient.post().uri(endpointUrl).body(BodyInserters.fromValue(jsonString)).retrieve()
				.bodyToMono(String.class).doOnError(error -> {
					log.error("Error making API request: {}", error.getMessage());
					error.printStackTrace();
				}).doOnSuccess(responseBody -> {
					log.info("Response received: {}", responseBody);
				}).doOnError(error -> {
					log.error("Error in handling response: {}", error.getMessage());
				}).doFinally(signal -> {
					log.info("Request completed successfully.");
				});

		// Subscribe to the Mono
		result.subscribe();

		log.info("====== End SendMsgTo360View ======");

	}

}
