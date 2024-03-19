package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_DataCall;

@Repository
public interface Repository_DataCall extends CrudRepository<Entity_DataCall, Integer> {

	List<Entity_DataCall> findAll();
    Optional<Entity_DataCall> findById(int id);
    int countBy();

}
