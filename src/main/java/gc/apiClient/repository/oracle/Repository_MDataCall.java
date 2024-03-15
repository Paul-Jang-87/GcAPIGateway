package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCall;
import gc.apiClient.entity.oracle.Entity_MDataCall;
import gc.apiClient.entity.oracle.Entity_MasterServiceCode;

@Repository
public interface Repository_MDataCall extends CrudRepository<Entity_MDataCall,  DataCall> {

	List<Entity_MDataCall> findAll();
    Optional<Entity_MDataCall> findById(DataCall id);
    int countBy();
}
