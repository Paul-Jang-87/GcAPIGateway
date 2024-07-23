package gc.apiClient.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
//@Slf4j
/**
 * 트랜젝션은 분리하기 위해 만든 클래스. 
 * ucrm로직과 callbot 로직에는 발신 결과를 'CAMPRT'테이블에 적재하는 로직이 존재한다.
 * 두개의 비지니스 로직(ucrm, callbot)이 3개의 팟에서 비동기적으로 하나에 테이블에 데이터를 적재를 하는데 적재하기 전 먼제 테이블 'CAMPRT'을 조회하여 'rlsq'속성의 최고 값을 가지고 온 후 
 * 그 값에 +1을 하여 새로운 데이터를 적재를 한다 그 과정(비동기적으로 하나에 테이블에 데이터를 적재를 하는)에서 한 레코드에 여러 트랜잭션이 동시에 접근하는 문제가 생겼고 
 * 비관적 락을 걸어 동시성 문제를 해결하고자 db인서트 부분만 따로 트렌잭션관리를 하기로 했다. 
 * 
 */
public class ServiceInstCmpRt {
	
	private final InterfaceDBPostgreSQL serviceDb;
	private final CreateEntity createEntity;
	
	public ServiceInstCmpRt(InterfaceDBPostgreSQL serviceDb,CreateEntity createEntity) {
		this.serviceDb = serviceDb;
		this.createEntity = createEntity;
	}
	
	@Transactional
	public void insrtCmpRt (JSONObject contactsresult, Entity_CampMa enCampMa) {
//		log.info("Transaction active insrtCmpRt: {}", TransactionSynchronizationManager.isActualTransactionActive());
		Entity_CampRt entityCmRt = createEntity.createCampRtMsg(contactsresult, enCampMa);// db 인서트 하기 위한 entity.
		try {
			serviceDb.insertCampRt(entityCmRt);
		} catch (Exception e) {
		}
	}
}
