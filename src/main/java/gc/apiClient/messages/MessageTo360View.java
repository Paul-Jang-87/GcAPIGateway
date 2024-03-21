package gc.apiClient.messages;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MessageTo360View {

	public static void SendMsgTo360View(String towhere, String key, String massage) {
		
		log.info("===== SendMsgTo360View =====");

		String jsonString = massage;
		
		log.info("JsonString Data : {}", jsonString);


		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build();

		String endpointUrl = "/360view/" + "firsttopic" + "/" + key;
//		String endpointUrl = "/360view/" + towhere + "/" + key;

		log.info("Endpoint : {}", endpointUrl);

		webClient.post().uri(endpointUrl).body(BodyInserters.fromValue(jsonString)).retrieve().bodyToMono(String.class)
				.onErrorResume(error -> {
					log.error("Error making API request: {}", error.getMessage());
					return Mono.empty();
				}).block(); // Wait for the result

		log.info("===== End SendMsgTo360View =====");

	}

	public static String ReturnKey(String topic, String crudtype) {
		
		log.info("===== ReturnKey =====");
		
		String key = "";
		String name = topic.split("_")[2];
		
		switch (crudtype) {
		case "INSERT": 
			
			key = name+"CreatedEvent";
			
			break;
			
		case "UPDATE":
			key = name+"UpdatedEvent";
			
			break;
			
		default:
			key = name+"DeletedEvent";
			break;
		}
		
		log.info("key : {}",key);
		log.info("===== End ReturnKey =====");
		
		return key;
	}

}
