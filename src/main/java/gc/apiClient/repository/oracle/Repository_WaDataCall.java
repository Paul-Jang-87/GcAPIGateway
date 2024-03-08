package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_WaDataCall;

@Repository
public interface Repository_WaDataCall extends CrudRepository<Entity_WaDataCall,  Integer> {

    Optional<Entity_WaDataCall> findById(int wcseq);

}
