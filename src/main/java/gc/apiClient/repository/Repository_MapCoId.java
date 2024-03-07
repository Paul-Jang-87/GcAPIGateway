package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.postgresql.Entity_MapCoId;

@Repository
public interface Repository_MapCoId extends CrudRepository<Entity_MapCoId , String> {
	 Optional<Entity_MapCoId> findByCpid(String cpid);
}