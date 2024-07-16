package gc.apiClient.repository.postgresql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import jakarta.persistence.LockModeType;

@Repository
public interface Repository_CampMa_D extends CrudRepository<Entity_CampMa_D, String> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_CampMa_D c WHERE c.cpid = :cpid")
	Optional<Entity_CampMa_D> findByCpid(@Param("cpid") String cpid);
	
	@Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT c FROM Entity_CampMa_D c")
    List<Entity_CampMa_D> findAll();

	int countBy();
}