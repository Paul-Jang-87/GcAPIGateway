package gc.apiClient.entity.postgresql;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CAMPMA")
/**
 * 
 * 캠페인에 대한 정보를 가지고 있다. 캠페인 아이디는 고유하기 때문에 작업 요청이나 콜 혹은 발신 대상자 관련 이슈 사항이 들어 올 때 
 * 주로 캠페인 아이디를 기준으로 해서 로그를 찾아보거나 데이터 베이스를 조회한다. 한 캠페인에 대한 대부분의 정보들은 이 테이블에서 찾아볼 수 있다.  
 *  
 */

public class Entity_CampMa {

	@Id
	@NotNull
	@Column(name = "CPID")
	private String cpid;
	
	@Column(name = "COID")
	private int coid;
	
	@Column(name = "CPNA")
 	private String cpna;
	
	@Column(name = "CONTACTLTID")
	private String contactltid;
	
	@Column(name = "CONTACTLTNM")
	private String contactltnm;
	
	@Column(name = "QUEUEID")
	private String queueid;
	
	@Column(name = "DIVISIONID")
	private String divisionid;
	
	@Column(name = "DIVISIONNM")
	private String divisionnm;
	
	@Column(name = "INSDATE")
	private String insdate;
	
	@Column(name = "MODDATE")
	private String moddate;

	public Entity_CampMa() {
	}

}
	
