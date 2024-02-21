package gc.apiClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.CampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_ContactLt;

@Repository
public interface Repository_CampRt extends CrudRepository<Entity_CampRt, CampRt> {
	
	Optional<Entity_CampRt> findByCpid(String cpid);
	Optional<Entity_ContactLt> findById(ContactLtId id);

}