package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.WaDataCallTrace;
import gc.apiClient.entity.oracle.Entity_WaMTracecode;

@Repository
public interface Repository_WaMTraceCode extends CrudRepository<Entity_WaMTracecode,  WaDataCallTrace> {

    Optional<Entity_WaMTracecode> findById(WaDataCallTrace id);

}
