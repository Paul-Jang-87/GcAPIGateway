package gc.apiClient.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.oracleH.*;
import gc.apiClient.entity.oracleM.*;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageTo360View;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Profile("oracleH")
public class Controller360view {

    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
    private final InterfaceDBOracle serviceOracle;
    private final InterfaceMsgObjOrcl serviceMsgObjOrcl;

    public Controller360view(InterfaceDBPostgreSQL serviceDb, InterfaceDBOracle serviceOracle,
                             InterfaceWebClient serviceWeb, CustomProperties customProperties, 
                             InterfaceMsgObjOrcl serviceMsgObjOrcl) {
        this.serviceOracle = serviceOracle;
        this.serviceMsgObjOrcl = serviceMsgObjOrcl;
    }
    
    @Scheduled(fixedRate = 30000)
    public void scheduledMethod() {
        String[] methods = {
            "Msg360Datacall", "Msg360DataCallCustomer", "Msg360DataCallService", "Msg360MDatacall",
            "Msg360MDataCallCustomer", "Msg360MDataCallService", "Msg360MMstrsSvcCd", "Msg360MstrsSvcCd",
            "Msg360MWaDataCall", "Msg360MWaDataCallOptional", "Msg360MWaDataCallTrace", "Msg360MWaMTrCode",
            "Msg360WaDataCall", "Msg360WaDataCallOptional", "Msg360WaDataCallTrace", "Msg360WaMTrCode"
        };
        for (String method : methods) {
            Mono.fromCallable(() -> invokeMethod(method))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        }
    }
    
