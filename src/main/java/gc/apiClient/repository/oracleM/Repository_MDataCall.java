package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MDataCall;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MDataCall extends CrudRepository<Entity_MDataCall,  Integer> {

	List<Entity_MDataCall> findAll();
    Optional<Entity_MDataCall> findById(int id);
    int countBy();
}
