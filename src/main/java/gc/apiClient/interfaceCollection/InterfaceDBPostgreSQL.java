package gc.apiClient.interfaceCollection;


import java.util.List;

import org.json.JSONObject;

import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;

public interface InterfaceDBPostgreSQL {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid) throws Exception;
	JSONObject createCampRtJson(Entity_CampRt enCampRt,String business) throws Exception;
	Entity_CampMaJson createCampMaJson(Entity_CampMa enCampMa, String datachgcd) throws Exception;
	Entity_CampMa createCampMaMsg(String cpid, String crudtype) throws Exception;
	Entity_ContactLt createContactLtMsg(String msg) throws Exception;
	Entity_ContactLt createContactUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_Ucrm createUcrm(String msg) throws Exception;
	String createContactLtGC(String msg) throws Exception;
	JSONObject createMaMsgApim(Entity_CampMa enCampMa,String datachgcd) throws Exception;
	
	//insert
	Entity_CampRt InsertCampRt(Entity_CampRt entityCampRt) throws Exception;
	Entity_Ucrm InsertUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa) throws Exception;
	Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt) throws Exception;
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid) throws Exception;
	Entity_CampRt findCampRtByCpid(String cpid) throws Exception;
	Integer findCampRtMaxRlsq() throws Exception;
	Entity_ContactLt findContactLtByCske(String cske)throws Exception;
	List<Entity_ContactLt> findContactLtByCpid(String cpid)throws Exception;
	List<Entity_Ucrm> getAll()throws Exception;
	int getRecordCount()throws Exception;
	
	//update 
	public void UpdateCampMa(String cpid, String cpna)throws Exception;
	
	//delete
	public void DelCampMaById(String cpid)throws Exception;
	
	
}
