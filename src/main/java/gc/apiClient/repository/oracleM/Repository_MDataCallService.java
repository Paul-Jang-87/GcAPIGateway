package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MDataCallService;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MDataCallService extends CrudRepository<Entity_MDataCallService,  Integer> {

	List<Entity_MDataCallService> findAll();
    Optional<Entity_MDataCallService> findById(int id);
    int countBy();
}
