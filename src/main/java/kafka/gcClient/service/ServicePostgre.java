package kafka.gcClient.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kafka.gcClient.entity.Entity_AppConfig;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import kafka.gcClient.entity.Entity_MapCoid;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.repository.Repository_AppConfig;
import kafka.gcClient.repository.Repository_CampMa;
import kafka.gcClient.repository.Repository_CampRt;
import kafka.gcClient.repository.Repository_ContactLt;
import kafka.gcClient.repository.Repository_MapCoId;
import reactor.core.publisher.Mono;

@Service
public class ServicePostgre extends ServiceJson implements InterfaceDB {
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_AppConfig repositoryAppConfig;
	private final Repository_ContactLt repositoryContactLt;
	private final Repository_MapCoId repositoryMapCoId;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa,
			Repository_AppConfig repositoryAppConfig, Repository_MapCoId repositoryMapCoId,
			Repository_ContactLt repositoryContactLt) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryAppConfig = repositoryAppConfig;
		this.repositoryContactLt = repositoryContactLt;
		this.repositoryMapCoId = repositoryMapCoId;
	}

	// **Create
	@Override
	public Entity_MapCoid createMapCoIdMsg() {
		Entity_MapCoid enCampMa = new Entity_MapCoid();
		enCampMa.setCoid("coid_3");
		enCampMa.setCpid("cpid_3");

		return enCampMa;
	}

	@Override
	public Entity_CampRt createCampRtMsg() {
		return null;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String message) {

		Entity_CampMa enCampMa = new Entity_CampMa();
		String parts[] = message.split("\\|");

		enCampMa.setCoid(parts[0]);
		enCampMa.setCpid(parts[1]);
		enCampMa.setCpna(parts[2]);

		return enCampMa;
	}

	@Override
	public Entity_ContactLt createContactLtMsg() {
		return null;
	}

	@Override
	public Entity_AppConfig createAppConfigMsg() {
		return null;
	}

	// **Insert
	@Override
	public Mono<Entity_CampRt> InsertCampRt(Entity_CampRt entity_CampRt) {
		return Mono.fromCallable(() -> repositoryCampRt.save(entity_CampRt));
	}

	@Override
	public Mono<Entity_CampMa> InsertCampMa(Entity_CampMa entityCampMa) {
		return Mono.fromCallable(() -> repositoryCampMa.save(entityCampMa));
	}

	@Override
	public Mono<Entity_ContactLt> InsertContactLt(Entity_ContactLt entityContactLt) {
		return Mono.fromCallable(() -> repositoryContactLt.save(entityContactLt));
	}

	@Override
	public Mono<Entity_AppConfig> InsertAppConfig(Entity_AppConfig entityAppConfig) {
		return Mono.fromCallable(() -> repositoryAppConfig.save(entityAppConfig));
	}

	@Override
	public Mono<Entity_MapCoid> InsertMapCoId(Entity_MapCoid entityMapCoid) {
		return Mono.fromCallable(() -> repositoryMapCoId.save(entityMapCoid));
	}

	// **Select
	@Override
	public void SelectCampMa(String target) {

		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCoid(target);

		if (optionalEntity.isPresent()) {
		} else {
		}
	}

	@Override
	public Mono<Entity_MapCoid> findMapCoidByCpid(String cpid) {
		
	    Optional<Entity_MapCoid> optionalEntity = repositoryMapCoId.findByCpid(cpid);
	    
	    return Mono.justOrEmpty(optionalEntity);
	}


}
