package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_MasterServiceCode;

@Repository
public interface Repository_MasterServiceCode extends CrudRepository<Entity_MasterServiceCode,  Integer> {

	List<Entity_MasterServiceCode> findAll();
    Optional<Entity_MasterServiceCode> findById(int id);
    int countBy();
}
