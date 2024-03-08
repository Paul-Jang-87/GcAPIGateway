package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCall;
import gc.apiClient.entity.oracle.Entity_MDataCall;

@Repository
public interface Repository_MDataCall extends CrudRepository<Entity_MDataCall,  DataCall> {

    Optional<Entity_MDataCall> findById(DataCall id);

}
