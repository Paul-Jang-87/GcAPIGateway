package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_ContactLt;

@Repository
public interface Repository_ContactLt extends CrudRepository<Entity_ContactLt, Long> {
	
	Optional<Entity_ContactLt> findByCpid(String cpid);
	
}