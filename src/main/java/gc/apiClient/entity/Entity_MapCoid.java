package gc.apiClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "MAPCOID")
public class Entity_MapCoid {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Long id;

	@Column(name = "COID")
	private String coid;

	@Column(name = "CPID")
	private String cpid;
	
	public Entity_MapCoid() {
	}

	public Entity_MapCoid(Long id, String coid, String cpid) {
		this.id = id;
		this.coid = coid;
		this.cpid = cpid;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	

	public String getCoid() { return coid; }
	public void setCoid(String coid) {this.coid = coid;}

	
	public String getCpid() {return cpid;}
	public void setCpid(String cpid) {this.cpid = cpid;}
	
}
	
