package kafka.gcClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_AppConfig;

@Repository
public interface Repository_AppConfig extends CrudRepository<Entity_AppConfig, Long> {

	Optional<Entity_AppConfig> findByid(Long msg_id);
	
}