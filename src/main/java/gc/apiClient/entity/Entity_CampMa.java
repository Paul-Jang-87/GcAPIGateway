package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "CAMPMA")
public class Entity_CampMa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Long id;

	@Column(name = "COID")
	private String coid;

	@Column(name = "CPID")
	private String cpid;
	
	@Column(name = "CPNA")
 	private String cpna;

	public Entity_CampMa() {
	}

	public Entity_CampMa(Long id, String coid, String cpid, String cpna) {
		this.id = id;
		this.coid = coid;
		this.cpid = cpid;
		this.cpna = cpna;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	

	public String getCoid() { return coid; }
	public void setCoid(String coid) {this.coid = coid;}

	
	public String getCpid() {return cpid;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	
	
	public String getCpna() {return cpna;}
	public void setCpna(String cpna) {this.cpna = cpna;}
	
	
}
	
