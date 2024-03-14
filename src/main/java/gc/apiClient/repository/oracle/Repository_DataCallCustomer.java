package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallCustomer;
import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_DataCallCustomer;

@Repository
public interface Repository_DataCallCustomer extends CrudRepository<Entity_DataCallCustomer,  DataCallCustomer> {

	List<Entity_DataCallCustomer> findAll();
    Optional<Entity_DataCallCustomer> findById(DataCallCustomer id);
    int countBy();
}
