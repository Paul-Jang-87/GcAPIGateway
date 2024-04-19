package gc.apiClient.repository.postgresql;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;

@Repository
public interface Repository_UcrmRt extends CrudRepository<Entity_UcrmRt , UcrmCampRt> {
	Optional<Entity_UcrmRt> findById(UcrmCampRt id);
	 Page<Entity_UcrmRt> findAll(Pageable pageable);
}