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


/**
 * 
 * 제네시스의 api를 호출하기 위해 필요한 webclient 객체를 만들어주는 클래스
 *   
 */
@Slf4j
public class WebClientApp {

	private static String CLIENT_ID = ""; //제네시스 OAuth 아이디
	private static String CLIENT_SECRET = "";//제네시스 OAuth Secret
	private static String API_BASE_URL = ""; //제네시스 도메인
	private static String accessToken = ""; // 제네시스 api 호출을 위한 인증 토큰.
	
	/**
	 * 제네시스 api사용 설명을 보면 한 토큰당 1분에 api를 호출 할 수 있는 최대 횟수가 정해져있다. 
	 * 예를 들어 한개의 토큰으로 api를 1분에 300번만 호출할 수 있다. 그 이상은 호출 할 수 없고 1분을 기다려야한다.
	 * 때문에 api를 호출할 때 한 토큰만 사용하지 않고 15개의 토큰을 한번씩 번갈아가면서 쓰기로 정했다.
	 * 그러나 매번 토큰을 발급받는 것은 제약도 있고(20개) 비효율적이기 떄문에 총 15개까지만 토큰을 발급받고 돌려쓰기로 했다. 
	 * 
	 * 그것을 위한 변수 'index', 배열변수 'tokenlist'이다.
	 *  
	 */
	private static int index = 0;
	private static String[] tokenlist = new String[15];

	private static WebClient webClient;

	public WebClientApp() {

		CLIENT_ID = WebClientConfig.getClientId();
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		API_BASE_URL = WebClientConfig.getBaseUrl();

		checkToken();
	}
	

	
	/**
	 * 토큰을 발급받고 관리하는 함수.
	 */
	public static synchronized void checkToken() {

		/**
		 * 어플리케이션이 최초로 올라가면 배열'tokenlist'이 비어있기 때문에 당연히 어떠한 토큰도 없다. 
		 * 15개가 채워질때까지 이쪽 if구간을 타고 15개가 차면 배열'tokenlist'이 하루에 한번 비워지는 것으로 초기화 될때까지 else문을 탄다.
		 */
		if (tokenlist[index] == null || tokenlist[index].equals("")) {
//			log.info("토큰 없음");
//			log.info("현재 인덱스 : {}", index);
			getAccessToken(index);
			index++;
			if (index % 15 == 0) {
				index = 0;
			}
//			log.info("발급 후 현재 인덱스 : {}", index);
		} else {
//			log.info("토큰 있음 : {}", tokenlist[index]);
//			log.info("현재 인덱스 : {}", index);
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

//		log.info("발급 받은 토큰 : {}", accessToken);

		webClient = WebClient.builder().exchangeStrategies(exchangeStrategies).baseUrl(API_BASE_URL)
				.defaultHeader("Accept", "application/json").defaultHeader("Content-Type", "application/json")
				.defaultHeader("Authorization", "Bearer " + accessToken).build();
	}
	

	/**
	 * 토큰 발급 받는 실질적 함수.
	 * @param index 'tokenlist' 배열의 인덱스. 토큰을 발급받은 후 배열'tokenlist'에 해당 인덱스에 발급받은 토큰을 저장한다. 
	 */
	public static synchronized void getAccessToken(int index) {
		String region = "ap_northeast_2"; // Consider making this configurable

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(PureCloudRegionHosts.valueOf(region)).build();

		try {
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			String newAccessToken = authResponse.getBody().getAccess_token();
			accessToken = newAccessToken;
			tokenlist[index] = newAccessToken;

//			log.info("Access token이 성공적으로 발급되었습니다.");
		} catch (Exception e) {
			log.error("토큰 발급 과정에서 에러가 발생했습니다 : {}", e.getMessage());
		}

	}

	
	/**
	 * 아래 2개의 함수들이 첫번째 두번째 파라미터들은 공통적인 내용이다. 
	 * 'WebClientConfig'클래스의 'getApiEndpoint'메서드 참조.
	 * 
	 * @param endpoint 어떤 endpoint로 어떤 api를 호출 할지
	 * @param httpmethod 어떤 http메서트로 호출할지 POST,GET,DELETE,PUT 등등...
	 * @param param path 파라미터나 Query 파라미터가 있는 경우.
	 * @return
	 */
	

	public String apionlyfordelContacts(String endpoint, String httpmethod, Object... param) {
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
		UriComponents api = apiRequestHandler.apiReqForContacts(API_END_POINT, param);

		return requestSpec.uri(api.toUriString()).retrieve()
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

	
	/**
	 * 아래 2개의 함수들은 발신대상자 관련 api를 호출하기 위한 함수이다. 세번째 인자로 쿼리파라미터가 아닌 request body값이 들어간다. 
	 * 
	 * 
	 * @param endpoint endpoint 어떤 endpoint로 어떤 api를 호출 할지??? 'WebClientConfig'클래스의 'getApiEndpoint'메서드 참조.
	 * @param contactListId path 파라미터 컨텍리스트아이디가 들어간다. 아래 두 함수에 대한 자세한 내용은 'ServiceWebClient'서비스 참조.
	 * @param msg
	 * @return
	 */

	public String apiReqPushContacts(String endpoint, String contactListId, String msg) {
		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api = apiRequestHandler.buildApiRequest(API_END_POINT, contactListId);

		return webClient.post().uri(api.toUriString()).body(BodyInserters.fromValue(msg)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	public String apiReqGetRtOfContacts(String endpoint, String contactListId, List<String> cskes) {

		String API_END_POINT = WebClientConfig.getApiEndpoint(endpoint);
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api = apiRequestHandler.buildApiRequest(API_END_POINT, contactListId);

		return webClient.post().uri(api.toUriString()).body(BodyInserters.fromValue(cskes)).retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						response -> response.bodyToMono(String.class).flatMap(errorBody -> {
							return Mono.error(new RuntimeException("Error: " + errorBody));
						}))
				.bodyToMono(String.class).block();
	}

	
	//'tokenlist'배열을 초기화 시켜주는 함수. 프로젝트의 모든 스케줄을 관리해주는 SchedulerService에서 24시간마다 1번씩 이 함수를 실행시켜 배열을 초기화 시켜준다.  
	public static void EmptyTockenlt() {

		for (int i = 0; i < 15; i++) {
			tokenlist[i] = "";
		}
		index = 0;
	}

}
