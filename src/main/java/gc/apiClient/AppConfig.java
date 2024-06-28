package gc.apiClient;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
//@PropertySource("file:./logs/gc_config/gcapi_info.properties") 
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
