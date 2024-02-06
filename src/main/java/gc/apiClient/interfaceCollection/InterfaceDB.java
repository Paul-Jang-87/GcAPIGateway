package gc.apiClient.interfaceCollection;


import gc.apiClient.entity.Entity_AppConfig;
import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_MapCoid;
import reactor.core.publisher.Mono;

public interface InterfaceDB {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid);
	Entity_CampRt createCampRtToJson(String cpid);
	Entity_CampMa createCampMaMsg(String cpid);
	Entity_ContactLt createContactLtMsg(String msg);
	Entity_AppConfig createAppConfigMsg(String encryptedPassword);
	Entity_MapCoid createMapCoIdMsg();
	
	//insert
	Entity_CampRt InsertCampRt(Entity_CampRt entityCampRt);
	Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa);
	Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt);
	Entity_AppConfig InsertAppConfig(Entity_AppConfig entityAppConfig);
	Entity_MapCoid InsertMapCoId(Entity_MapCoid entityMapCoid);
	
	//select
	Entity_MapCoid findMapCoidByCpid(String cpid);
	Entity_CampMa findCampMaByCpid(String cpid);
	Entity_CampRt findCampRtByCpid(String cpid);
	Entity_ContactLt findContactLtByid(String id);
	Entity_AppConfig findAppConfigByid(Long id);
	Entity_AppConfig getEntityById(Long id);
	
}
