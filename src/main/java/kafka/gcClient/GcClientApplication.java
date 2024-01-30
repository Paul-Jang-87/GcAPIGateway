package kafka.gcClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GcClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcClientApplication.class, args);
	}

}
