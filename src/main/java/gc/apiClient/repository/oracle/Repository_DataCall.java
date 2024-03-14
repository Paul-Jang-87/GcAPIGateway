package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCall;
import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;

@Repository
public interface Repository_DataCall extends CrudRepository<Entity_DataCall,  DataCall> {

	List<Entity_DataCall> findAll();
    Optional<Entity_DataCall> findById(DataCall id);
    int countBy();

}
