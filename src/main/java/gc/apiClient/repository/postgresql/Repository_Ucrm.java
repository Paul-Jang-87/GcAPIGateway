package gc.apiClient.repository.postgresql;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import gc.apiClient.embeddable.Ucrm;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_Ucrm extends CrudRepository<Entity_Ucrm, Ucrm> {
	

	Optional<Entity_Ucrm> findById(Ucrm id);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_Ucrm c WHERE c.workDivsCd = :workdivscd")
	Page<Entity_Ucrm> findAllWithLock(@Param("workdivscd") String workdivscd, Pageable pageable);

	Page<Entity_Ucrm> findAll(Pageable pageable);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Entity_Ucrm c WHERE c.topcDataIsueSno = :issueNo")
    Optional<Entity_Ucrm> lockByTopcDataIsueSno(@Param("issueNo") String topcDataIsueSno);

    @Modifying
    @Transactional
    @Query("DELETE FROM Entity_Ucrm c WHERE c.topcDataIsueSno = :issueNo")
    void deleteByTopcDataIsueSno(@Param("issueNo") String topcDataIsueSno);
    
    
    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Entity_Ucrm c WHERE c.id.cpid = :cpid AND c.id.cpsq = :cpsq")
    Optional<Entity_Ucrm> lockByCpidAndCpsq(@Param("cpid") String cpid, @Param("cpsq") String cpsq);

    @Modifying
    @Transactional
    @Query("DELETE FROM Entity_Ucrm e WHERE e.id.cpid = :cpid AND e.id.cpsq = :cpsq")
    void deleteByCpidAndCpsq(@Param("cpid") String cpid, @Param("cpsq") String cpsq);
}
