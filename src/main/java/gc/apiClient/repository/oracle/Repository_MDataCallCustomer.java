package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_MDataCallCustomer;

@Repository
public interface Repository_MDataCallCustomer extends CrudRepository<Entity_MDataCallCustomer,  Integer> {

	List<Entity_MDataCallCustomer> findAll();
    Optional<Entity_MDataCallCustomer> findById(int id);
    int countBy();
}
