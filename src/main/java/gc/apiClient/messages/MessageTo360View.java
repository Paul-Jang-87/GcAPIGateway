package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageTo360View {

	public static void sendMsgTo360View(String towhere,String key, String massage) {

			String jsonString = massage;
			log.info("JsonString Data : {}", jsonString);

			log.info("===== To360View =====");

			WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

			String endpointUrl = "/360view/" + towhere+ "/" +key;

			log.info("Endpoint : {}", endpointUrl);

			
			webClient.post().uri(endpointUrl).body(BodyInserters.fromValue(jsonString)).retrieve()
					.bodyToMono(String.class).onErrorResume(error -> {
						log.error("Error making API request: {}", error.getMessage());
						return Mono.empty();
					}).block(); // Wait for the result

			log.info("Entity as JSON: {}", jsonString);


	}

}
