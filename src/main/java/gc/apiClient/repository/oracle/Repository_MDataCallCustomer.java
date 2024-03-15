package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallCustomer;
import gc.apiClient.entity.oracle.Entity_MDataCall;
import gc.apiClient.entity.oracle.Entity_MDataCallCustomer;

@Repository
public interface Repository_MDataCallCustomer extends CrudRepository<Entity_MDataCallCustomer,  DataCallCustomer> {

	List<Entity_MDataCallCustomer> findAll();
    Optional<Entity_MDataCallCustomer> findById(DataCallCustomer id);
    int countBy();
}
