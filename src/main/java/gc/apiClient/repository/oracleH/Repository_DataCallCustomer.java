package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_DataCallCustomer;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_DataCallCustomer extends CrudRepository<Entity_DataCallCustomer,  Integer> {

	List<Entity_DataCallCustomer> findAll();
    Optional<Entity_DataCallCustomer> findById(int id);
    int countBy();
}
