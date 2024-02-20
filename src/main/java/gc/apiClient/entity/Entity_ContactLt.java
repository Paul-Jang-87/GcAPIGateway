package gc.apiClient.entity;

import gc.apiClient.embeddable.ContactLtId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "CONTACTLT")

public class Entity_ContactLt {
	
	@EmbeddedId
    private ContactLtId id;

	@Column(name = "CPID")
	private String cpid;

	@Column(name = "CPSQ")
	private int cpsq;
	
	@Column(name = "CSKE")
 	private String cske;

	@Column(name = "TNO1")
	private String tno1;

	@Column(name = "TNO2")
	private String tno2;
	
	@Column(name = "TNO3")
	private String tno3;
	
	@Column(name = "CSNA")
	private String csna;
	
	@Column(name = "TKDA")
	private String tkda;
	
	@Column(name = "FLAG")
	private String flag;
	

	public Entity_ContactLt() {
		
	}

	public Entity_ContactLt(
	 String cpid,  
	 int cpsq,   
	 String cske,   
	 String tno1,   
	 String tno2,   
	 String tno3,   
	 String csna,   
	 String tkda,   
	 String flag,
	 ContactLtId id) {
	this.id = new ContactLtId();
	this.cpid =  cpid;
	this.cpsq =  cpsq ;
    this.cske =  cske ;
	this.tno1 =  tno1 ;
	this.tno2 =  tno2 ;
	this.tno3 =  tno3 ;
	this.csna =  csna ;
	this.tkda =  tkda ;
	this.flag =  flag ;
		
		
	}

	public ContactLtId getId() {return id;}
	public void setId(ContactLtId id) {this.id = id;}

	public String getCpid() { return cpid; }
	public void setCpid(String cpid) {this.cpid = cpid;}

	
	public int getCpsq() {return cpsq;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	
	
	public String getCske() {return cske;}
	public void setCske(String cske) {this.cske = cske;}
	
	
	public String getTn01() {return tno1;}
	public void setTn01(String tno1) {this.tno1 = tno1;}
	
	
	public String getTn02() {return tno2;}
	public void setTn02(String tno2) {this.tno2 = tno2;}
	
	public String getTn03() { return tno3; }
	public void setTn03(String tno3) {this.tno3 = tno3;}
	
	public String getCsna() { return csna; }
	public void setCsna(String csna) {this.csna = csna;}
	
	public String getTkda() { return tkda; }
	public void setTkda(String tkda) {this.tkda = tkda;}
	
	public String getFlag() { return flag; }
	public void setFlag(String flag) {this.flag = flag;}
	
}
	
