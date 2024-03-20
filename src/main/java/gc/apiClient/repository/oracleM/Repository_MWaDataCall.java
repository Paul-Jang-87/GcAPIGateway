package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MWaDataCall;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MWaDataCall extends CrudRepository<Entity_MWaDataCall,  Integer> {

	List<Entity_MWaDataCall> findAll();
    Optional<Entity_MWaDataCall> findById(int wcseq);
    int countBy();
}
