package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;
import gc.apiClient.embeddable.CampRt;

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
	
	

	public Entity_CampRt() {
	}

	public Entity_CampRt( String cpid,int cpsq, int hubid, Date didt,
			int dirt, int dict, CampRt id,int coid,String contactLtId,
			String contactid,
			 String tkda) {
		this.cpid = cpid;
		this.cpsq = cpsq;
		this.hubid = hubid;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
		this.tkda = tkda ;
		this.contactLtId = contactLtId;
		this.contactid = contactid;
		this.id = new CampRt();
	}

	public int getRlsq() {return id.getRlsq();}
    public int getCoid() {return id.getCoid();}
	public int getCpsq() {return cpsq;}
	public int getHubId() {return hubid;}
	public int getDirt() {return dirt;}
	public int getDict() {return dict;}
	public String getTkda() { return tkda; }
	public String getCpid() { return cpid; }
	public String getContactLtid() { return contactLtId; }
	public String getContactid() { return contactid; }
	public Date getDidt() {return didt;}
	public CampRt getId() {return id;}
	
	public void setId(CampRt id) {this.id = id;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	public void setContactLtid(String contactLtId) {this.contactLtId = contactLtId;}
	public void setContactid(String contactid) {this.contactid = contactid;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	public void setHubId(int hubid) {this.hubid = hubid;}
	public void setDidt(Date didt) {this.didt = didt;}
	public void setDirt(int dirt) {this.dirt = dirt;}
	public void setDict(int dict) {this.dict = dict;}
	public void setTkda(String tkda) {this.tkda = tkda;}
	
	
}
	
