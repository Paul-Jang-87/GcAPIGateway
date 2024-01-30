package service;


import org.springframework.stereotype.Service;

import entity.Entity_AppConfig;
import entity.Entity_CampMa;
import entity.Entity_CampRt;
import entity.Entity_ContactLt;
import interfaceCollection.InterfaceDB;
import reactor.core.publisher.Mono;
import repository.Repository_AppConfig;
import repository.Repository_CampMa;
import repository.Repository_CampRt;
import repository.Repository_ContactLt;

@Service
public class ServicePostgre implements InterfaceDB{
	
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_AppConfig repositoryAppConfig;
	private final Repository_ContactLt repositoryContactLt;
	
    public ServicePostgre(Repository_CampRt repositoryCampRt,Repository_CampMa repositoryCampMa,
    		Repository_AppConfig repositoryAppConfig,
    		Repository_ContactLt repositoryContactLt) {
    	
    	this.repositoryCampRt = repositoryCampRt;
    	this.repositoryCampMa = repositoryCampMa;
        this.repositoryAppConfig = repositoryAppConfig;
        this.repositoryContactLt = repositoryContactLt;
    }

    @Override
	public Entity_CampRt createCampRtMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity_CampMa createCampMaMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity_ContactLt createContactLtMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity_AppConfig createAppConfigMsg() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public Mono<Entity_CampRt> insertMsg(Entity_CampRt entity_CampRt) {
		return Mono.fromCallable(() -> repositoryCampRt.save(entity_CampRt));
	}

	@Override
	public Mono<Entity_CampMa> insertMsg(Entity_CampMa entityCampMa) {
		return Mono.fromCallable(() -> repositoryCampMa.save(entityCampMa));
	}

	@Override
	public Mono<Entity_ContactLt> insertMsg(Entity_ContactLt entityContactLt) {
		return Mono.fromCallable(() -> repositoryContactLt.save(entityContactLt));
	}

	@Override
	public Mono<Entity_AppConfig> insertMsg(Entity_AppConfig entityAppConfig) {
		return Mono.fromCallable(() -> repositoryAppConfig.save(entityAppConfig));
	}


}
