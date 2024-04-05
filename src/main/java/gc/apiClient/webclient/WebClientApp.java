package gc.apiClient.webclient;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
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
	private static String API_END_POINT = "";
	private static String HTTP_METHOD = "";
	private static String accessToken = "";
	
	private WebClient webClient;

	public WebClientApp(String apiName, String httpMethod) {// WebClinet 생성자, 기본적인 초기 설정들.
										 // WebClient를 사용하기 위한 기본 설정들과 매개변수로 온 api를 사용하기 위한 기본 설정들.
		
		//암호화된 id, 비밀번호 db에서 가져오는 작업.  
//		WebClientConfig webClientConfig = new WebClientConfig(servicedb);
//      webClientConfig.getClientIdPwd();
        
		CLIENT_ID = WebClientConfig.getClientId();
		log.info("Client Id : {}",CLIENT_ID);
		CLIENT_SECRET = WebClientConfig.getClientSecret();
		log.info("Client secret : {}",CLIENT_SECRET);
		API_BASE_URL = WebClientConfig.getBaseUrl();
		API_END_POINT = WebClientConfig.getApiEndpoint(apiName);
		HTTP_METHOD = httpMethod;
		if (accessToken.equals("")) {
			getAccessToken();
		}else {
			log.info("토큰 있음 : {}",accessToken);
		}
		this.webClient = WebClient.builder().baseUrl(API_BASE_URL)
				.defaultHeader("Accept", "application/json")
				.defaultHeader("Content-Type", "application/json")
				.defaultHeader("Authorization", "Bearer " + accessToken).build();
	}

	// OAuth access token 유효기간 86400초 (24시간)
	// 24시간 마다 token 다시 받아오게끔 스케쥴링
	@Scheduled(fixedDelay=86400*1000)
	private void getAccessToken() {
		// Replace the region with your desired one
		String region = "ap_northeast_2";

		ApiClient apiClient = ApiClient.Builder.standard().withBasePath(PureCloudRegionHosts.valueOf(region)).build();

		try {
			ApiResponse<AuthResponse> authResponse = apiClient.authorizeClientCredentials(CLIENT_ID, CLIENT_SECRET);
			accessToken = authResponse.getBody().getAccess_token();
			log.info("accessToken : {}",accessToken);
		} catch (Exception e) {
			log.error("Error is occured during getting AccessToken: {}",e.getMessage());
			e.printStackTrace();
		}
	}

	public Mono<String> makeApiRequestAsync() {
		
		RequestHeadersUriSpec<?> requestSpec = null;
		
		if(HTTP_METHOD.equals("GET")) {//http method설정. 
			requestSpec = webClient.get();
		}else {
			requestSpec = webClient.post();
		}
		
		ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
		UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT,"87dde849-5710-4470-8a00-5e94c679e703");//첫번째 인자는 api endpoint이기 때문데 무조건 필요.
																													 //두번째 인자부터는 path parameter or query parameter. 
																													 //두번째 인자는 있어도 되고 없어도 됨. 

	    return requestSpec
				.uri(api1.toUriString())
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(error -> {
			log.error("Error making API request: {}",error.getMessage());
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
	    UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT,param);

	    return requestSpec
	            .uri(api1.toUriString())
	            .retrieve()
	            .bodyToMono(String.class)
	            .onErrorResume(error -> {
	                log.error("Error making API request: {}",error.getMessage()) ;
	                return Mono.empty();
	            })
	            .block(); // Wait for the result
	}
	
	
	public String makeApiRequest34(String contactListId, String msg) {

	    ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
	    UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT,contactListId);

	    return webClient.post().uri(api1.toUriString()).body(BodyInserters.fromValue(msg)).retrieve()
				.bodyToMono(String.class)
	            .onErrorResume(error -> {
	            	log.error("Error making API request: {}",error.getMessage());
	                return Mono.empty();
	            })
	            .block(); // Wait for the result
	}
	
	
	public String makeApiRequest56(String contactListId, List<String> cskes) {

	    ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
	    UriComponents api1 = apiRequestHandler.buildApiRequest(API_END_POINT,contactListId);

	    return webClient.post().uri(api1.toUriString()).body(BodyInserters.fromValue(cskes)).retrieve()
				.bodyToMono(String.class)
	            .onErrorResume(error -> {
	                log.error("Error making API request: {}",error.getMessage());
	                return Mono.empty();
	            })
	            .block(); // Wait for the result
	}
	

}
