package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_MWaDataCall;

@Repository
public interface Repository_MWaDataCall extends CrudRepository<Entity_MWaDataCall,  Integer> {

    Optional<Entity_MWaDataCall> findById(int wcseq);

}
