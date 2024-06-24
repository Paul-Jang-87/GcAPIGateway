package gc.apiClient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_DataCallCustomer;
import gc.apiClient.entity.oracleH.Entity_DataCallService;
import gc.apiClient.entity.oracleH.Entity_MasterServiceCode;
import gc.apiClient.entity.oracleH.Entity_WaDataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;
import gc.apiClient.entity.oracleH.Entity_WaDataCallTrace;
import gc.apiClient.entity.oracleH.Entity_WaMTracecode;
import gc.apiClient.entity.oracleM.Entity_MDataCall;
import gc.apiClient.entity.oracleM.Entity_MDataCallCustomer;
import gc.apiClient.entity.oracleM.Entity_MDataCallService;
import gc.apiClient.entity.oracleM.Entity_MMasterServiceCode;
import gc.apiClient.entity.oracleM.Entity_MWaDataCall;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallTrace;
import gc.apiClient.entity.oracleM.Entity_MWaMTracecode;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.repository.oracleH.Repository_DataCall;
import gc.apiClient.repository.oracleH.Repository_DataCallCustomer;
import gc.apiClient.repository.oracleH.Repository_DataCallService;
import gc.apiClient.repository.oracleH.Repository_MasterServiceCode;
import gc.apiClient.repository.oracleH.Repository_WaDataCall;
import gc.apiClient.repository.oracleH.Repository_WaDataCallOptional;
import gc.apiClient.repository.oracleH.Repository_WaDataCallTrace;
import gc.apiClient.repository.oracleH.Repository_WaMTraceCode;
import gc.apiClient.repository.oracleM.Repository_MDataCall;
import gc.apiClient.repository.oracleM.Repository_MDataCallCustomer;
import gc.apiClient.repository.oracleM.Repository_MDataCallService;
import gc.apiClient.repository.oracleM.Repository_MMasterServiceCode;
import gc.apiClient.repository.oracleM.Repository_MWaDataCall;
import gc.apiClient.repository.oracleM.Repository_MWaDataCallOptional;
import gc.apiClient.repository.oracleM.Repository_MWaDataCallTrace;
import gc.apiClient.repository.oracleM.Repository_MWaMTraceCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("oracleH")
public class ServiceOracle implements InterfaceDBOracle {
	// 검색 **Create **Insert **Select
	private final Repository_DataCall repositoryDataCall;
	private final Repository_DataCallCustomer repositoryDataCallCustomer;
	private final Repository_DataCallService repositoryDataCallService;
	private final Repository_MasterServiceCode repositoryMasterServiceCode;
	private final Repository_MDataCall repositoryMDataCall;
	private final Repository_MDataCallCustomer repositoryMDataCallCustomer;
	private final Repository_MDataCallService repositoryMDataCallService;
	private final Repository_MMasterServiceCode repositoryMMasterServiceCode;
	private final Repository_MWaDataCall repositoryMWaDataCall;
	private final Repository_MWaDataCallOptional repositoryMWaDataCallOptional;
	private final Repository_MWaDataCallTrace repositoryMWaDataCallTrace;
	private final Repository_MWaMTraceCode repositoryMWaMTraceCode;
	private final Repository_WaDataCall repositoryWaDataCall;
	private final Repository_WaDataCallOptional repositoryWaDataCallOptional;
	private final Repository_WaDataCallTrace repositoryWaDataCallTrace;
	private final Repository_WaMTraceCode repositoryWaMTraceCode;

