package gc.apiClient.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
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
/**
 * 
 * IVR쪽에서 그 쪽 담당자들이 만들어 놓은 쉐도우 테이블에 주기적(정확히는 트리거 발생시)으로 테이터를 쌓는다.
 * 360view 로직은 그 쉐도우 테이블들에 쌓인 데이터들을 주기적(배치 30초마다)으로 읽어와서 어떠한 조작 없이 바로 카프카 서버로 전송한다. 
 * 엄밀히 말하면 'Producer APP' 보낸다. 카프카 서버로 메시지 보내는 것은 전적으로 'Producer APP'이 담당.
 * 
 */
public class Controller360view {

    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
    private final InterfaceDBOracle serviceOracle;
    private final InterfaceMsgObjOrcl serviceMsgObjOrcl;

    public Controller360view(InterfaceDBPostgreSQL serviceDb, InterfaceDBOracle serviceOracle, InterfaceWebClient serviceWeb,InterfaceMsgObjOrcl serviceMsgObjOrcl) {
        this.serviceOracle = serviceOracle;
        this.serviceMsgObjOrcl = serviceMsgObjOrcl;
    }
    
    
    /** 
     * 
     * @param methodName
     * @return 없음.
     * 서비스 'SchedulerService'에 의해 실행된다. 
     * 360view의 16개 함수를 각각 스케줄 돌리는 대신 이 함수를 대표적으로 실행함으로써 16개 함수를 모두 스케줄 실행시킬 수 있다. 
     * 
     */
    public Mono<Void> invokeMethod(String methodName) {
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
            log.error("(invokeMethod) 매서드를 불러오는데 에러가 발생하셨습니다. {}: {}", methodName, e.getMessage());
            errorLogger.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    @Transactional
    public Mono<Void> msg360Datacall() {
        return processRecords("from_clcc_hmcepcalldt_message", Entity_DataCall.class, wrap(serviceMsgObjOrcl::dataCallMsg));
    }

    @Transactional
    public Mono<Void> msg360MDatacall() {
        return processRecords("from_clcc_mblcepcalldt_message", Entity_MDataCall.class, wrap(serviceMsgObjOrcl::dataCallMsg));
    }

    @Transactional
    public Mono<Void> msg360DataCallCustomer() {
        return processRecords("from_clcc_hmcepcalldtcust_message", Entity_DataCallCustomer.class, wrap(serviceMsgObjOrcl::dataCallCustomerMsg));
    }

    @Transactional
    public Mono<Void> msg360MDataCallCustomer() {
        return processRecords("from_clcc_mblcepcalldtcust_message", Entity_MDataCallCustomer.class, wrap(serviceMsgObjOrcl::dataCallCustomerMsg));
    }

    @Transactional
    public Mono<Void> msg360DataCallService() {
        return processRecords("from_clcc_hmcepcallsvccd_message", Entity_DataCallService.class, wrap(serviceMsgObjOrcl::dataCallService));
    }

    @Transactional
    public Mono<Void> msg360MDataCallService() {
        return processRecords("from_clcc_mblcepcallsvccd_message", Entity_MDataCallService.class, wrap(serviceMsgObjOrcl::dataCallService));
    }

    @Transactional
    public Mono<Void> msg360MstrsSvcCd() {
        return processRecords("from_clcc_hmcepcallmstrsvccd_message", Entity_MasterServiceCode.class, wrap(serviceMsgObjOrcl::mstrSvcCdMsg));
    }

    @Transactional
    public Mono<Void> msg360MMstrsSvcCd() {
        return processRecords("from_clcc_mblcepcallmstrsvccd_message", Entity_MMasterServiceCode.class, wrap(serviceMsgObjOrcl::mstrSvcCdMsg));
    }

    @Transactional
    public Mono<Void> msg360WaDataCall() {
        return processRecords("from_clcc_hmcepwacalldt_message", Entity_WaDataCall.class, wrap(serviceMsgObjOrcl::waDataCallMsg));
    }

    @Transactional
    public Mono<Void> msg360MWaDataCall() {
        return processRecords("from_clcc_mblcepwacalldt_message", Entity_MWaDataCall.class, wrap(serviceMsgObjOrcl::waDataCallMsg));
    }

    @Transactional
    public Mono<Void> msg360WaDataCallOptional() {
        return processRecords("from_clcc_hmcepwacallopt_message", Entity_WaDataCallOptional.class, wrap(serviceMsgObjOrcl::waDataCallOptionalMsg));
    }

    @Transactional
    public Mono<Void> msg360MWaDataCallOptional() {
        return processRecords("from_clcc_mblcepwacallopt_message", Entity_MWaDataCallOptional.class, wrap(serviceMsgObjOrcl::waDataCallOptionalMsg));
    }

    @Transactional
    public Mono<Void> msg360WaDataCallTrace() {
        return processRecords("from_clcc_hmcepwacalltr_message", Entity_WaDataCallTrace.class, wrap(serviceMsgObjOrcl::waDataCallTraceMsg));
    }

    @Transactional
    public Mono<Void> msg360MWaDataCallTrace() {
        return processRecords("from_clcc_mblcepwacalltr_message", Entity_MWaDataCallTrace.class, wrap(serviceMsgObjOrcl::waDataCallTraceMsg));
    }
    
    @Transactional
    public Mono<Void> msg360WaMTrCode() {
        return processRecords("from_clcc_hmcepwatrcd_message", Entity_WaMTracecode.class, wrap(serviceMsgObjOrcl::waMTraceCdMsg));
    }
    
    @Transactional
    public Mono<Void> msg360MWaMTrCode() {
        return processRecords("from_clcc_mblcepwatrcd_message", Entity_MWaMTracecode.class, wrap(serviceMsgObjOrcl::waMTraceCdMsg));
    }

    /**
     * 데이터베이스에서 레코드를 가져와 카프카 서버로 메시지 형태로 보낸 후 레코드를 테이블에서 삭제하는 프로세스를 구현한 매서드
     * @param <T>
     * @param topicId 처리할 레코드의 토픽 ID.
     * @param entityClass  처리할 엔티티 클래스.
     * @param messageFunction 엔티티를 처리할 함수.
     * @return 빈 Mono를 반환.
     */
    
    private <T> Mono<Void> processRecords(String topicId, Class<T> entityClass, BiFunction<T, String, String> messageFunction) {
        try {
            int numberOfRecords = serviceOracle.getRecordCount(topicId);
            log.info("({})의 레코드의 개수 : {}", entityClass.getSimpleName(), numberOfRecords);

            if (numberOfRecords > 0) {
                List<T> entityList = serviceOracle.getAll(entityClass);
                for (T entity : entityList) {
                    String crudType = (String) entityClass.getMethod("getCmd").invoke(entity);
                    int orderId = (int) entityClass.getMethod("getOrderid").invoke(entity);
                    MessageTo360View.sendMsgTo360View(topicId, messageFunction.apply(entity, crudType));
                    serviceOracle.deleteAll(entityClass, orderId);
                }
            }
        } catch (Exception e) {
            log.error("Error processing records for {}: {}", entityClass.getSimpleName(), e.getMessage());
            errorLogger.error(e.getMessage(), e);
        }
        return Mono.empty();
    }

    
    /**
     * 
     * 기본 BiFunction은 체크 예외를 던질 수 없기 때문에, 체크 예외를 던질 수 있는 BiFunctionWithException을 정의하고 이를 wrap 메서드를 통해 처리.
     * 내부적으로 BiFunctionWithException의 apply 메서드를 호출하고, 예외가 발생하면 RuntimeException으로 래핑하여 던집니다.
     * @param <T>
     * @param <U>
     * @param <R>
     * @param biFunction
     * @return 변환된 BiFunction 반환
     */
    private <T, U, R> BiFunction<T, U, R> wrap(BiFunctionWithException<T, U, R> biFunction) {   
        return (t, u) -> {
            try {
                return biFunction.apply(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    
    /**
     * 예외를 던질 수 있는 apply 메서드를 가진 함수형 인터페이스.
     * @param <T>
     * @param <U>
     * @param <R>
     */
    @FunctionalInterface
    public interface BiFunctionWithException<T, U, R> {
        R apply(T t, U u) throws Exception;//예외
    }
}
