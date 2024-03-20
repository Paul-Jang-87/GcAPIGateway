package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_DataCallService;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_DataCallService extends CrudRepository<Entity_DataCallService,  Integer> {

	List<Entity_DataCallService> findAll();
    Optional<Entity_DataCallService> findById(int id);
    int countBy();
}
