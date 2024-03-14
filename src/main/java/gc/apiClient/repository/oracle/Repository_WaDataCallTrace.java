package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.WaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_WaDataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallTrace;

@Repository
public interface Repository_WaDataCallTrace extends CrudRepository<Entity_WaDataCallTrace,  WaDataCallTrace> {

	List<Entity_WaDataCallTrace> findAll();
    Optional<Entity_WaDataCallTrace> findById(WaDataCallTrace id);

}
