package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracleM.Entity_MWaDataCallTrace;

@Repository
public interface Repository_MWaDataCallTrace extends CrudRepository<Entity_MWaDataCallTrace,  Integer> {

	List<Entity_MWaDataCallTrace> findAll();
    Optional<Entity_MWaDataCallTrace> findById(int id);
    int countBy();
}
