package kafka.gcClient.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kafka.gcClient.entity.Entity_CampMa;

@Repository
public interface Repository_CampMa extends CrudRepository<Entity_CampMa , Long> {
	 Optional<Entity_CampMa> findByCoid(String coid);
}