	public ServiceOracle(Repository_DataCall repositoryDataCall, Repository_DataCallCustomer repositoryDataCallCustomer,
			Repository_DataCallService repositoryDataCallService,
			Repository_MasterServiceCode repositoryMasterServiceCode, Repository_MDataCall repositoryMDataCall,
			Repository_MDataCallCustomer repositoryMDataCallCustomer,
			Repository_MDataCallService repositoryMDataCallService,
			Repository_MMasterServiceCode repositoryMMasterServiceCode, Repository_MWaDataCall repositoryMWaDataCall,
			Repository_MWaDataCallOptional repositoryMWaDataCallOptional,
			Repository_MWaDataCallTrace repositoryMWaDataCallTrace, Repository_MWaMTraceCode repositoryMWaMTraceCode,
			Repository_WaDataCall repositoryWaDataCall, Repository_WaDataCallOptional repositoryWaDataCallOptional,
			Repository_WaDataCallTrace repositoryWaDataCallTrace, Repository_WaMTraceCode repositoryWaMTraceCode) {

		this.repositoryDataCall = repositoryDataCall;
		this.repositoryDataCallCustomer = repositoryDataCallCustomer;
		this.repositoryDataCallService = repositoryDataCallService;
		this.repositoryMasterServiceCode = repositoryMasterServiceCode;
		this.repositoryMDataCall = repositoryMDataCall;
		this.repositoryMDataCallCustomer = repositoryMDataCallCustomer;
		this.repositoryMDataCallService = repositoryMDataCallService;
		this.repositoryMMasterServiceCode = repositoryMMasterServiceCode;
		this.repositoryMWaDataCall = repositoryMWaDataCall;
		this.repositoryMWaDataCallOptional = repositoryMWaDataCallOptional;
		this.repositoryMWaDataCallTrace = repositoryMWaDataCallTrace;
		this.repositoryMWaMTraceCode = repositoryMWaMTraceCode;
		this.repositoryWaDataCall = repositoryWaDataCall;
		this.repositoryWaDataCallOptional = repositoryWaDataCallOptional;
		this.repositoryWaDataCallTrace = repositoryWaDataCallTrace;
		this.repositoryWaMTraceCode = repositoryWaMTraceCode;
	}

	@PersistenceContext(unitName = "oracleH")
	private EntityManager entityManagerOracleH;

	@PersistenceContext(unitName = "oracleM")
	private EntityManager entityManagerOracleM;

	@PersistenceContext(unitName = "postgres")
	private EntityManager entityManagerPostgres;

	@Override
	public Entity_WaDataCallOptional findWaDataCallOptional(int wcseq) {

		try {
			Optional<Entity_WaDataCallOptional> optionalEntity = repositoryWaDataCallOptional.findById(wcseq);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_WaDataCallOptional by wcseq: {}", wcseq, ex);

			return null;
		}
	}

