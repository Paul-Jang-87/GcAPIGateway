package kafka.gcClient.interfaceCollection;

import kafka.gcClient.entity.Entity_AppConfig;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import reactor.core.publisher.Mono;

public interface InterfaceDB {

	Entity_CampRt createCampRtMsg();
	Entity_CampMa createCampMaMsg(String msg);
	Entity_ContactLt createContactLtMsg();
	Entity_AppConfig createAppConfigMsg();
	
	Mono<Entity_CampRt> insertMsg(Entity_CampRt entityCampRt);
	Mono<Entity_CampMa> insertMsg(Entity_CampMa entityCampMa);
	Mono<Entity_ContactLt> insertMsg(Entity_ContactLt entityContactLt);
	Mono<Entity_AppConfig> insertMsg(Entity_AppConfig entityAppConfig);
	
}
