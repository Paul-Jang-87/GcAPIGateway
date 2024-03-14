package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_MWaDataCall;
import gc.apiClient.entity.oracle.Entity_MWaDataCallOptional;

@Repository
public interface Repository_MWaDataCallOptional extends CrudRepository<Entity_MWaDataCallOptional,  Integer> {

	List<Entity_MWaDataCallOptional> findAll();
    Optional<Entity_MWaDataCallOptional> findById(int wcseq);

}
