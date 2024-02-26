package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "MAPCOID")
public class Entity_MapCoId {
	
	@Id
	@Column(name = "CPID")
	private String cpid;
	
	@Column(name = "COID")
	private int coid;

	public Entity_MapCoId() {
	}

	public Entity_MapCoId(int coid, String cpid) {
		this.coid = coid;
		this.cpid = cpid;
	}


	public int getCoid() { return coid; }
	public String getCpid() {return cpid;}
	
	public void setCoid(int coid) {this.coid = coid;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	
	
}
	
