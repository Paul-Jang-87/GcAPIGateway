package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;


@Entity
@Table(name = "CAMPRT")
public class Entity_CampRt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Long id;

	@Column(name = "CAMP_ID")
	private String cpid;
	
	@Column(name = "CPSQ")
	private int cpsq;

	@Column(name = "CONTACTID_LIST_ID")
	private String contactLtId;
	
	@Column(name = "CONTACT_ID")
 	private String contactid;
	
	@Column(name = "IBM_SLTN_CNTA_HUB_ID")
 	private int hubid;
	
	@Column(name = "DIDT")
 	private Date didt;

	@Column(name = "DIRT")
	private int dirt;

	@Column(name = "DICT")
	private int dict;
	
	@Column(name = "COID")
	private String coid;
	

	public Entity_CampRt() {
	}

	public Entity_CampRt(Long id, String cpid,int cpsq, String contactLtId, String contactid, int hubid, Date didt,
			int dirt, int dict,String coid) {
		this.id = id;
		this.cpid = cpid;
		this.cpsq = cpsq;
		this.contactLtId = contactLtId;
		this.contactid = contactid;
		this.hubid = hubid;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
		this.coid = coid;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}

	public String getCpid() { return cpid; }
	public void setCpid(String cpid) {this.cpid = cpid;}
	
	public int getCpsq() {return cpsq;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	
	public String getContactLtId() {return contactLtId;}
	public void setContactLtId(String contactLtId) {this.contactLtId = contactLtId;}
	
	public String getContactId() {return contactid;}
	public void setContactId(String contactid) {this.contactid = contactid;}
	
	public int getHubId() {return hubid;}
	public void setHubId(int hubid) {this.hubid = hubid;}
	
	public Date getDidt() {return didt;}
	public void setDidt(Date didt) {this.didt = didt;}
	
	public int getDirt() {return dirt;}
	public void setDirt(int dirt) {this.dirt = dirt;}
	
	public int getDict() {return dict;}
	public void setDict(int dict) {this.dict = dict;}
	
	public String getCoid() { return coid; }
	public void setCoid(String coid) {this.coid = coid;}
}
	
