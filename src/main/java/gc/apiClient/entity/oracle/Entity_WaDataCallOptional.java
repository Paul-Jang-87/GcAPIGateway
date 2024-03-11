package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.jetbrains.annotations.NotNull;

@Data
@Entity
@Table(name = "WA_DATA_CALL_OPTIONAL")
public class Entity_WaDataCallOptional {
	
	@Id
	@NotNull
	@Column(name = "WCSEQ")
 	private int wcseq;
	
	@Column(name = "DATA02")
	private String data02;
	
	public Entity_WaDataCallOptional(){
		
	}
	
}
	
