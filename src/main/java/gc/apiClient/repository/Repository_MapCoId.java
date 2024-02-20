package gc.apiClient.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_MapCoid;

@Repository
public interface Repository_MapCoId extends CrudRepository<Entity_MapCoid, String> {
	
    Optional<Entity_MapCoid> findByCpid(String cpid);
    
}