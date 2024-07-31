package gc.apiClient.entity.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import gc.apiClient.embeddable.ApimCampRt;

@Data
@Entity
@Table(name = "CAMPRT_UCUBE_W")
/**
 * 'CAMPRT_UCUBE_W'테이블을 대표하는 JPA 앤티티이다. 
 *  APIM 발신 결과 쉐도우 테이블이다. 1분마다 테이블에 있는 데이터들을 가져와서 APIM쪽으로 보낼 메시지 포맷에 맞춰 제가공한 뒤 카프카로 메시지를 보낸다. 
 */
public class Entity_ApimRt {
	
	@EmbeddedId //@EmbeddedId가 있다는 의미는 이 테이블'CAMPRT_UCUBE_W'이 복합키를 갖고 있다는 의미이다. 그 복합키를 ID로 하여레코드를 구별할 키 값으로 쓴다는 의미이다. 
    private ApimCampRt id; 
	
	@Column(name = "DIVISION_ID")
 	private String divisionid;
	
	public Entity_ApimRt(){
		
	}
	
}
	
