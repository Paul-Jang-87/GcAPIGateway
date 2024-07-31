package gc.apiClient.entity.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

import gc.apiClient.embeddable.CampRt;

@Data
@Entity
@Table(name = "CAMPRT")
/**
 * 발신결과들에 대한 정보를 카프카로 보낸 후 그 데이터들을 적재하는 테이블이다. 사실상 쓸 일이 없다. 
 * 이 테이블에 적재된 데이터들을 어디에 가져다 쓸 일도 없고 그냥 혹시나 비상시 확인용으로 적재해 두는 테이블이다.  
 * 추가설명) 제네시스에서 아웃바운드 콜이 끝나면 세가지 로직(UCRM,콜봇,APIM)별로 
 * 해당 테이들에 끝난 콜들에 대한 정보를 남긴다. 각각 Entity_UcrmRt(UCRM),Entity_CallbotRt(콜봇),Entity_ApimRt(APIM) 
 * 그리고 그 정보를 바탕으로 1분 간격으로 카프카에 메시지를 보낸 후 이 테이블(CAMPRT)에 적재를 한다. 때문에 이 테이블에는 3가지 로직에 대한
 * 모든 발신경과 정보들이 모여있다.  
 * 
 */


public class Entity_CampRt {
	
	@EmbeddedId
    private CampRt id;
	
	@Column(name = "CONTACT_LIST_ID")
	private String contactLtId;
	
	@Column(name = "CONTACT_ID")
 	private String contactid;
	
	@Column(name = "IBM_SLTN_CNTA_HUB_ID")
	private long hubid;
	
	@Column(name = "CAMP_SEQ")
	private int camp_seq;
	
	@Column(name = "DIRT")
	private int dirt;
	
	@Column(name = "DICT")
	private int dict;
	
	@Column(name = "CAMP_ID")
	private String cpid;
	
	@Column(name = "TKDA")
	private String tkda;
	
	@Column(name = "DIDT")
 	private Date didt;
	
	public Entity_CampRt(){
		
	}
	
}
	
