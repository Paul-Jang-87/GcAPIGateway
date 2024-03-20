package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracleH.Entity_WaMTracecode;

@Repository
public interface Repository_WaMTraceCode extends CrudRepository<Entity_WaMTracecode,  Integer> {

	List<Entity_WaMTracecode> findAll();
    Optional<Entity_WaMTracecode> findById(int id);
    int countBy();
}
