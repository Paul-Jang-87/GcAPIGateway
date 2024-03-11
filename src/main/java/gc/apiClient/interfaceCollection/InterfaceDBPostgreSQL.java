package gc.apiClient.interfaceCollection;


import java.util.List;

import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_MapCoId;

public interface InterfaceDBPostgreSQL {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid);
	Entity_CampRtJson createCampRtJson(String cpid);
	Entity_CampMa createCampMaMsg(String cpid);
	Entity_ContactLt createContactLtMsg(String msg);
	Entity_ContactltMapper createContactLtGC(String msg);
	
	//insert
	Entity_CampRt InsertCampRt(Entity_CampRt entityCampRt);
	Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa);
	Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt);
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid);
	Entity_CampRt findCampRtByCpid(String cpid);
	Entity_MapCoId findMapcoidByCpid(String cpid);
	Integer findCampRtMaxRlsq();
	Entity_ContactLt findContactLtByCske(String cske);
	List<Entity_ContactLt> findContactLtByCpid(String cpid);
	
	
}
