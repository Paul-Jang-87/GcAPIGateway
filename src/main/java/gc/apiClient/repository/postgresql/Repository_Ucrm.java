package gc.apiClient.repository.postgresql;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.Ucrm;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import jakarta.transaction.Transactional;

@Repository
public interface Repository_Ucrm extends CrudRepository<Entity_Ucrm,  Ucrm> {
    
    @Query("SELECT c FROM Entity_ContactLt c WHERE c.id.cpid = :cpidValue")
    List<Entity_Ucrm> findByCpid(@Param("cpidValue") String id);

    Optional<Entity_Ucrm> findById(Ucrm id);
    List<Entity_Ucrm> findAll();
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Entity_Ucrm c WHERE c.topcDataIsueSno = :issueNo")
    void deleteByTopcDataIsueSno(@Param("issueNo") String topcDataIsueSno);
}
