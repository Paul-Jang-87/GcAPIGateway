package gc.apiClient.interfaceCollection;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;

public interface InterfaceDBPostgreSQL {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid);
	Entity_CampRtJson createCampRtJson(Entity_CampRt enCampRt,String business);
	Entity_CampMaJson createCampMaJson(Entity_CampMa enCampMa, String datachgcd);
	Entity_CampMa createCampMaMsg(String cpid, String crudtype);
	Entity_ContactLt createContactLtMsg(String msg);
	String createContactLtGC(String msg);
	
	//insert
	Entity_CampRt InsertCampRt(Entity_CampRt entityCampRt);
	Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa);
	Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt);
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid);
	Entity_CampRt findCampRtByCpid(String cpid);
	Integer findCampRtMaxRlsq();
	Entity_ContactLt findContactLtByCske(String cske);
	List<Entity_ContactLt> findContactLtByCpid(String cpid);
	int getRecordCount();
	
	//update 
	public void UpdateCampMa(String cpid, String cpna);
	
	//delete
	public void DelCampMaById(String cpid);
	
	
}
