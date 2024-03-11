package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import gc.apiClient.embeddable.oracle.WaDataCallTrace;

@Data
@Entity
@Table(name = "WA_DATA_CALL_TRACE")
public class Entity_WaDataCallTrace {
	
	@EmbeddedId
    private WaDataCallTrace id;
	
	@Column(name = "TRACECODE")
	private String tracecode;
	
	public Entity_WaDataCallTrace(){
		
	}
	
}
	
