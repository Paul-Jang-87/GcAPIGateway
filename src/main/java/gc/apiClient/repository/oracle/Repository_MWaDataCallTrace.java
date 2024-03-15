package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.WaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_MWaDataCallOptional;
import gc.apiClient.entity.oracle.Entity_MWaDataCallTrace;

@Repository
public interface Repository_MWaDataCallTrace extends CrudRepository<Entity_MWaDataCallTrace,  WaDataCallTrace> {

	List<Entity_MWaDataCallTrace> findAll();
    Optional<Entity_MWaDataCallTrace> findById(WaDataCallTrace id);
    int countBy();
}
