package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallCustomer;
import gc.apiClient.entity.oracle.Entity_MDataCallCustomer;

@Repository
public interface Repository_MDataCallCustomer extends CrudRepository<Entity_MDataCallCustomer,  DataCallCustomer> {

    Optional<Entity_MDataCallCustomer> findById(DataCallCustomer id);

}
