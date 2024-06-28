package gc.apiClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class GcClientApplication {

	public static void main(String[] args) {
		
		//최초 어플리케이션 실행 부분.
		SpringApplication.run(GcClientApplication.class, args);   
	}

}
