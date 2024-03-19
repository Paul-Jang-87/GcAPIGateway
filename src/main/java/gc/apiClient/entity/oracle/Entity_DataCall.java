package gc.apiClient.entity.oracle;


import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "IPIVRADM.DATA_CALL_W")
public class Entity_DataCall {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_ENTERED_TIME")
	private String old_entered_time;
	
	@Column(name = "OLD_ENTERED_DATE")
	private String old_entered_date;
	
	@Column(name = "OLD_CALL_SEQ")
	private int old_call_seq;
	
	@Column(name = "OLD_ICID")
	private String old_icid;
	
	@Column(name = "OLD_SITE_CODE")
	private int old_site_code;
	
	@Column(name = "NEW_ENTERED_TIME")
	private String new_entered_time;
	
	@Column(name = "NEW_ENTERED_DATE")
	private String new_entered_date;
	
	@Column(name = "NEW_CALL_SEQ")
	private int new_call_seq;
	
	@Column(name = "NEW_ICID")
	private String new_icid;
	
	@Column(name = "NEW_SITE_CODE")
	private int new_site_code;
	
	public Entity_DataCall(){
		
	}
	
	
}
	
