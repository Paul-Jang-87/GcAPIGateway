package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_DataCall;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_DataCall extends CrudRepository<Entity_DataCall, Integer> {

	List<Entity_DataCall> findAll();
    Optional<Entity_DataCall> findById(int id);
    int countBy();

}
