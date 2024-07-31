package gc.apiClient.webclient;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
/**
 * 스케줄 테스크(배치)가 작동할수 있게 해주는 클래스
 */
public class SchedulerConfig implements SchedulingConfigurer {
	private final int POOL_SIZE = 10;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// Thread Pool 설정
		ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler(); 
		
		threadPool.setPoolSize(POOL_SIZE);
		threadPool.setThreadNamePrefix("test-task-scheduling");
		threadPool.initialize();
        
		taskRegistrar.setTaskScheduler(threadPool);
	}
	
}
