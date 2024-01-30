package kafka.gcClient.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_CampRt;

@Repository
public interface Repository_CampRt extends CrudRepository<Entity_CampRt, Long> {
	
}