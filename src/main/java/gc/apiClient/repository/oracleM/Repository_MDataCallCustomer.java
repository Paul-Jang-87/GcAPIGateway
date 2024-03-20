package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MDataCallCustomer;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MDataCallCustomer extends CrudRepository<Entity_MDataCallCustomer,  Integer> {

	List<Entity_MDataCallCustomer> findAll();
    Optional<Entity_MDataCallCustomer> findById(int id);
    int countBy();
}
