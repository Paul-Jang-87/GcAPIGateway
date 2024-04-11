package gc.apiClient.entity.oracleH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.jetbrains.annotations.NotNull;


@Data
@Entity
@Table(name = "MASTER_SERVICE_CODE_W")
public class Entity_MasterServiceCode {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private Integer orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_SERVICE_NAME")
 	private String old_service_name;
	
	@Column(name = "OLD_SITE_CODE")
	private Integer old_site_code;
	
	@Column(name = "OLD_SERVICE_CODE")
 	private String old_service_code;
	
	@Column(name = "OLD_SERVICE_CODE_TYPE")
 	private Integer old_service_code_type;
	
	@Column(name = "NEW_SERVICE_NAME")
 	private String new_service_name;
	
	@Column(name = "NEW_SITE_CODE")
	private Integer new_site_code;
	
	@Column(name = "NEW_SERVICE_CODE")
 	private String new_service_code;
	
	@Column(name = "NEW_SERVICE_CODE_TYPE")
 	private Integer new_service_code_type;
	
	public Entity_MasterServiceCode(){
		
	}
	
}
	
