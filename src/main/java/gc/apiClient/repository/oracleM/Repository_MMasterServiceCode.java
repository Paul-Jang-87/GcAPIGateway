package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MMasterServiceCode;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MMasterServiceCode extends CrudRepository<Entity_MMasterServiceCode,  Integer> {

	List<Entity_MMasterServiceCode> findAll();
    Optional<Entity_MMasterServiceCode> findById(int id);
    int countBy();
}
