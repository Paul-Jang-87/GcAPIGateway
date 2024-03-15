package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.WaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_MWaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_MWaMTracecode;

@Repository
public interface Repository_MWaMTraceCode extends CrudRepository<Entity_MWaMTracecode,  WaDataCallTrace> {

	List<Entity_MWaMTracecode> findAll();
    Optional<Entity_MWaMTracecode> findById(WaDataCallTrace id);
    int countBy();
}
