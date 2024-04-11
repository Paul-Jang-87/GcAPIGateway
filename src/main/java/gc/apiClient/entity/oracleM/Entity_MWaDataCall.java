package gc.apiClient.entity.oracleM;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "WA_DATA_CALL_W")
public class Entity_MWaDataCall {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private Integer orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_WCSEQ")
 	private Integer old_wcseq;
	
	@Column(name = "OLD_ICID")
	private String old_icid;
	
	@Column(name = "OLD_ENTERED_DATE")
	private Date old_entered_date;
	
	@Column(name = "OLD_CTN")
	private String old_ctn;
	
	@Column(name = "NEW_WCSEQ")
 	private Integer new_wcseq;
	
	@Column(name = "NEW_ICID")
	private String new_icid;
	
	@Column(name = "NEW_ENTERED_DATE")
	private Date new_entered_date;
	
	@Column(name = "NEW_CTN")
	private String new_ctn;
	
	
	public Entity_MWaDataCall(){
		
	}
	
}
	
