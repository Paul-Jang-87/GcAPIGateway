package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallService;
import gc.apiClient.entity.oracle.Entity_DataCallCustomer;
import gc.apiClient.entity.oracle.Entity_DataCallService;

@Repository
public interface Repository_DataCallService extends CrudRepository<Entity_DataCallService,  DataCallService> {

	List<Entity_DataCallService> findAll();
    Optional<Entity_DataCallService> findById(DataCallService id);
    int countBy();
}
