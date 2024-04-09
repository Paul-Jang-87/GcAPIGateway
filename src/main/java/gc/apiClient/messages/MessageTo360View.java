package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageTo360View {

	public static void SendMsgTo360View(String towhere, String massage) {
		
		log.info(" ");
		log.info("====== ClassName : MessageTo360View & Method : SendMsgTo360View ======");

		String jsonString = massage;
		
		log.info("JsonString Data : {}", jsonString);


		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8081")
				.defaultHeader("Accept", "application/json")
				.defaultHeader("Content-Type", "application/json").build();

//		String endpointUrl = "/360view/" + "firsttopic";
		String endpointUrl = "/360view/" + towhere;

		log.info("Endpoint : {}", endpointUrl);

		webClient.post().uri(endpointUrl).body(BodyInserters.fromValue(jsonString)).retrieve().bodyToMono(String.class)
		.doOnError(error -> {
            log.error("Error making API request: {}", error.getMessage());
            error.printStackTrace();
        })
        .subscribe(responseBody -> {
            log.info("Response received: {}", responseBody);
        }, error -> {
            log.error("Error in handling response: {}", error.getMessage());
        }, () -> {
            log.info("Request completed successfully.");
        });

		log.info("====== End SendMsgTo360View ======");

	}

}
