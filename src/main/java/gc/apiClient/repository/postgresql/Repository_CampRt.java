package gc.apiClient.repository.postgresql;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.CampRt;
import gc.apiClient.entity.postgresql.Entity_CampRt;

@Repository
public interface Repository_CampRt extends CrudRepository<Entity_CampRt, CampRt> {

	Optional<Entity_CampRt> findByCpid(String cpid);
	Optional<Entity_CampRt> findById(CampRt id);

	@Query("SELECT MAX(e.id.rlsq) FROM Entity_CampRt e")
    Optional<Integer> findMaxRlsq();

}