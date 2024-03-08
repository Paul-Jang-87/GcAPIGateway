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
public class Entity_CampRt {
	
	@EmbeddedId
    private CampRt id;
	
	@Column(name = "CONTACTID_LIST_ID")
	private String contactLtId;
	
	@Column(name = "CONTACT_ID")
 	private String contactid;
	
	@Column(name = "IBM_SLTN_CNTA_HUB_ID")
	private int hubid;
	
	@Column(name = "CPSQ")
	private int cpsq;
	
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
	
}
	
