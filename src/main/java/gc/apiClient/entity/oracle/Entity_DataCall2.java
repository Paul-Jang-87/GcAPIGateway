package gc.apiClient.entity.oracle;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

import gc.apiClient.embeddable.oracle.DataCall;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DATA_CALL")
public class Entity_DataCall2 {
	
	@EmbeddedId
    private DataCall id;
	
	@NotNull
	@Column(name = "ENTERED_TIME")
	private String entered_time;
	
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
	
}
	
