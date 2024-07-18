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
