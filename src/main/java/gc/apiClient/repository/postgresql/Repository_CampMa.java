package gc.apiClient.repository.postgresql;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.postgresql.Entity_CampMa;

@Repository
public interface Repository_CampMa extends CrudRepository<Entity_CampMa , String> {
	 Optional<Entity_CampMa> findByCpid(String cpid);
	 int countBy();
}