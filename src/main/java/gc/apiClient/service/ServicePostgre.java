package gc.apiClient.service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.repository.postgresql.Repository_ApimRt;
import gc.apiClient.repository.postgresql.Repository_CallbotRt;
import gc.apiClient.repository.postgresql.Repository_CampMa;
import gc.apiClient.repository.postgresql.Repository_CampMa_D;
import gc.apiClient.repository.postgresql.Repository_CampRt;
import gc.apiClient.repository.postgresql.Repository_ContactLt;
import gc.apiClient.repository.postgresql.Repository_Ucrm;
import gc.apiClient.repository.postgresql.Repository_UcrmRt;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/**
 * 데이터 베이스와 관련된 서비스이다. CRUD와 관련 된 내용이 들어있는 서비스다.   
 * 참고 - 엔티티 클래스는 디비테이블로 볼 수 있고 레포지토리는 그 디비에 대한 동작(insert, select, delete)즉 쿼리를 정의한다.
 * 때문에 엔티티와 레포지토리는 1:1로 연결되어있다. 깊은 연관성을 가진다.  
 * 
 */
public class ServicePostgre implements InterfaceDBPostgreSQL {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// 검색 **Create **Insert **SelectupdateCampMa
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_CampMa_D repositoryCampMa_D;
	private final Repository_Ucrm repositoryUcrm;
	private final Repository_CallbotRt repositoryCallbotRt;
	private final Repository_UcrmRt repositoryUcrmRt;
	private final Repository_ApimRt repositoryApimRt;
	private final Repository_ContactLt repositoryContactLt;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa, Repository_ContactLt repositoryContactLt,
			Repository_Ucrm repositoryUcrm, Repository_CallbotRt repositoryCallbotRt, Repository_UcrmRt repositoryUcrmRt, 
			Repository_ApimRt repositoryApimRt,Repository_CampMa_D repositoryCampMa_D) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryUcrm = repositoryUcrm;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryContactLt = repositoryContactLt;
		this.repositoryCallbotRt = repositoryCallbotRt;
		this.repositoryUcrmRt = repositoryUcrmRt;
		this.repositoryApimRt = repositoryApimRt;
		this.repositoryCampMa_D = repositoryCampMa_D;
	}


	//Transactional 어노테이션은 트렌젝션 범위를 설정한다 함수 위에 쓰면 함수가 시작할 때부터 끝날 때 까지를 트랜잭션 범위로 설정한다는 의미가 된다. 
	@Override
	@Transactional
	public Entity_CampRt insertCampRt(Entity_CampRt entity_CampRt) {
		
		int coid =  entity_CampRt.getId().getCoid();
		int rlsq =  entity_CampRt.getId().getRlsq();
		
		if( coid == 0 && rlsq == 0 ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 바로 함수 종료
			throw new RuntimeException("복합키 coid와 rlsq의 값이 모두 0입니다. 레코드를 테이블에 추가할 수 없습니다.");
		}

		//디비에서 해당 레코드를 찾는다. 엔티티(레코드)가 매개변수로 들어왔고, 그것의 키값(entity_CampRt.getId())으로 디비를 조회한다(findById). 
		Optional<Entity_CampRt> existingEntity = repositoryCampRt.findById(entity_CampRt.getId());  

		if (existingEntity.isPresent()) {//조회 결과 해당 레코드가 테이블에 이미 존재한다면 에러를 발생시킨다.
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		//없으면 그대로 해달 레코드를 디비에 인서트한다. 
		return repositoryCampRt.save(entity_CampRt);

	}

	@Override
	@Transactional
	public Entity_CampMa insertCampMa(Entity_CampMa entityCampMa) {
		
		String cpid = entityCampMa.getCpid();
		if(cpid.equals("")) {
			throw new RuntimeException("캠페인 아이디가 공백입니다. 레코드를 테이블에 추가할 수 없습니다.");
		}
		
		Optional<Entity_CampMa> existingEntity = repositoryCampMa.findByCpid(entityCampMa.getCpid()); // db에 인서트 하기 전. 키 값인 캠페인 아이디로 먼저 조회를 한다.

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryCampMa.save(entityCampMa);// 없으면 인서트
	}
	
	
	@Override
	@Transactional
	public Entity_CampMa_D insertCampMa_D(Entity_CampMa_D entityCampMa_D) throws Exception {
		
		String cpid = entityCampMa_D.getCpid();
		if(cpid.equals("")) {
			throw new RuntimeException("캠페인 아이디가 공백입니다. 레코드를 테이블에 추가할 수 없습니다.");
		}
		
		Optional<Entity_CampMa_D> existingEntity = repositoryCampMa_D.findByCpid(entityCampMa_D.getCpid()); // db에 인서트 하기 전. 키 값인 캠페인 아이디로 먼저 조회를 한다.

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryCampMa_D.save(entityCampMa_D);// 없으면 인서트
	}
	

	@Override
	@Transactional
	public Entity_Ucrm insertUcrm(Entity_Ucrm entityUcrm) {
		
		String cpsq = entityUcrm.getId().getCpsq();
		String cpid = entityUcrm.getId().getCpid();
		
		if( cpid.equals("") && cpsq.equals("") ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 예외 발생 후 바로 함수 종료
			throw new RuntimeException("복합키 cpsq와 cpid의 값이 없습니다. 레코드를 테이블에 추가할 수 없습니다.");
		}

		Optional<Entity_Ucrm> existingEntity = repositoryUcrm.findById(entityUcrm.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrm.save(entityUcrm);
	}

	@Override
	@Transactional
	public Entity_ContactLt insertContactLt(Entity_ContactLt entityContactLt) {
		
		int cpsq =  entityContactLt.getId().getCpsq();
		String cpid = entityContactLt.getId().getCpid();
		
		if( cpid.equals("") && cpsq ==0 ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 예외 발생 후 바로 함수 종료
			throw new RuntimeException("복합키 cpsq와 cpid의 값이 없습니다. 레코드를 테이블에 추가할 수 없습니다.");
		}

		Optional<Entity_ContactLt> existingEntity = repositoryContactLt.findById(entityContactLt.getId());

		if (existingEntity.isPresent()) {
		}
		return repositoryContactLt.save(entityContactLt);
	}

	@Override
	@Transactional
	public Entity_CampMa findCampMaByCpid(String cpid) {

		try {
			Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);//주어진 값(여기서는 'cpid')으로 주어진 값과 일치하는 값을 가지고 있는 레코드를 디비에서 찾는다. 
			return optionalEntity.orElse(null); //있다면 찾은 레코드를 반환하고 없으면 null을 반환한다.
		} catch (Exception e) {
			log.error("(findCampMaByCpid) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return null;
		}
	}
	
	
	@Override
	@Transactional
	public Entity_CampMa_D findCampMa_DByCpid(String cpid) throws Exception {
		try {
			Optional<Entity_CampMa_D> optionalEntity = repositoryCampMa_D.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (Exception e) {
			log.error("(findCampMa_DByCpid) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return null;
		}
	}
	
	
	@Override
	@Transactional
	public List<Entity_CampMa> getAllRecords() throws Exception {//디비에 모든 레코드를 가지고 온다. 
		 return repositoryCampMa.findAll();
	}
	

	@Override
	@Transactional
	public Integer findCampRtMaxRlsq() {//CAMPRT테이블의 속성 중 rlsq 값의 최고 값을 가지고 온다. 

		try {
			Optional<Integer> optionalEntity = repositoryCampRt.findMaxRlsq();
			return optionalEntity.orElse(null);
		} catch (Exception e) {
			log.error("(findCampRtMaxRlsq) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public int getRecordCount() {
		log.info("(getRecordCount) - Campma 테이블 레코드 수 : {}", repositoryCampMa.countBy());
		return repositoryCampMa.countBy();
	}

	@Override
	@Transactional
	/*
	 * UCRMLT테이블에서 workDivsCd 속성 값과 매개변수로 들어온 'workdivscd' 값이 일치하는 레코드들만 전부 반환.
	 * 한번에 최대 750개 까지 
	 */
	public Page<Entity_Ucrm> getAll(String workdivscd) throws Exception { 
	    return repositoryUcrm.findAllWithLock(workdivscd, PageRequest.of(0, 750));
	}

	
	@Override
	@Transactional
	public Page<Entity_UcrmRt> getAllUcrmRt() throws Exception {
		return repositoryUcrmRt.findAll(PageRequest.of(0, 1000));//레코드를 가지고 온다. 한번에 최대 1000개까지
	}

	@Override
	@Transactional
	public Page<Entity_CallbotRt> getAllCallBotRt() throws Exception {
		return repositoryCallbotRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	@Transactional
	public Page<Entity_ApimRt> getAllApimRt() throws Exception {
		return repositoryApimRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	@Transactional
	public void delCampMaById(String cpid) throws Exception {

		Optional<Entity_CampMa> entityOpt = repositoryCampMa.findByCpid(cpid); //삭제하기 전에 삭제하려는 레코드(매개변수로 들어온 cpid와 일치하는 값을 가진)가 디비에 존재하는지 일단 찾는다.  
		if (entityOpt.isPresent()) {
			repositoryCampMa.deleteById(cpid);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + cpid);
		}
	}

	@Override
	@Transactional
	public void delCallBotRtById(CallBotCampRt id) throws Exception {

		Optional<Entity_CallbotRt> entityOpt = repositoryCallbotRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryCallbotRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delUcrmRtById(UcrmCampRt id) throws Exception {

		Optional<Entity_UcrmRt> entityOpt = repositoryUcrmRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryUcrmRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delApimRtById(ApimCampRt id) throws Exception {

		Optional<Entity_ApimRt> entityOpt = repositoryApimRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryApimRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delUcrmLtById(String topcDataIsueSno) throws Exception {
		Optional<Entity_Ucrm> entityOptional = repositoryUcrm.lockByTopcDataIsueSno(topcDataIsueSno);
		if (entityOptional.isPresent()) {
			repositoryUcrm.deleteByTopcDataIsueSno(topcDataIsueSno);
		} else {
			throw new Exception("해당 topcDataIsueSno 값을 가진 엔티티가 DB테이블에서 조회되지 않습니다. : " + topcDataIsueSno);
		}
	}

	@Override
	@Transactional
	public void delContactltById(ContactLtId id) throws Exception {

		Optional<Entity_ContactLt> entityOpt = repositoryContactLt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryContactLt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void updateCampMa(JSONObject jsonobj) throws Exception {
		
		String cpid = jsonobj.getString("cpid");
		
		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);// 캠페인 아이디로 레코드 조회.

		if (optionalEntity.isPresent()) {// 조회 후 있다면 해당 레코드의 캠페인명 업데이트
			Entity_CampMa entity = optionalEntity.get();
			// 로컬 시간 가져오기
	        LocalDateTime localDateTime = LocalDateTime.now();
	        // UTC 시간대로 변환
	        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
	        // 포맷 정의
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	        // 문자열로 변환
	        String formattedDateTime = utcDateTime.format(formatter);
	        
	        //새로운 값들로 세팅
	        entity.setCpna(jsonobj.getString("cpnm"));
	        entity.setContactltid(jsonobj.getString("contactListid"));
	        entity.setQueueid(jsonobj.getString("queueid"));
	        entity.setDivisionnm(jsonobj.getString("divisionnm"));
	        entity.setCoid(Integer.parseInt(jsonobj.getString("coid")));
	        entity.setModdate(formattedDateTime);
	        
	        //새로운 값으로 인서트하면 업데이트가 된다. 
			repositoryCampMa.save(entity);
			
		} else {
			throw new EntityNotFoundException("해당 cpid (" + cpid + ")로 조회 된 레코드가 DB에 없습니다.");
		}
	}

	@Override
	@Transactional
	public Entity_CallbotRt insertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception {
		
		String cpsq = enCallbotRt.getId().getCpsq();
		String cpid = enCallbotRt.getId().getCpid();
		
		if( cpid.equals("") && cpsq.equals("") ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 예외 발생 후 바로 함수 종료
			throw new RuntimeException("복합키 cpsq와 cpid의 값이 없습니다. 레코드를 테이블에 추가할 수 없습니다.");
		}

		Optional<Entity_CallbotRt> existingEntity = repositoryCallbotRt.findById(enCallbotRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryCallbotRt.save(enCallbotRt);
	}

	@Override
	@Transactional
	public Entity_UcrmRt insertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception {
		
		String cpsq = enUcrmRt.getId().getCpsq();
		String cpid = enUcrmRt.getId().getCpid();
		
		if( cpid.equals("") && cpsq.equals("") ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 예외 발생 후 바로 함수 종료
			throw new RuntimeException("복합키 cpsq와 cpid의 값이 없습니다. 레코드를 테이블에 추가할 수 없습니다.");
		}
		
		Optional<Entity_UcrmRt> existingEntity = repositoryUcrmRt.findById(enUcrmRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrmRt.save(enUcrmRt);
	}

	@Override
	@Transactional
	public Entity_ApimRt insertApimRt(Entity_ApimRt enApimRt) throws Exception {
		
		String cpsq = enApimRt.getId().getCpsq();
		String cpid = enApimRt.getId().getCpid();
		
		if( cpid.equals("") && cpsq.equals("") ) {//2024-07-31 테이블 키 값이 없는경우(정상이 아닐 경우) 예외 발생 후 바로 함수 종료
			throw new RuntimeException("복합키 cpsq와 cpid의 값이 없습니다. 레코드를 테이블에 추가할 수 없습니다.");
		}
		
		Optional<Entity_ApimRt> existingEntity = repositoryApimRt.findById(enApimRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryApimRt.save(enApimRt);
	}
	

	@Override
	@Transactional
	public void delUcrmltRecord(String cpid, String cpsq) throws Exception {//두 값(cpid, cpsq)을 기준으로 레코드를 디비에서 찾는다.  
		Optional<Entity_Ucrm> entityOptional = repositoryUcrm.lockByCpidAndCpsq(cpid, cpsq);
		if (entityOptional.isPresent()) {
			repositoryUcrm.deleteByCpidAndCpsq(cpid, cpsq);
		} else {
		}

	}

	@Override
	@Transactional
	public void delContactltRecord(String cpid, String cpsq) throws Exception {
		Optional<Entity_ContactLt> entityOptional = repositoryContactLt.lockByCpidAndCpsq(cpid, cpsq);
		if (entityOptional.isPresent()) {
			repositoryContactLt.deleteByCpidAndCpsq(cpid, cpsq);
		} else {
		}

	}

	@Override
	public List<Entity_ContactLt> getRecordsByCpid(String cpid) throws Exception {
		return repositoryContactLt.findByCpId(cpid);
	}


}
