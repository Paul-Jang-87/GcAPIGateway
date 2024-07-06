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

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.Ucrm;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_Ucrm extends CrudRepository<Entity_Ucrm, Ucrm> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_ContactLt c WHERE c.id.cpid = :cpidValue")
	List<Entity_Ucrm> findByCpid(@Param("cpidValue") String id);

	Optional<Entity_Ucrm> findById(Ucrm id);

	Page<Entity_Ucrm> findAll(Pageable pageable);

	@Modifying
	@Transactional
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("DELETE FROM Entity_Ucrm c WHERE c.topcDataIsueSno = :issueNo")
	void deleteByTopcDataIsueSno(@Param("issueNo") String topcDataIsueSno);
}
