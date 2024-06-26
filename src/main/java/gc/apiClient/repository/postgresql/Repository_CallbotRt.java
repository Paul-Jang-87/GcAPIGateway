package gc.apiClient.repository.postgresql;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_CallbotRt extends CrudRepository<Entity_CallbotRt , CallBotCampRt> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Entity_CallbotRt c WHERE c.id = :id")
	Optional<Entity_CallbotRt> findById(@Param("id") CallBotCampRt id);
	Page<Entity_CallbotRt> findAll(Pageable pageable);
	
}