package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;

@Repository
public interface Repository_WaDataCallOptional extends CrudRepository<Entity_WaDataCallOptional,  Integer> {

	List<Entity_WaDataCallOptional> findAll();
    Optional<Entity_WaDataCallOptional> findById(int wcseq);
    int countBy();

}
