package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallService;
import gc.apiClient.entity.oracle.Entity_MDataCallCustomer;
import gc.apiClient.entity.oracle.Entity_MDataCallService;

@Repository
public interface Repository_MDataCallService extends CrudRepository<Entity_MDataCallService,  DataCallService> {

	List<Entity_MDataCallService> findAll();
    Optional<Entity_MDataCallService> findById(DataCallService id);
    int countBy();
}
