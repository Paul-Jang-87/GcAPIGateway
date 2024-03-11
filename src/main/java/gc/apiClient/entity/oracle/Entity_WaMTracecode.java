package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import gc.apiClient.embeddable.oracle.WaDataCallTrace;

@Data
@Entity
@Table(name = "WA_M_TRACECODE")
public class Entity_WaMTracecode {
	
	@EmbeddedId
    private WaDataCallTrace id;
	
	@Column(name = "TRACECODE")
 	private String tracecode;
	
	public Entity_WaMTracecode(){
		
	}
}
	
