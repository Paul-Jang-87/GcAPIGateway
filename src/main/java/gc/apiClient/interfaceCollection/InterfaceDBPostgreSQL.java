package gc.apiClient.interfaceCollection;


import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;

import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

public interface InterfaceDBPostgreSQL {
	
	//insert
	Entity_CampRt insertCampRt(Entity_CampRt entityCampRt) throws Exception;
	Entity_Ucrm insertUcrm(Entity_Ucrm entityUcrm) throws Exception;
	Entity_CampMa insertCampMa(Entity_CampMa entityCampMa) throws Exception;
	Entity_CampMa_D insertCampMa_D(Entity_CampMa_D entityCampMa_D) throws Exception;
	Entity_ContactLt insertContactLt(Entity_ContactLt entityContactLt) throws Exception;
	Entity_CallbotRt insertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception;
	Entity_UcrmRt insertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception;
	Entity_ApimRt insertApimRt(Entity_ApimRt enApimRt) throws Exception;
	
	//select
	Entity_CampMa findCampMaByCpid(String cpid) throws Exception;
	Entity_CampMa_D findCampMa_DByCpid(String cpid) throws Exception;
	List<Entity_CampMa> getAllRecords() throws Exception;
	Integer findCampRtMaxRlsq() throws Exception;
	Page<Entity_Ucrm> getAll(String workdivscd) throws Exception;
	Page<Entity_UcrmRt> getAllUcrmRt() throws Exception; 
	Page<Entity_CallbotRt> getAllCallBotRt() throws Exception; 
	Page<Entity_ApimRt> getAllApimRt() throws Exception; 
	int getRecordCount()throws Exception;
	public List<Entity_ContactLt> getRecordsByCpid(String cpid)throws Exception; 
	
	//update 
	public void updateCampMa(JSONObject jsonobj)throws Exception;
	
	//delete
	public void delCampMaById(String cpid) throws Exception;
	public void delContactltById(ContactLtId id)throws Exception;
	public void delCallBotRtById(CallBotCampRt id) throws Exception;
	public void delUcrmRtById(UcrmCampRt id) throws Exception;
	public void delApimRtById(ApimCampRt id) throws Exception;
	public void delUcrmLtById(String topcDataIsueSno) throws Exception;
	public void delUcrmltRecord(String cpid, String cpsq) throws Exception;
	public void delContactltRecord(String cpid, String cpsq) throws Exception;
	
}
