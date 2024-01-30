package kafka.gcClient.service;


import org.springframework.stereotype.Service;

import kafka.gcClient.entity.Entity_AppConfig;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.interfaceCollection.InterfaceJson;
import kafka.gcClient.repository.Repository_AppConfig;
import kafka.gcClient.repository.Repository_CampMa;
import kafka.gcClient.repository.Repository_CampRt;
import kafka.gcClient.repository.Repository_ContactLt;
import reactor.core.publisher.Mono;

@Service
public class ServicePostgre implements InterfaceDB,InterfaceJson{
	
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_AppConfig repositoryAppConfig;
	private final Repository_ContactLt repositoryContactLt;
	private final InterfaceJson servicejson;
	
    public ServicePostgre(Repository_CampRt repositoryCampRt,Repository_CampMa repositoryCampMa,
    		Repository_AppConfig repositoryAppConfig,
    		Repository_ContactLt repositoryContactLt,
    		InterfaceJson servicejson) {
    	
    	this.repositoryCampRt = repositoryCampRt;
    	this.repositoryCampMa = repositoryCampMa;
        this.repositoryAppConfig = repositoryAppConfig;
        this.repositoryContactLt = repositoryContactLt;
        this.servicejson = servicejson;
    }

    @Override
	public Entity_CampRt createCampRtMsg() {
		return null;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String message) {
		String temp = servicejson.ExtractVal(message);
		System.out.println(temp);
		return null;
	}

	@Override
	public Entity_ContactLt createContactLtMsg() {
		return null;
	}

	@Override
	public Entity_AppConfig createAppConfigMsg() {
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
