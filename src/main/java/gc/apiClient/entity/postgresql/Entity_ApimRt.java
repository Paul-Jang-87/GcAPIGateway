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
public class Entity_ApimRt {
	
	@EmbeddedId
    private ApimCampRt id;
	
	@Column(name = "DIVISION_ID")
 	private String divisionid;
	
	public Entity_ApimRt(){
		
	}
	
}
	
