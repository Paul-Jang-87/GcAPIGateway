package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import org.jetbrains.annotations.NotNull;

import gc.apiClient.embeddable.oracle.MasterServiceCode;

@Data
@Entity
@Table(name = "MASTER_SERVICE_CODE")
public class Entity_MasterServiceCode {
	
	@EmbeddedId
    private MasterServiceCode id;
	
	@NotNull
	@Column(name = "SERVICE_NAME")
 	private String service_name;
	
	@Column(name = "SERVICE_CODE_YPE")
 	private int service_code_type;
	
}
	
