package gc.apiClient.repository.postgresql;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Repository
public interface Repository_ContactLt extends CrudRepository<Entity_ContactLt, ContactLtId> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_ContactLt c WHERE c.id.cpid = :cpidValue")
	List<Entity_ContactLt> findByCpid(@Param("cpidValue") String id);

	@Query("SELECT e FROM Entity_ContactLt e WHERE e.id.cpid = :cpid")
	List<Entity_ContactLt> findByCpId(@Param("cpid") String cpid);

	Optional<Entity_ContactLt> findByCske(String id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_ContactLt c WHERE c.id = :id")
	Optional<Entity_ContactLt> findById(@Param("id") ContactLtId id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Entity_ContactLt c WHERE c.id.cpid = :cpid AND c.id.cpsq = :cpsq")
	Optional<Entity_ContactLt> lockByCpidAndCpsq(@Param("cpid") String cpid, @Param("cpsq") String cpsq);

	@Modifying
	@Transactional
	@Query("DELETE FROM Entity_ContactLt e WHERE e.id.cpid = :cpid AND e.id.cpsq = :cpsq")
	void deleteByCpidAndCpsq(@Param("cpid") String cpid, @Param("cpsq") String cpsq);

}
