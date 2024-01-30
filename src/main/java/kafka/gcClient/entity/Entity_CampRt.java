package kafka.gcClient.entity;

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

	@Column(name = "CLTID")
	private String cltid;

	@Column(name = "CONTACTID")
	private String contactId;
	
	@Column(name = "DIDT")
 	private Date didt;

	@Column(name = "DIRT")
	private double dirt;

	@Column(name = "DICT")
	private double dict;
	

	public Entity_CampRt() {
	}

	public Entity_CampRt(Long id, String cltid, String contactId, Date didt, double dirt, double dict) {
		this.id = id;
		this.cltid = cltid;
		this.contactId = contactId;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	

	public String getCltid() { return cltid; }
	public void setCltid(String cltid) {this.cltid = cltid;}

	
	public String getContactId() {return contactId;}
	public void setContactId(String contactId) {this.contactId = contactId;}
	
	
	public Date getDidt() {return didt;}
	public void setDidt(Date didt) {this.didt = didt;}
	
	
	public double getDirt() {return dirt;}
	public void setDirt(double dirt) {this.dirt = dirt;}
	
	
	public double getDict() {return dict;}
	public void setDict(double dict) {this.dict = dict;}
	
}
	
