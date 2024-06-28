package gc.apiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogArchiver {

	 //@Scheduled(cron = "0 0 0 * * *") //하루 한번 정각에 
	// @Scheduled(cron = "0 */3 * * * *") // Every 3 minutes
	// @Scheduled(cron = "0 * * * * *") // Every minute
	// @Scheduled(cron = "*/5 * * * * *") // Every 5second
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

	@Scheduled(cron = "0 5 0 * * *") // 매일 12:05에 실행
	public void archiveLogs() {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("bash", "/logs/archive_logs.sh");
			processBuilder.directory(new File(System.getProperty("user.dir"))); // Set working directory
			Process process = processBuilder.start();
			process.waitFor();
			System.out.println("Log archiving script executed successfully.");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Scheduled(cron = "0 3 0 * * *") // 매일 12:03에 실행
	public void startlogs() {
		
		SimpleDateFormat form = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		Date now = new Date();
		String nowtime = form.format(now);
		
		log.info("{}, apiClient 로그 시작",nowtime);
		log.info("{}, apiClient_error 로그 시작",nowtime);
		errorLogger.error("{}, apiClient_error 로그 시작",nowtime); 
	
	}

}
