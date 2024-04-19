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

import gc.apiClient.customproperties.CustomProperties;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientApp {

	private static String CLIENT_ID = "";
	private static String CLIENT_SECRET = "";
	private static String API_BASE_URL = "";
	private static String API_END_POINT = "";
	private static String HTTP_METHOD = "";
	private static String accessToken = "";
	private static int index = 0;
	private static String[] tokenlist = new String[15] ;

	private WebClient webClient;

	public WebClientApp(String apiName, String httpMethod) {// WebClinet 생성자, 기본적인 초기 설정들.
		// WebClient를 사용하기 위한 기본 설정들과 매개변수로 온 api를 사용하기 위한 기본 설정들.

		// 암호화된 id, 비밀번호 db에서 가져오는 작업.
//		WebClientConfig webClientConfig = new WebClientConfig(servicedb);
//      webClientConfig.getClientIdPwd();
		CLIENT_ID = WebClientConfig.getClientId();
		log.info("Client Id : {}", CLIENT_ID);
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		log.info("Client secret : {}", CLIENT_SECRET);
		API_BASE_URL = WebClientConfig.getBaseUrl();
		API_END_POINT = WebClientConfig.getApiEndpoint(apiName);
		HTTP_METHOD = httpMethod;

		if (tokenlist[index] == null||tokenlist[index].equals("")) {
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
		this.webClient = WebClient.builder().exchangeStrategies(exchangeStrategies).baseUrl(API_BASE_URL)
				.defaultHeader("Accept", "application/json").defaultHeader("Content-Type", "application/json")
				.defaultHeader("Authorization", "Bearer " + accessToken).build();
	}

	public WebClientApp() {

	}

	
	public synchronized void getAccessToken(int index) {
		String region = "ap_northeast_2"; // Consider making this configurable

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(PureCloudRegionHosts.valueOf(region)).build();

		try {
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			String newAccessToken = authResponse.getBody().getAccess_token();
			synchronized (this) {
				accessToken = newAccessToken;
				tokenlist[index] = newAccessToken;
			}
			log.info("Access token refreshed successfully.");
		} catch (Exception e) {
			log.error("Error occurred during access token refresh: {}", e.getMessage(), e);
		}

	}

	public Mono<String> makeApiRequestAsync() {

		RequestHeadersUriSpec<?> requestSpec = null;

		if (HTTP_METHOD.equals("GET")) {// http method설정.
			requestSpec = webClient.get();
		} else {
			requestSpec = webClient.post();
		}

		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, "87dde849-5710-4470-8a00-5e94c679e703");// 첫번째

		return requestSpec.uri(api1.toUriString()).retrieve().bodyToMono(String.class).onErrorResume(error -> {
			log.error("Error making API request: {}", error.getMessage());
			return Mono.empty();
		});
	}

	public String makeApiRequest(Object... param) {
		RequestHeadersUriSpec<?> requestSpec = null;

		if (HTTP_METHOD.equals("GET")) {
			requestSpec = webClient.get();
		} else {
			requestSpec = webClient.post();
		}

		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, param);

		return requestSpec.uri(api1.toUriString()).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public String makeApiRequest34(String contactListId, String msg) {
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT, contactListId);

		return webClient.post().uri(api1.toUriString()).body(BodyInserters.fromValue(msg)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public String makeApiRequest56(String contactListId, List<String> cskes) {

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
		
		for(int i = 0; i<15; i++) {
			tokenlist[i] = "";
		}
		index = 0;
	}

}
