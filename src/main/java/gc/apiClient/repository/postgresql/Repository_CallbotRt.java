package gc.apiClient.repository.postgresql;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;

@Repository
public interface Repository_CallbotRt extends CrudRepository<Entity_CallbotRt , CallBotCampRt> {
	Optional<Entity_CallbotRt> findById(CallBotCampRt id);
	Page<Entity_CallbotRt> findAll(Pageable pageable);
	
}