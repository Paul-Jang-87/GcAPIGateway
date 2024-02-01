package kafka.gcClient.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_MapCoid;

@Repository
public interface Repository_MapCoId extends CrudRepository<Entity_MapCoid, Long> {
	
    Optional<Entity_MapCoid> findByCpid(String cpid);
    
}