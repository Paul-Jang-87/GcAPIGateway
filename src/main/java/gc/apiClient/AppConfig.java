package gc.apiClient;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
//@PropertySource("file:./logs/gc_config/gcapi_info.properties") 
/**
 * 
 * 도메인 정보와 제네시스 OAuth를 위한 Id와 Secret 정보를 어플리케이션 외부에서 관리를 하고 있는데 외부 파일에서 그 정보를 읽어 오는 클래스이다.
 * 메시지를 다른 앱('Producer'앱이나 'APIM'앱)으로 api호출을 통해 보낼 때가 있다. 그 앱들의 도메인 정보가 'gcapi_info.properties'파일에 담겨있다. 
 * 도메인은 'https://gckafka.lguplus.co.kr'
 */
public class AppConfig {
/*	
	@Value("${domain}")
	private String config_domian;
	@Value("${gc.client.id}")	
	private String config_oauth_id;
	@Value("${gc.client.secret}")	
	private String config_oauth_secret;
*/	
	private static String domain;
	private static String id;			// Genesys Cloud API OAuth ID
	private static String secret;		// Genesys Cloud API OAuth SECRET
	
	public AppConfig() throws Exception {
		
		//제네시스 api를 호출 하기 위한 OAuth ID와 OAuth SECRET 정보, 다른 앱의 api를 호출하기 위한 도메인 정보들이 들어 있는 gcapi_info.properties 파일 경로
		ResourcePropertySource rps = new ResourcePropertySource("file:./logs/gc_config/gcapi_info.properties");
		
		domain 	= String.valueOf(rps.getProperty("domain"));
		id 		= String.valueOf(rps.getProperty("gc.client.id"));
		secret 	= String.valueOf(rps.getProperty("gc.client.secret"));
		
		log.info("domain : {}", domain);
		log.info("id : {}", id);
		log.info("secret : {}", secret);
	}

	public static String getDomain() {
		return domain;
	}

	public static String getId() {
		return id;
	}

	public static String getSecret() {
		return secret;
	}
	
}
