package kafka.gcClient.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_AppConfig;

@Repository
public interface Repository_AppConfig extends CrudRepository<Entity_AppConfig, Long> {
	
}