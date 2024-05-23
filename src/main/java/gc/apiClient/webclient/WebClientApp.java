package gc.apiClient.webclient;

import java.util.List;

import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriComponents;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientApp {

	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	private static String API_BASE_URL = "";
	private static String accessToken = "";
	private static int index = 0;
	private static String[] tokenlist = new String[15];

	private static WebClient webClient;

	public WebClientApp() {

		CLIENT_ID = WebClientConfig.getClientId();
		log.info("Client Id : {}", CLIENT_ID);
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		log.info("Client secret : {}", CLIENT_SECRET);
		API_BASE_URL = WebClientConfig.getBaseUrl();

		checkToken();
	}

	public static synchronized void checkToken() {

		if (tokenlist[index] == null || tokenlist[index].equals("")) {
			log.info("토큰 없음");
			log.info("현재 인덱스 : {}", index);
			getAccessToken(index);
			index++;
			if (index % 15 == 0) {
				index = 0;
			}
			log.info("발급 후 현재 인덱스 : {}", index);
		} else {
			log.info("토큰 있음 : {}", tokenlist[index]);
			log.info("현재 인덱스 : {}", index);
			accessToken = tokenlist[index];
			index++;
			if (index % 15 == 0) {
				index = 0;
			}
		}

		int bufferSize = 1024 * 1024;
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
			clientCodecConfigurer.defaultCodecs().maxInMemorySize(bufferSize);
		}).build();

		log.info("발급 받은 토큰 : {}", accessToken);

		webClient = WebClient.builder().exchangeStrategies(exchangeStrategies).baseUrl(API_BASE_URL)
				.defaultHeader("Accept", "application/json").defaultHeader("Content-Type", "application/json")
				.defaultHeader("Authorization", "Bearer " + accessToken).build();
	}

	public static synchronized void getAccessToken(int index) {
		String region = "ap_northeast_2"; // Consider making this configurable

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(PureCloudRegionHosts.valueOf(region)).build();

		try {
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			String newAccessToken = authResponse.getBody().getAccess_token();
			accessToken = newAccessToken;
			tokenlist[index] = newAccessToken;

			log.info("Access token has been refreshed successfully.");
		} catch (Exception e) {
			log.error("Error occurred during access token refresh: {}", e.getMessage());
		}

	}

//	public Mono<String> makeApiRequestAsync() {
//
//		RequestHeadersUriSpec<?> requestSpec = null;
//
//		if (HTTP_METHOD.equals("GET")) {// http method설정.
//			requestSpec = webClient.get();
//		} else {
//			requestSpec = webClient.post();
//		}
//
//		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
//		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, "87dde849-5710-4470-8a00-5e94c679e703");// 첫번째
//
//		return requestSpec.uri(api1.toUriString()).retrieve().bodyToMono(String.class).onErrorResume(error -> {
//			log.error("Error making API request: {}", error.getMessage());
//			return Mono.empty();
//		});
//	}

	public String ApionlyfordelContacts(String endpoint, String httpmethod, Object... param) {
		RequestHeadersUriSpec<?> requestSpec = null;

		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);

		if (httpmethod.equals("GET")) {
			requestSpec = webClient.get();
		} else if (httpmethod.equals("POST")) {
			requestSpec = webClient.post();
		} else {
			requestSpec = webClient.delete();
		}

		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest1(API_END_POINT, param);

		return requestSpec.uri(api1.toUriString()).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public String makeApiRequest(String endpoint, String httpmethod, Object... param) {
		RequestHeadersUriSpec<?> requestSpec = null;

		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);

		if (httpmethod.equals("GET")) {
			requestSpec = webClient.get();
		} else if (httpmethod.equals("POST")) {
			requestSpec = webClient.post();
		} else {
			requestSpec = webClient.delete();
		}

		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api = apiRequestHandler.buildApiRequest(API_END_POINT, param);

		return requestSpec.uri(api.toUriString()).retrieve()  //세팅된 api 호출. 
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), //api호출 후 응답값으로 4나 5로 시작하는 에러 코드를 받았을 때. 즉, 호출했는데 비정상응답일 때.  
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));//exception 발생함으로써 에러 코드와 에러 메시지를 출력. 
						}))
				.bodyToMono(String.class).block();//정상 응답일때 결과 값 리턴. 
	}


	public String makeApiRequest34(String endpoint, String contactListId, String msg) {
		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, contactListId);

		return webClient.post().uri(api1.toUriString()).body(BodyInserters.fromValue(msg)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public String makeApiRequest56(String endpoint, String contactListId, List<String> cskes) {

		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, contactListId);

		return webClient.post().uri(api1.toUriString()).body(BodyInserters.fromValue(cskes)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public static void EmptyTockenlt() {

		for (int i = 0; i < 15; i++) {
			tokenlist[i] = "";
		}
		index = 0;
	}

}
