package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracleM.Entity_MWaMTracecode;

@Repository
public interface Repository_MWaMTraceCode extends CrudRepository<Entity_MWaMTracecode,  Integer> {

	List<Entity_MWaMTracecode> findAll();
    Optional<Entity_MWaMTracecode> findById(int id);
    int countBy();
}
