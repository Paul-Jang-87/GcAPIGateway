package gc.apiClient.interfaceCollection;


import java.util.List;

import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.Entity_AppConfig;
import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.entity.Entity_MapCoid;

public interface InterfaceDB {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid);
	Entity_CampRtJson createCampRtJson(String cpid);
	Entity_CampRt createCampRtMsgCallbot(String cpid);
	Entity_CampRtJson createCampRtJsonCallbot(String cpid);
	Entity_CampMa createCampMaMsg(String cpid);
	Entity_ContactLt createContactLtMsg(String msg);
	Entity_ContactltMapper createContactLtGC(String msg);
	Entity_ContactltMapper createContactLCallbottGC(String msg);
	Entity_ContactLt createContactLtMsgCallbot(String msg);
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
	Entity_ContactLt findContactLtByCske(String cske);
	List<Entity_ContactLt> findContactLtByCpid(String cpid);
	Entity_AppConfig findAppConfigByid(Long id);
	Entity_AppConfig getEntityById(Long id);
	
}