	@Override
	public Entity_DataCall findDataCallByCpid(int orderid) {

		try {
			Optional<Entity_DataCall> optionalEntity = repositoryDataCall.findById(orderid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_DataCall by id: {}", orderid, ex);

			return null;
		}
	}

	@Override
	public int getRecordCount(String topic_id) {

		switch (topic_id) {

		case "from_clcc_hmcepcalldt_message": {
			return repositoryDataCall.countBy();
		}
		case "from_clcc_mblcepcalldt_message": {
			return repositoryMDataCall.countBy();
		}
		case "from_clcc_hmcepcalldtcust_message": {
			return repositoryDataCallCustomer.countBy();
		}
		case "from_clcc_mblcepcalldtcust_message": {
			return repositoryMDataCallCustomer.countBy();
		}
		case "from_clcc_hmcepcallsvccd_message": {
			return repositoryDataCallService.countBy();
		}
		case "from_clcc_mblcepcallsvccd_message": {
			return repositoryMDataCallService.countBy();
		}
		case "from_clcc_hmcepcallmstrsvccd_message": {
			return repositoryMasterServiceCode.countBy();
		}
		case "from_clcc_mblcepcallmstrsvccd_message": {
			return repositoryMMasterServiceCode.countBy();
		}
		case "from_clcc_hmcepwacalldt_message": {
			return repositoryWaDataCall.countBy();
		}
		case "from_clcc_mblcepwacalldt_message": {
			return repositoryMWaDataCall.countBy();
		}
		case "from_clcc_hmcepwacallopt_message": {
			return repositoryWaDataCallOptional.countBy();
		}
		case "from_clcc_mblcepwacallopt_message": {
			return repositoryMWaDataCallOptional.countBy();
		}
		case "from_clcc_hmcepwacalltr_message": {
			return repositoryWaDataCallTrace.countBy();
		}
		case "from_clcc_mblcepwacalltr_message": {
			return repositoryMWaDataCallTrace.countBy();
		}
		case "from_clcc_hmcepwatrcd_message": {
			return repositoryWaMTraceCode.countBy();
		}
		case "from_clcc_mblcepwatrcd_message": {
			return repositoryMWaMTraceCode.countBy();
		}

		default:
			return 0;
		}
	}

	@Override
	public <T> List<T> getAll(Class<T> clazz) {

		EntityManager entityManagerToUse = null;

		try {

			if (Entity_DataCall.class.equals(clazz)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MDataCall.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_DataCallCustomer.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MDataCallCustomer.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_DataCallService.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MDataCallService.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_MasterServiceCode.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MMasterServiceCode.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_WaDataCall.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MWaDataCall.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_WaDataCallOptional.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MWaDataCallOptional.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_WaDataCallTrace.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MWaDataCallTrace.class)) {
				entityManagerToUse = entityManagerOracleM;
			} else if (clazz.isAssignableFrom(Entity_WaMTracecode.class)) {
				entityManagerToUse = entityManagerOracleH;
			} else if (clazz.isAssignableFrom(Entity_MWaMTracecode.class)) {
				entityManagerToUse = entityManagerOracleM;
			}

			CriteriaBuilder cb = entityManagerToUse.getCriteriaBuilder();
			jakarta.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> root = cq.from(clazz);
			cq.select(root);

//			int maxResults = 1000; // 해당 테이블에서 최대 1000개의 레코드만 가지고 온다.
//			return entityManagerToUse.createQuery(cq).setMaxResults(maxResults).getResultList();
			return entityManagerToUse.createQuery(cq).getResultList();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	

	@Override
	@Transactional
	public <T> void deleteAll(Class<T> clazz, int orderid) {
	    EntityManager entityManagerToUse = null;

	    log.info("들어옴. deleteAll 함수");

	    try {
	        if (Entity_DataCall.class.equals(clazz) || Entity_DataCallCustomer.class.equals(clazz)
	                || Entity_DataCallService.class.equals(clazz) || Entity_MasterServiceCode.class.equals(clazz)
	                || Entity_WaDataCall.class.equals(clazz) || Entity_WaDataCallOptional.class.equals(clazz)
	                || Entity_WaDataCallTrace.class.equals(clazz) || Entity_WaMTracecode.class.equals(clazz)) {
	            entityManagerToUse = entityManagerOracleH;
	        } else if (Entity_MDataCall.class.equals(clazz) || Entity_MDataCallCustomer.class.equals(clazz)
	                || Entity_MDataCallService.class.equals(clazz) || Entity_MMasterServiceCode.class.equals(clazz)
	                || Entity_MWaDataCall.class.equals(clazz) || Entity_MWaDataCallOptional.class.equals(clazz)
	                || Entity_MWaDataCallTrace.class.equals(clazz) || Entity_MWaMTracecode.class.equals(clazz)) {
	            entityManagerToUse = entityManagerOracleM;
	        }

	        log.info("엔티티 검색 전");
	        T entity = entityManagerToUse.find(clazz, orderid, LockModeType.PESSIMISTIC_WRITE);
	        log.info("엔티티 검색 후");

	        if (entity != null) {
	            entityManagerToUse.remove(entity);
	            entityManagerToUse.flush(); // Ensure the removal is flushed to the database
	            log.info("Entity with orderid " + orderid + " deleted successfully.");
	        } else {
	            log.warn("Entity with orderid " + orderid + " not found.");
	        }

	    } catch (Exception e) {
	        log.error("Error occurred during deleteAll operation", e);
	    }
	}

	
	
	@Override
	public Entity_WaDataCallOptional InsertWaDataCallOptional(Entity_WaDataCallOptional entityWaDataCallOptional,
			int wcseq) {
		Optional<Entity_WaDataCallOptional> existingEntity = repositoryWaDataCallOptional.findById(wcseq); // db에 인서트 하기
																											// 전. 키

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryWaDataCallOptional.save(entityWaDataCallOptional);// 없으면 인서트

	}

}
