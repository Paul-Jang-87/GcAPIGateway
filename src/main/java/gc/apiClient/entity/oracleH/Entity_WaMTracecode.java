package gc.apiClient.entity.oracleH;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "IPIVRADM.WA_M_TRACECODE_W")
public class Entity_WaMTracecode {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_SITE_CODE")
	private int old_site_code;
	
	@Column(name = "OLD_TRACECODE")
    private String old_tracecode;
	
	@Column(name = "OLD_TRACECODE_NAME")
    private String old_tracecode_name;
	
	@Column(name = "OLD_TRACECODE_TYPE")
    private int old_tracecode_type;
	
	@Column(name = "NEW_SITE_CODE")
	private int new_site_code;
	
	@Column(name = "NEW_TRACECODE")
    private String new_tracecode;
	
	@Column(name = "NEW_TRACECODE_NAME")
    private String new_tracecode_name;
	
	@Column(name = "NEW_TRACECODE_TYPE")
    private int new_tracecode_type;
	
	public Entity_WaMTracecode(){
		
	}
}
	
