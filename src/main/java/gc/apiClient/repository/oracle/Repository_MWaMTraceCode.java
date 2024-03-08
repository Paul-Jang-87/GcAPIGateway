package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.WaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_MWaMTracecode;

@Repository
public interface Repository_MWaMTraceCode extends CrudRepository<Entity_MWaMTracecode,  WaDataCallTrace> {

    Optional<Entity_MWaMTracecode> findById(WaDataCallTrace id);

}
