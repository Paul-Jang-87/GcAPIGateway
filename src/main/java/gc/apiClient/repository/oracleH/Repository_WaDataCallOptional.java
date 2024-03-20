package gc.apiClient.repository.oracleH;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_WaDataCallOptional extends CrudRepository<Entity_WaDataCallOptional,  Integer> {

	List<Entity_WaDataCallOptional> findAll();
    Optional<Entity_WaDataCallOptional> findById(int wcseq);
    int countBy();

}
