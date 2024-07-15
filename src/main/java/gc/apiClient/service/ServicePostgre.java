package gc.apiClient.service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.data.domain.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.repository.postgresql.Repository_ApimRt;
import gc.apiClient.repository.postgresql.Repository_CallbotRt;
import gc.apiClient.repository.postgresql.Repository_CampMa;
import gc.apiClient.repository.postgresql.Repository_CampRt;
import gc.apiClient.repository.postgresql.Repository_ContactLt;
import gc.apiClient.repository.postgresql.Repository_Ucrm;
import gc.apiClient.repository.postgresql.Repository_UcrmRt;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicePostgre implements InterfaceDBPostgreSQL {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_Ucrm repositoryUcrm;
	private final Repository_CallbotRt repositoryCallbotRt;
	private final Repository_UcrmRt repositoryUcrmRt;
	private final Repository_ApimRt repositoryApimRt;
	private final Repository_ContactLt repositoryContactLt;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa, Repository_ContactLt repositoryContactLt,
			Repository_Ucrm repositoryUcrm, Repository_CallbotRt repositoryCallbotRt, Repository_UcrmRt repositoryUcrmRt, Repository_ApimRt repositoryApimRt) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryUcrm = repositoryUcrm;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryContactLt = repositoryContactLt;
		this.repositoryCallbotRt = repositoryCallbotRt;
		this.repositoryUcrmRt = repositoryUcrmRt;
		this.repositoryApimRt = repositoryApimRt;
	}


	@Override
	@Transactional
	public Entity_CampRt insertCampRt(Entity_CampRt entity_CampRt) {

		Optional<Entity_CampRt> existingEntity = repositoryCampRt.findById(entity_CampRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryCampRt.save(entity_CampRt);

	}

	@Override
	@Transactional
	public Entity_CampMa insertCampMa(Entity_CampMa entityCampMa) {

		Optional<Entity_CampMa> existingEntity = repositoryCampMa.findByCpid(entityCampMa.getCpid()); // db에 인서트 하기 전. 키 값인 캠페인 아이디로 먼저 조회를 한다.

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryCampMa.save(entityCampMa);// 없으면 인서트
	}

	@Override
	@Transactional
	public Entity_Ucrm insertUcrm(Entity_Ucrm entityUcrm) {

		Optional<Entity_Ucrm> existingEntity = repositoryUcrm.findById(entityUcrm.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrm.save(entityUcrm);
	}

	@Override
	@Transactional
	public Entity_ContactLt insertContactLt(Entity_ContactLt entityContactLt) {

		Optional<Entity_ContactLt> existingEntity = repositoryContactLt.findById(entityContactLt.getId());

		if (existingEntity.isPresent()) {
		}
		return repositoryContactLt.save(entityContactLt);
	}

	@Override
	@Transactional
	public Entity_CampMa findCampMaByCpid(String cpid) {

		try {
			Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampMa by cpid: {}", cpid);
			errorLogger.error("Error retrieving Entity_CampMa by cpid: {}", cpid, ex);
			return null;
		}
	}
	
	
	@Override
	@Transactional
	public List<Entity_CampMa> getAllRecords() throws Exception {
		 return repositoryCampMa.findAll();
	}
	

	@Override
	@Transactional
	public Integer findCampRtMaxRlsq() {
		log.info("Transaction active findCampRtMaxRlsq: {}", TransactionSynchronizationManager.isActualTransactionActive());

		try {
			Optional<Integer> optionalEntity = repositoryCampRt.findMaxRlsq();
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampRt which has hightest value of 'rlsq' column: {}", ex.getMessage());
			errorLogger.error("Error retrieving Entity_CampRt which has hightest value of 'rlsq' column: {}", ex.getMessage(), ex);
			return null;
		}
	}

	@Override
	public int getRecordCount() {
		log.info("Campma 테이블 레코드 수 : {}", repositoryCampMa.countBy());
		return repositoryCampMa.countBy();
	}

	@Override
	@Transactional
	public Page<Entity_Ucrm> getAll() throws Exception {
		return repositoryUcrm.findAllWithLock(PageRequest.of(0, 750));
	}

	@Override
	public Page<Entity_UcrmRt> getAllUcrmRt() throws Exception {
		return repositoryUcrmRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	public Page<Entity_CallbotRt> getAllCallBotRt() throws Exception {
		return repositoryCallbotRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	public Page<Entity_ApimRt> getAllApimRt() throws Exception {
		return repositoryApimRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	@Transactional
	public void delCampMaById(String cpid) throws Exception {

		Optional<Entity_CampMa> entityOpt = repositoryCampMa.findByCpid(cpid);
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
	public void updateCampMa(String cpid, String cpna) throws Exception {
		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findById(cpid);// 캠페인 아이디로 레코드 조회.

		if (optionalEntity.isPresent()) {// 조회 후 있다면 해당 레코드의 캠페인명 업데이트
			Entity_CampMa entity = optionalEntity.get();
			entity.setCpna(cpna);
			// 로컬 시간 가져오기
	        LocalDateTime localDateTime = LocalDateTime.now();

	        // UTC 시간대로 변환
	        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));

	        // 포맷 정의
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	        // 문자열로 변환
	        String formattedDateTime = utcDateTime.format(formatter);
	        entity.setModdate(formattedDateTime);
			repositoryCampMa.save(entity);
		} else {
			throw new EntityNotFoundException("해당 cpid (" + cpid + ")로 조회 된 레코드가 DB에 없습니다.");
		}
	}

	@Override
	@Transactional
	public Entity_CallbotRt insertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception {

		Optional<Entity_CallbotRt> existingEntity = repositoryCallbotRt.findById(enCallbotRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryCallbotRt.save(enCallbotRt);
	}

	@Override
	@Transactional
	public Entity_UcrmRt insertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception {
		Optional<Entity_UcrmRt> existingEntity = repositoryUcrmRt.findById(enUcrmRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrmRt.save(enUcrmRt);
	}

	@Override
	@Transactional
	public Entity_ApimRt insertApimRt(Entity_ApimRt enApimRt) throws Exception {
		Optional<Entity_ApimRt> existingEntity = repositoryApimRt.findById(enApimRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryApimRt.save(enApimRt);
	}
	

	@Override
	@Transactional
	public void delUcrmltRecord(String cpid, String cpsq) throws Exception {
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
