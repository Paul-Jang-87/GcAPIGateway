package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "CONTACTLT")
public class Entity_ContactLt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Long id;

	@Column(name = "CPID")
	private String cpid;

	@Column(name = "CPSQ")
	private int cpsq;
	
	@Column(name = "CSKE")
 	private String cske;

	@Column(name = "TN01")
	private String tn01;

	@Column(name = "TN02")
	private String tn02;
	
	@Column(name = "TN03")
	private String tn03;
	
	@Column(name = "CSNA")
	private String csna;
	
	@Column(name = "TKDA")
	private String tkda;
	
	@Column(name = "FLAG")
	private String flag;
	

	public Entity_ContactLt() {
	}

	public Entity_ContactLt(Long id,  
	 String cpid,  
	 int cpsq,   
	 String cske,   
	 String tn01,   
	 String tn02,   
	 String tn03,   
	 String csna,   
	 String tkda,   
	 String flag) {
	this.id = id;
	this.cpid =  cpid;
	this.cpsq =  cpsq ;
    this.cske =  cske ;
	this.tn01 =  tn01 ;
	this.tn02 =  tn02 ;
	this.tn03 =  tn03 ;
	this.csna =  csna ;
	this.tkda =  tkda ;
	this.flag =  flag ;
		
		
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	

	public String getCpid() { return cpid; }
	public void setCpid(String cpid) {this.cpid = cpid;}

	
	public int getCpsq() {return cpsq;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	
	
	public String getCske() {return cske;}
	public void setCske(String cske) {this.cske = cske;}
	
	
	public String getTn01() {return tn01;}
	public void setTn01(String tn01) {this.tn01 = tn01;}
	
	
	public String getTn02() {return tn02;}
	public void setTn02(String tn02) {this.tn02 = tn02;}
	
	public String getTn03() { return tn03; }
	public void setTn03(String tn03) {this.tn03 = tn03;}
	
	public String getCsna() { return csna; }
	public void setCsna(String csna) {this.csna = csna;}
	
	public String getTkda() { return tkda; }
	public void setTkda(String tkda) {this.tkda = tkda;}
	
	public String getFlag() { return flag; }
	public void setFlag(String flag) {this.flag = flag;}
	
}
	
