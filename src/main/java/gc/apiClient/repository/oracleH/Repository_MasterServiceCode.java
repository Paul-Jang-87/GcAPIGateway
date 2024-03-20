package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_MasterServiceCode;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MasterServiceCode extends CrudRepository<Entity_MasterServiceCode,  Integer> {

	List<Entity_MasterServiceCode> findAll();
    Optional<Entity_MasterServiceCode> findById(int id);
    int countBy();
}
