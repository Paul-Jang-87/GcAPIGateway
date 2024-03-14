package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.oracle.MasterServiceCode;
import gc.apiClient.entity.oracle.Entity_MDataCallService;
import gc.apiClient.entity.oracle.Entity_MMasterServiceCode;

@Repository
public interface Repository_MMasterServiceCode extends CrudRepository<Entity_MMasterServiceCode,  MasterServiceCode> {

	List<Entity_MMasterServiceCode> findAll();
    Optional<Entity_MMasterServiceCode> findById(MasterServiceCode id);

}
