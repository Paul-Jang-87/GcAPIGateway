package gc.apiClient.repository.postgresql;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_ContactLt extends CrudRepository<Entity_ContactLt,  ContactLtId> {
    
    @Query("SELECT c FROM Entity_ContactLt c WHERE c.id.cpid = :cpidValue")
    List<Entity_ContactLt> findByCpid(@Param("cpidValue") String id);

    Optional<Entity_ContactLt> findByCske(String id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Entity_ContactLt c WHERE c.id = :id")
    Optional<Entity_ContactLt> findById(@Param("id") ContactLtId id);

}
