package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.MasterServiceCode;
import gc.apiClient.entity.oracle.Entity_MasterServiceCode;

@Repository
public interface Repository_MasterServiceCode extends CrudRepository<Entity_MasterServiceCode,  MasterServiceCode> {

    Optional<Entity_MasterServiceCode> findById(MasterServiceCode id);

}
