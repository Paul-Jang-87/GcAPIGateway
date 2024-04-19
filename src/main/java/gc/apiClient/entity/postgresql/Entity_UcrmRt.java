package gc.apiClient.entity.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import gc.apiClient.embeddable.UcrmCampRt;

@Data
@Entity
@Table(name = "CAMPRT_UCRM_W")
public class Entity_UcrmRt {
	
	@EmbeddedId
    private UcrmCampRt id;
	
	@Column(name = "DIVISIONID")
 	private String divisionid;
	
	public Entity_UcrmRt(){
		
	}
	
}
	
