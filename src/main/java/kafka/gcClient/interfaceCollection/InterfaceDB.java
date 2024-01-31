package kafka.gcClient.interfaceCollection;

import kafka.gcClient.entity.Entity_AppConfig;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import kafka.gcClient.entity.Entity_MapCoid;
import reactor.core.publisher.Mono;

public interface InterfaceDB {

	//table 별 매핑
	Entity_CampRt createCampRtMsg();
	Entity_CampMa createCampMaMsg(String msg);
	Entity_ContactLt createContactLtMsg();
	Entity_AppConfig createAppConfigMsg();
	Entity_MapCoid createMapCoIdMsg();
	
	//insert
	Mono<Entity_CampRt> InsertCampRt(Entity_CampRt entityCampRt);
	Mono<Entity_CampMa> InsertCampMa(Entity_CampMa entityCampMa);
	Mono<Entity_ContactLt> InsertContactLt(Entity_ContactLt entityContactLt);
	Mono<Entity_AppConfig> InsertAppConfig(Entity_AppConfig entityAppConfig);
	Mono<Entity_MapCoid> InsertMapCoId(Entity_MapCoid entityMapCoid);
	
	//select
	void SelectCampMa(String target);
	Mono<Entity_MapCoid> findMapCoidByCpid(String cpid);
	
}
