package gc.apiClient.repository.postgresql;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;

@Repository
public interface Repository_ApimRt extends CrudRepository<Entity_ApimRt ,ApimCampRt> {
	Optional<Entity_ApimRt> findById(ApimCampRt id);
	 Page<Entity_ApimRt> findAll(Pageable pageable);
}