package gc.apiClient.entity.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import gc.apiClient.embeddable.CallBotCampRt;

@Data
@Entity
@Table(name = "CAMPRT_CALLBOT_W")
public class Entity_CallbotRt {
	
	@EmbeddedId
    private CallBotCampRt id;
	
	@Column(name = "DIVISIONID")
 	private String divisionid;
	
	public Entity_CallbotRt(){
		
	}
	
}
	
