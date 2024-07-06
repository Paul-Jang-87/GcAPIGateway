package gc.apiClient.repository.postgresql;

import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gc.apiClient.embeddable.CampRt;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_CampRt extends CrudRepository<Entity_CampRt, CampRt> {

	Optional<Entity_CampRt> findByCpid(String cpid);
	Optional<Entity_CampRt> findById(CampRt id);

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT MAX(e.id.rlsq) FROM Entity_CampRt e")
    Optional<Integer> findMaxRlsq();

}