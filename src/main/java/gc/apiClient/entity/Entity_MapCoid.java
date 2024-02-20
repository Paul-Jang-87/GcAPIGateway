package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "MAPCOID")
public class Entity_MapCoid {
	
	
	@Column(name = "COID")
	private int coid;

	@Id
	@Column(name = "CPID")
	private String cpid;
	
	public Entity_MapCoid() {
	}

	public Entity_MapCoid(int coid, String cpid) {
		this.coid = coid;
		this.cpid = cpid;
	}


	public int getCoid() { return coid; }
	public void setCoid(int coid) {this.coid = coid;}

	
	public String getCpid() {return cpid;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	
}
	
