package gc.apiClient.entity.postgresql;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CAMPMA_D")

/**
 * 캠페인 마스터 테이블(CAMPMA)에서 레코드가 삭제가 되면 이쪽 이력 테이블러 넘어오게 된다. 제네시스에서 캠페인이 삭제가 되면 이벤트가 발생하고 그 이벤트는 
 * 이 GcApiGateWat앱의 api를 호출한다. 호출하면서 삭제된 캠페인의 정보를 같이 넘겨주게 되는데 그 정보를 바탕으로 CAMPMA테이블에서 해당레코드를 삭제하고
 * 이 CAMPMA_D 테이블에는 해당 레코드를 적재한다. 즉, 이 테이블에는 과거 삭제된 캠페인 정보들만 쌓이게 된다. 
 * 
 */

public class Entity_CampMa_D {
	
	@Column(name = "COID")
	private int coid;

	@Id
	@NotNull
	@Column(name = "CPID")
	private String cpid;
	
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

	public Entity_CampMa_D() {
	}

}
	
