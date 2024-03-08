package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCall;
import gc.apiClient.entity.oracle.Entity_DataCall;

@Repository
public interface Repository_DataCall extends CrudRepository<Entity_DataCall,  DataCall> {

    Optional<Entity_DataCall> findById(DataCall id);

}
