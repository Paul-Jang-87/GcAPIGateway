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
	
	@Column(name = "DIDT")
 	private Date didt;
	
	@Column(name = "COID")
    private int coid;

	public Entity_CampRt() {
	}

	public Entity_CampRt( String cpid,int cpsq, int hubid, Date didt,
			int dirt, int dict, CampRt id,int coid) {
		this.cpid = cpid;
		this.cpsq = cpsq;
		this.hubid = hubid;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
		this.coid = coid;
		this.id = new CampRt();
	}

	public String getContactLtId() {return id.getContactLtId();}
    public String getContactId() {return id.getContactId();}
	public int getCoid() {return coid;}
	public int getCpsq() {return cpsq;}
	public int getHubId() {return hubid;}
	public int getDirt() {return dirt;}
	public int getDict() {return dict;}
	public String getCpid() { return cpid; }
	public Date getDidt() {return didt;}
	public CampRt getId() {return id;}
	
	public void setId(CampRt id) {this.id = id;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	public void setCoid(int coid) {this.coid = coid;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	public void setHubId(int hubid) {this.hubid = hubid;}
	public void setDidt(Date didt) {this.didt = didt;}
	public void setDirt(int dirt) {this.dirt = dirt;}
	public void setDict(int dict) {this.dict = dict;}
	
	
}
	
