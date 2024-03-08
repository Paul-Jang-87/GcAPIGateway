package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_MWaDataCallOptional;

@Repository
public interface Repository_MWaDataCallOptional extends CrudRepository<Entity_MWaDataCallOptional,  Integer> {

    Optional<Entity_MWaDataCallOptional> findById(int wcseq);

}
