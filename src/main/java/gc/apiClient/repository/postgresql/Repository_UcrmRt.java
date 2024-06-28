package gc.apiClient.repository.postgresql;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_UcrmRt extends CrudRepository<Entity_UcrmRt, UcrmCampRt> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_UcrmRt c WHERE c.id = :id")
	Optional<Entity_UcrmRt> findById(@Param("id") UcrmCampRt id);

	Page<Entity_UcrmRt> findAll(Pageable pageable);
}