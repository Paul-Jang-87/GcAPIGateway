package gc.apiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import gc.apiClient.controller.Controller360view;
import gc.apiClient.controller.ControllerCallBot;
import gc.apiClient.controller.ControllerCenter;
import gc.apiClient.controller.ControllerUCRM;
import gc.apiClient.webclient.WebClientApp;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 프로젝트 내에서 배치관련 스케줄러들만 모아둔 클래스. 
 */
@Service 
public class SchedulerService {

	@Autowired
	private Controller360view controller360viewService; //360view 컨트롤러의 의존성 주입.
	@Autowired
	private ControllerCallBot callbotService; //콜봇 컨트롤러의 의존성 주입.
	@Autowired
	private ControllerCenter etcService;  //Center 컨트롤러의 의존성 주입. => Center 컨트롤러는 특정한 비즈니스 로직에 속하지 않고 다른 컨트롤러들과 조금씩 연관이 있다. ex)캠페인 매니저, 토큰 등등. apim은 다른 로직등과 연관 없이 독립적이지만 비중이 크지 않기에 Center컨트롤러에 포함시켰다.
	@Autowired
	private ControllerUCRM ucrmService; //UCRM 컨트롤러의 의존성 주입. 
	
	
	
	
	
	
	//360view 관련 스케줄러
	@Scheduled(fixedRate = 30000)
	public void scheduledMethod() { 
		String[] methods = {
	            "msg360Datacall", "msg360DataCallCustomer", "msg360DataCallService", "msg360MDatacall",
	            "msg360MDataCallCustomer", "msg360MDataCallService", "msg360MMstrsSvcCd", "msg360MstrsSvcCd",
	            "msg360MWaDataCall", "msg360MWaDataCallOptional", "msg360MWaDataCallTrace", "msg360MWaMTrCode",
	            "msg360WaDataCall", "msg360WaDataCallOptional", "msg360WaDataCallTrace", "msg360WaMTrCode"
	        };
	        for (String method : methods) {
	            Mono.fromCallable(() -> controller360viewService.invokeMethod(method))
	                .subscribeOn(Schedulers.boundedElastic())
	                .subscribe();
	        }
		
	}
	
	
	
	
	//콜봇 관련 스케줄러
	@Scheduled(fixedRate = 60000) // 1분 간격으로 'SendCallBotRt' 비동기적으로 실행.
	public void callbot_scheduledMethod() {
		/*
		 * SendCallBotRt() 메서드를 비동기 방식 스케쥴링을 위한 메서드 Mono.fromCallable : 주어진 callable을
		 * 호출하고 그 결과를 발행하는 Mono를 생성 subscribeOn(Schedulers.boundedElastic() : 작업을 실행할
		 * 스케쥴러를 설정. (스레드풀 제공) subscribe() : 'Mono'를 구독하여 실제로 작업이 수행 됨.
		 */
		Mono.fromCallable(() -> callbotService.sendCallBotRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}
	
	
	
	
	
	//기타 관련 스케줄러 (캠페인 마스터, apim, 토큰)	
	@Scheduled(fixedRate = 86400 * 1000) // 24시간 마다 토큰 초기화.
	public void RefreshToken() {

		WebClientApp.EmptyTockenlt();

	}
	@Scheduled(fixedRate = 60000)
	public void etc_scheduledMethod() {// 1분 간격으로 안의 함수들 비동기적으로 실행

		Mono.fromCallable(() -> etcService.receiveMessage("campma")).subscribeOn(Schedulers.boundedElastic()).subscribe();
		Mono.fromCallable(() -> etcService.sendApimRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}
	
	
	
	
	
	//ucrm관련 스케줄러
	@Scheduled(fixedRate = 10000) // 5초 간격으로 함수 'UcrmMsgFrmCnsmer' 스케줄 돌림.
	public void ucrmContactlt() {
		Mono.fromCallable(() -> ucrmService.ucrmMsgFrmCnsmer()).subscribeOn(Schedulers.boundedElastic()).subscribe();
		
	}
	@Scheduled(fixedRate = 60000) // 1분 간격으로 함수 'SendUcrmRt' 스케줄 돌림.
	public void ucrm_scheduledMethod() {

		Mono.fromCallable(() -> ucrmService.sendUcrmRt()).subscribeOn(Schedulers.boundedElastic()).subscribe();

	}
	

}