    /** 
     * 
     * @param methodName
     * @return 없음.
     * 
     * Mono란 반응형 프로그래밍을 지원 하는 라이브러리 Reactor library에서 Publisher를 상속 받은 객체이다. 
     * 반응형 프로그래밍에서 아이템을 Mono객체 1개당 1개의 아이템만 다룰 수 있다. 여기서는 void타입으로서 아무런 아이템도 emit하지 않는다.  
     */
    private Mono<Void> invokeMethod(String methodName) {
        try {
            //매서드 이름 즉'methodName'로 들어온 값을 가지고 현재 이 함수 'invokeMethod'가 정의 된 곳의 클래스에서 'methodName'의 값과 매칭되는 함수를 반환한다.  
            Method method = getClass().getDeclaredMethod(methodName);
            //method로 대표되는 현재의 매서드를 실행시킨다. 
            Object result = method.invoke(this);
            if (result instanceof Mono) {
                return (Mono<Void>) result;
            } else {
                throw new ClassCastException("매서드의 결과가 (" + methodName + ") Mono<Void> 타입이 아닙니다.");
            }
        } catch (Exception e) {
            log.error("매서드를 불러오는데 에러가 발생하셨습니다. {}: {}", methodName, e.getMessage());
            errorLogger.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    @Transactional
    public Mono<Void> Msg360Datacall() {
        return processRecords("from_clcc_hmcepcalldt_message", Entity_DataCall.class, wrap(serviceMsgObjOrcl::DataCallMsg));
    }

    @Transactional
    public Mono<Void> Msg360MDatacall() {
        return processRecords("from_clcc_mblcepcalldt_message", Entity_MDataCall.class, wrap(serviceMsgObjOrcl::DataCallMsg));
    }

    @Transactional
    public Mono<Void> Msg360DataCallCustomer() {
        return processRecords("from_clcc_hmcepcalldtcust_message", Entity_DataCallCustomer.class, wrap(serviceMsgObjOrcl::DataCallCustomerMsg));
    }

    @Transactional
    public Mono<Void> Msg360MDataCallCustomer() {
        return processRecords("from_clcc_mblcepcalldtcust_message", Entity_MDataCallCustomer.class, wrap(serviceMsgObjOrcl::DataCallCustomerMsg));
    }

    @Transactional
    public Mono<Void> Msg360DataCallService() {
        return processRecords("from_clcc_hmcepcallsvccd_message", Entity_DataCallService.class, wrap(serviceMsgObjOrcl::DataCallService));
    }

    @Transactional
    public Mono<Void> Msg360MDataCallService() {
        return processRecords("from_clcc_mblcepcallsvccd_message", Entity_MDataCallService.class, wrap(serviceMsgObjOrcl::DataCallService));
    }

    @Transactional
    public Mono<Void> Msg360MstrsSvcCd() {
        return processRecords("from_clcc_hmcepcallmstrsvccd_message", Entity_MasterServiceCode.class, wrap(serviceMsgObjOrcl::MstrSvcCdMsg));
    }

    @Transactional
    public Mono<Void> Msg360MMstrsSvcCd() {
        return processRecords("from_clcc_mblcepcallmstrsvccd_message", Entity_MMasterServiceCode.class, wrap(serviceMsgObjOrcl::MstrSvcCdMsg));
    }

    @Transactional
    public Mono<Void> Msg360WaDataCall() {
        return processRecords("from_clcc_hmcepwacalldt_message", Entity_WaDataCall.class, wrap(serviceMsgObjOrcl::WaDataCallMsg));
    }

    @Transactional
    public Mono<Void> Msg360MWaDataCall() {
        return processRecords("from_clcc_mblcepwacalldt_message", Entity_MWaDataCall.class, wrap(serviceMsgObjOrcl::WaDataCallMsg));
    }

    @Transactional
    public Mono<Void> Msg360WaDataCallOptional() {
        return processRecords("from_clcc_hmcepwacallopt_message", Entity_WaDataCallOptional.class, wrap(serviceMsgObjOrcl::WaDataCallOptionalMsg));
    }

    @Transactional
    public Mono<Void> Msg360MWaDataCallOptional() {
        return processRecords("from_clcc_mblcepwacallopt_message", Entity_MWaDataCallOptional.class, wrap(serviceMsgObjOrcl::WaDataCallOptionalMsg));
    }

    @Transactional
    public Mono<Void> Msg360WaDataCallTrace() {
        return processRecords("from_clcc_hmcepwacalltr_message", Entity_WaDataCallTrace.class, wrap(serviceMsgObjOrcl::WaDataCallTraceMsg));
    }

    @Transactional
    public Mono<Void> Msg360MWaDataCallTrace() {
        return processRecords("from_clcc_mblcepwacalltr_message", Entity_MWaDataCallTrace.class, wrap(serviceMsgObjOrcl::WaDataCallTraceMsg));
    }
    
    @Transactional
    public Mono<Void> Msg360WaMTrCode() {
        return processRecords("from_clcc_hmcepwatrcd_message", Entity_WaMTracecode.class, wrap(serviceMsgObjOrcl::WaMTraceCdMsg));
    }
    
    @Transactional
    public Mono<Void> Msg360MWaMTrCode() {
        return processRecords("from_clcc_mblcepwatrcd_message", Entity_MWaMTracecode.class, wrap(serviceMsgObjOrcl::WaMTraceCdMsg));
    }

    private <T> Mono<Void> processRecords(String topicId, Class<T> entityClass, BiFunction<T, String, String> messageFunction) {
        try {
            int numberOfRecords = serviceOracle.getRecordCount(topicId);
            log.info("({})의 레코드의 개수 : {}", entityClass.getSimpleName(), numberOfRecords);

            if (numberOfRecords > 0) {
                List<T> entityList = serviceOracle.getAll(entityClass);
                for (T entity : entityList) {
                    String crudType = (String) entityClass.getMethod("getCmd").invoke(entity);
                    int orderId = (int) entityClass.getMethod("getOrderid").invoke(entity);
                    MessageTo360View.SendMsgTo360View(topicId, messageFunction.apply(entity, crudType));
                    serviceOracle.deleteAll(entityClass, orderId);
                }
            }
        } catch (Exception e) {
            log.error("Error processing records for {}: {}", entityClass.getSimpleName(), e.getMessage());
            errorLogger.error(e.getMessage(), e);
        }
        return Mono.empty();
    }

    // Wrapper method to handle exceptions in BiFunction
    private <T, U, R> BiFunction<T, U, R> wrap(BiFunctionWithException<T, U, R> biFunction) {
        return (t, u) -> {
            try {
                return biFunction.apply(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface BiFunctionWithException<T, U, R> {
        R apply(T t, U u) throws Exception;
    }
}
