package gc.apiClient.entity.oracle;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "M_WA_DATA_CALL")
public class Entity_MWaDataCall {
	
	@Id
	@NotNull
	@Column(name = "WCSEQ")
 	private int wcseq;
	
	@Column(name = "ICID")
	private String icid;
	
	@Column(name = "ENTERED_DATE")
	private Date entered_date;
	
	@Column(name = "CNT")
	private String cnt;
	
	@NotNull
	@Column(name = "CEP_REPS_CNT")
	private int cep_reps_cnt;
	
	@NotNull
	@Column(name = "CEP_PRSS_STUS_CD")
	private String cep_prss_stus_cd;
	
	@NotNull
	@Column(name = "CEP_CREATION_DATE")
	private Date cep_creation_date;
	
	@Column(name = "CEP_UPDATE_DATE")
	private Date cep_update_date;
	
	public Entity_MWaDataCall(){
		
	}
	
}
	
