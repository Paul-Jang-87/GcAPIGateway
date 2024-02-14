package gc.apiClient.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.Entity_ContactLt;

@Repository
public interface Repository_ContactLt extends CrudRepository<Entity_ContactLt, Long> {

	@Query("SELECT c FROM Entity_ContactLt c WHERE c.cpid = :cpidValue")
	List<Entity_ContactLt> findByCpid(@Param("cpidValue") String cpid);
	
	Optional<Entity_ContactLt> findByCske(String cske);

}