package gc.apiClient.entity.oracleH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.jetbrains.annotations.NotNull;

@Data
@Entity
@Table(name = "IPIVRADM.WA_DATA_CALL_OPTIONAL_W")
public class Entity_WaDataCallOptional {
	
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_WCSEQ")
 	private int old_wcseq;
	
	@Column(name = "OLD_DATA02")
	private String old_data02;
	
	@Column(name = "NEW_WCSEQ")
 	private int new_wcseq;
	
	@Column(name = "NEW_DATA02")
	private String new_data02;
	
	public Entity_WaDataCallOptional(){
		
	}
	
}
	
