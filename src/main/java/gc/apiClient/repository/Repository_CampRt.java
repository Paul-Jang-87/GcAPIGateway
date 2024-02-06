package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_ContactLt;

@Repository
public interface Repository_CampRt extends CrudRepository<Entity_CampRt, Long> {
	Optional<Entity_CampRt> findByCpid(String cpid);
}