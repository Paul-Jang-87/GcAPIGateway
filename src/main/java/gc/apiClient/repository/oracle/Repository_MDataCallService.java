package gc.apiClient.repository.oracle;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import gc.apiClient.embeddable.oracle.DataCallService;
import gc.apiClient.entity.oracle.Entity_MDataCallService;

@Repository
public interface Repository_MDataCallService extends CrudRepository<Entity_MDataCallService,  DataCallService> {

    Optional<Entity_MDataCallService> findById(DataCallService id);

}
