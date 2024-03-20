package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_WaDataCall;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_WaDataCall extends CrudRepository<Entity_WaDataCall,  Integer> {

	List<Entity_WaDataCall> findAll();
    Optional<Entity_WaDataCall> findById(int wcseq);
    int countBy();
}
