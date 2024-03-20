package gc.apiClient.repository.oracleM;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repository_MWaDataCallOptional extends CrudRepository<Entity_MWaDataCallOptional,  Integer> {

	List<Entity_MWaDataCallOptional> findAll();
    Optional<Entity_MWaDataCallOptional> findById(int wcseq);
    int countBy();
}
