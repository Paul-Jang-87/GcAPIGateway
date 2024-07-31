package gc.apiClient.repository.postgresql;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_ApimRt extends CrudRepository<Entity_ApimRt, ApimCampRt> {

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_ApimRt c WHERE c.id = :id")
	Optional<Entity_ApimRt> findById(@Param("id") ApimCampRt id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT e FROM Entity_ApimRt e")
	Page<Entity_ApimRt> findAll(Pageable pageable);
}