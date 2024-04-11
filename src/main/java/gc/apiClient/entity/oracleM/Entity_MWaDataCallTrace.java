package gc.apiClient.entity.oracleM;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.jetbrains.annotations.NotNull;


@Data
@Entity
@Table(name = "WA_DATA_CALL_TRACE_W")
public class Entity_MWaDataCallTrace {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private Integer orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_WCSEQ")
 	private Integer old_wcseq;
	
	@Column(name = "OLD_TC_SEQ")
 	private Integer old_tc_seq;
	
	@Column(name = "OLD_TRACECODE")
	private String old_tracecode;
	
	@Column(name = "NEW_WCSEQ")
 	private Integer new_wcseq;
	
	@Column(name = "NEW_TC_SEQ")
 	private Integer new_tc_seq;
	
	@Column(name = "NEW_TRACECODE")
	private String new_tracecode;
	
	public Entity_MWaDataCallTrace(){
		
	}
	
}
	
