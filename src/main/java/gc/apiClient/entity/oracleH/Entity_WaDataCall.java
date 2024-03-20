package gc.apiClient.entity.oracleH;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "IPIVRADM.WA_DATA_CALL_W")
public class Entity_WaDataCall {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_WCSEQ")
 	private int old_wcseq;
	
	@Column(name = "OLD_ICID")
	private String old_icid;
	
	@Column(name = "OLD_ENTERED_DATE")
	private Date old_entered_date;
	
	@Column(name = "NEW_WCSEQ")
 	private int new_wcseq;
	
	@Column(name = "NEW_ICID")
	private String new_icid;
	
	@Column(name = "NEW_ENTERED_DATE")
	private Date new_entered_date;
	
	
	public Entity_WaDataCall(){
		
	}
	
}
	
