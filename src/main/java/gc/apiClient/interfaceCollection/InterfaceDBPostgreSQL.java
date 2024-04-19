package gc.apiClient.interfaceCollection;


import java.util.List;

import org.json.JSONObject;

import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.Entity_CampMaJsonUcrm;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;

import org.springframework.data.domain.Page;

public interface InterfaceDBPostgreSQL {

	//table 별 매핑
	Entity_CampRt createCampRtMsg(String cpid) throws Exception;
	JSONObject createCampRtJson(Entity_CampRt enCampRt,String business) throws Exception;
	Entity_CampMaJson JsonCampMaCallbot(Entity_CampMa enCampMa, String datachgcd) throws Exception;
	Entity_CampMaJsonUcrm JsonCampMaUcrm(Entity_CampMa enCampMa, String datachgcd) throws Exception;
	Entity_CampMa CreateEnCampMa(String cpid) throws Exception;
	Entity_ContactLt createContactLtMsg(String msg) throws Exception;
	Entity_ContactLt createContactUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_Ucrm createUcrm(String msg) throws Exception;
	Entity_UcrmRt createUcrmRt(String msg) throws Exception;
	Entity_CallbotRt createCallbotRt(String msg) throws Exception;
	Entity_ApimRt createApimRt(String msg) throws Exception;
	String createContactLtGC(String msg) throws Exception;
	JSONObject createMaMsgApim(Entity_CampMa enCampMa,String datachgcd) throws Exception;
	
	//insert
	Entity_CampRt InsertCampRt(Entity_CampRt entityCampRt) throws Exception;
	Entity_Ucrm InsertUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa) throws Exception;
	Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt) throws Exception;
	Entity_CallbotRt InsertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception;
	Entity_UcrmRt InsertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception;
	Entity_ApimRt InsertApimRt(Entity_ApimRt enApimRt) throws Exception;
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid) throws Exception;
	Entity_CampRt findCampRtByCpid(String cpid) throws Exception;
	Integer findCampRtMaxRlsq() throws Exception;
	Entity_ContactLt findContactLtByCske(String cske)throws Exception;
	List<Entity_ContactLt> findContactLtByCpid(String cpid)throws Exception;
	Page<Entity_Ucrm> getAll() throws Exception; 
	Page<Entity_UcrmRt> getAllUcrmRt() throws Exception; 
	Page<Entity_CallbotRt> getAllCallBotRt() throws Exception; 
	Page<Entity_ApimRt> getAllApimRt() throws Exception; 
	int getRecordCount()throws Exception;
	
	//update 
	public void UpdateCampMa(String cpid, String cpna)throws Exception;
	
	//delete
	public void DelCampMaById(String cpid)throws Exception;
	public void DelCallBotRtById(CallBotCampRt id)throws Exception;
	public void DelUcrmRtById(UcrmCampRt id)throws Exception;
	public void DelApimRtById(ApimCampRt id)throws Exception;
	public void DelUcrmLtById(String topcDataIsueSno)throws Exception;
	
	
}
