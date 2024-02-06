package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_MapCoid;

@Repository
public interface Repository_CampMa extends CrudRepository<Entity_CampMa , Long> {
	 Optional<Entity_CampMa> findByCpid(String cpid);
}