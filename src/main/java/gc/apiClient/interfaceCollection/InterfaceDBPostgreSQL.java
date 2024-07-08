package gc.apiClient.interfaceCollection;


import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.UcrmCampRt;
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
	Entity_CampRt createCampRtMsg(String cpid, Entity_CampMa enCampMa) throws Exception;
	Entity_CampMa createEnCampMa(String cpid) throws Exception;
	Entity_ContactLt createContactLtMsg(String msg) throws Exception;
	Entity_ContactLt createContactUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_Ucrm createUcrm(String msg) throws Exception;
	Entity_UcrmRt createUcrmRt(String msg) throws Exception;
	Entity_CallbotRt createCallbotRt(String msg) throws Exception;
	Entity_ApimRt createApimRt(String msg) throws Exception;
	String createContactLtGC(String msg) throws Exception;
	
	//insert
	Entity_CampRt insertCampRt(Entity_CampRt entityCampRt) throws Exception;
	Entity_Ucrm insertUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_CampMa insertCampMa(Entity_CampMa entityCampMa) throws Exception;
	Entity_ContactLt insertContactLt(Entity_ContactLt entityContactLt) throws Exception;
	Entity_CallbotRt insertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception;
	Entity_UcrmRt insertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception;
	Entity_ApimRt insertApimRt(Entity_ApimRt enApimRt) throws Exception;
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid) throws Exception;
	Entity_Ucrm findUcrmBykey(String cpid,String cpsq) throws Exception;
	Integer findCampRtMaxRlsq() throws Exception;
	Page<Entity_Ucrm> getAll() throws Exception; 
	Page<Entity_UcrmRt> getAllUcrmRt() throws Exception; 
	Page<Entity_CallbotRt> getAllCallBotRt() throws Exception; 
	Page<Entity_ApimRt> getAllApimRt() throws Exception; 
	int getRecordCount()throws Exception;
	
	//update 
	public void updateCampMa(String cpid, String cpna)throws Exception;
	
	//delete
	public void delCampMaById(String cpid) throws Exception;
	public void delContactltById(ContactLtId id)throws Exception;
	public void delCallBotRtById(CallBotCampRt id) throws Exception;
	public void delUcrmRtById(UcrmCampRt id) throws Exception;
	public void delApimRtById(ApimCampRt id) throws Exception;
	public void delUcrmLtById(String topcDataIsueSno) throws Exception;
	public void deleteRecord(String cpid, String cpsq) throws Exception;
	
}
