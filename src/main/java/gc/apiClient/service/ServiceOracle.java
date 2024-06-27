package gc.apiClient.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

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
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
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

	@Autowired
	@Qualifier("oracleMTransactionManager")
	private PlatformTransactionManager transactionManagerOracleM;

	@Autowired
	@Qualifier("oracleHTransactionManager")
	private PlatformTransactionManager transactionManagerOracleH;

	@PersistenceContext(unitName = "oracleM")
	private EntityManager entityManagerOracleM;

	@PersistenceContext(unitName = "oracleH")
	private EntityManager entityManagerOracleH;

	@PersistenceContext(unitName = "postgres")
	private EntityManager entityManagerPostgres;

	/**
	 * 
	 * @param clazz 엔티티 클래스
	 * @return 트랜젝션 매니저
	 * 
	 * 매개변수로 들어오는 앤티티 클래스에 따라서 다른 트랜잭션 매니저를 리턴한다.
	 */
	
	private PlatformTransactionManager selectTransactionManager(Class<?> clazz) {
		if (Entity_DataCall.class.equals(clazz) || Entity_DataCallCustomer.class.equals(clazz)
				|| Entity_DataCallService.class.equals(clazz) || Entity_MasterServiceCode.class.equals(clazz)
				|| Entity_WaDataCall.class.equals(clazz) || Entity_WaDataCallOptional.class.equals(clazz)
				|| Entity_WaDataCallTrace.class.equals(clazz) || Entity_WaMTracecode.class.equals(clazz)) {
			return transactionManagerOracleH;
		} else if (Entity_MDataCall.class.equals(clazz) || Entity_MDataCallCustomer.class.equals(clazz)
				|| Entity_MDataCallService.class.equals(clazz) || Entity_MMasterServiceCode.class.equals(clazz)
				|| Entity_MWaDataCall.class.equals(clazz) || Entity_MWaDataCallOptional.class.equals(clazz)
				|| Entity_MWaDataCallTrace.class.equals(clazz) || Entity_MWaMTracecode.class.equals(clazz)) {
			return transactionManagerOracleM;
		} else {
			throw new IllegalArgumentException("알 수 없는 엔티티 클래스: " + clazz.getName());
		}
	}

	/**
	 * 
	 * @param clazz 엔티티 클래스
	 * @return 엔티티 매니저
	 * 
	 * 매개변수로 들어오는 앤티티 클래스에 따라서 다른 엔티티 매니저를 리턴한다.
	 */
	private EntityManager selectEntityManager(Class<?> clazz) {
		if (Entity_DataCall.class.equals(clazz) || Entity_DataCallCustomer.class.equals(clazz)
				|| Entity_DataCallService.class.equals(clazz) || Entity_MasterServiceCode.class.equals(clazz)
				|| Entity_WaDataCall.class.equals(clazz) || Entity_WaDataCallOptional.class.equals(clazz)
				|| Entity_WaDataCallTrace.class.equals(clazz) || Entity_WaMTracecode.class.equals(clazz)) {
			return entityManagerOracleH;
		} else if (Entity_MDataCall.class.equals(clazz) || Entity_MDataCallCustomer.class.equals(clazz)
				|| Entity_MDataCallService.class.equals(clazz) || Entity_MMasterServiceCode.class.equals(clazz)
				|| Entity_MWaDataCall.class.equals(clazz) || Entity_MWaDataCallOptional.class.equals(clazz)
				|| Entity_MWaDataCallTrace.class.equals(clazz) || Entity_MWaMTracecode.class.equals(clazz)) {
			return entityManagerOracleM;
		} else {
			throw new IllegalArgumentException("알 수 없는 엔티티 클래스: " + clazz.getName());
		}
	}
	
	/**
	 * 
	 * 테이블에 있는 레코드의 개수를 리턴해주는 함수.
	 */
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

	
	/**
	 * 매개변수로 들어오는 앤티티 클래스 즉, 그 테이블의 레코드들을 최대 400개까지 가져와 리스트로 반환한다. 
	 */
	public <T> List<T> getAll(Class<T> clazz) {
		
		PlatformTransactionManager transactionManagerToUse = selectTransactionManager(clazz);
		EntityManager entityManagerToUse = selectEntityManager(clazz);

		//TransactionTemplate 클래스는 Spring 안에 있는 헬퍼 클래스이다. 트랜잭션 관리를 프로그램적으로 단순화시켜준다. 'PlatformTransactionManager'를 매개변수로 받는다.
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerToUse);
		return transactionTemplate.execute(status -> {//트랜잭션 context 하에 괄호 안의 코드를 실행 후 리턴 값을 받음
			try {
				CriteriaBuilder cb = entityManagerToUse.getCriteriaBuilder(); //쿼리를 프로그램적으로 구조화 하기 위해 CriteriaBuilder 인스턴스를 만든다.
				//주어진 클래스(clazz 즉, 엔티티 클래스)에 대한 'CriteriaQuery' 객체를 생성한다. 여기서 'Root' 쿼리 안의 엔티티를 의미한다.
				jakarta.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(clazz);
				Root<T> root = cq.from(clazz);
				cq.select(root);
				//쿼리를 옵션들과 함께 실행한다. '비관적락','가져올 수 있는 레코드 최대 400개로 제한'
				return entityManagerToUse.createQuery(cq).setLockMode(LockModeType.PESSIMISTIC_WRITE) 
						.setMaxResults(400) //가져올 수 있는 레코드 최대 400개로 제한
						.getResultList();
			} catch (Exception e) {
				log.error("엔티티를 불러오는 과정에서 에러가 발생했습니다. {} : {}", clazz.getName(), e.getMessage());
				errorLogger.error("엔티티를 불러오는 과정에서 에러가 발생했습니다. {} : {}", clazz.getName(), e.getMessage(), e);
				throw new RuntimeException("엔티티를 불러오는 과정에서 에러가 발생했습니다. : " + clazz.getName(), e);
			}
		});
	}
	

	/**
	 * 테이블 안의 레코드를 'orderid'를 키 값으로 해서 삭제하는 함수. 
	 */
	public <T> void deleteAll(Class<T> clazz, int orderid) {
		PlatformTransactionManager transactionManagerToUse = selectTransactionManager(clazz);
		EntityManager entityManagerToUse = selectEntityManager(clazz);

		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerToUse);
		transactionTemplate.executeWithoutResult(status -> {//트랜잭션 context 하에 안의 코드를 실행 후 리턴 값을 받지않음.
			try {
				T entity = entityManagerToUse.find(clazz, orderid, LockModeType.PESSIMISTIC_WRITE);
				if (entity != null) {
					entityManagerToUse.remove(entity);
				}
			} catch (Exception e) {
				log.error("해당 orderid({})의 레코드를 삭제하는데 에러가 발생했습니다.: {} ", orderid, e.getMessage());
				errorLogger.error("엔티티를 불러오는 과정에서 에러가 발생했습니다. {} : {}", clazz.getName(), e.getMessage(), e);
				throw new RuntimeException("해당 orderid({})의 레코드를 삭제하는데 에러가 발생했습니다.: " + orderid, e);
			}
		});
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

	@Override
	public Entity_MWaDataCallOptional InsertMWaDataCallOptional(Entity_MWaDataCallOptional entityWaDataCallOptional,
			int wcseq) {
		Optional<Entity_MWaDataCallOptional> existingEntity = repositoryMWaDataCallOptional.findById(wcseq); // db에 인서트
																												// 하기
		// 전. 키

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryMWaDataCallOptional.save(entityWaDataCallOptional);// 없으면 인서트
	}

}
