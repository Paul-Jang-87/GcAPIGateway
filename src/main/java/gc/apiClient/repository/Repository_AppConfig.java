package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_AppConfig;

@Repository
public interface Repository_AppConfig extends CrudRepository<Entity_AppConfig, Long> {

	Optional<Entity_AppConfig> findByid(Long msg_id);
	
}