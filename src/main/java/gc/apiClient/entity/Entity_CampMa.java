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
	@Column(name = "msg_id")//postgreSQL에서 기본 키로서 역할. 레코드가 하나씩 생성될 때마다 맨앞에 자동으로 붙음. 레코드 하나당 1,2,3.. 이런 식으로
	  						//이게 기본키로서 역할을 하기 때문에 로컬 테스트 환경에서는 매번 데이터 무결성에 신경 안 써도 되서 편함. 단위 테스트, 통합 테스트 시 
	  						//삭제 예정. 
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
	
