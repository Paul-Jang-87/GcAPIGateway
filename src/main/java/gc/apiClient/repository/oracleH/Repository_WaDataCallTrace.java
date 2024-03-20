package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracleH.Entity_WaDataCallTrace;

@Repository
public interface Repository_WaDataCallTrace extends CrudRepository<Entity_WaDataCallTrace,  Integer> {

	List<Entity_WaDataCallTrace> findAll();
    Optional<Entity_WaDataCallTrace> findById(int id);
    int countBy();
}
