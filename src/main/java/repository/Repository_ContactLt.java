package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entity.Entity_ContactLt;

@Repository
public interface Repository_ContactLt extends CrudRepository<Entity_ContactLt, Long> {
	
}