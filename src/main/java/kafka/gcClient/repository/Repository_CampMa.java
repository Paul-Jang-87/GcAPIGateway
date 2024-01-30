package kafka.gcClient.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_CampMa;

@Repository
public interface Repository_CampMa extends CrudRepository<Entity_CampMa , Long> {
	
}