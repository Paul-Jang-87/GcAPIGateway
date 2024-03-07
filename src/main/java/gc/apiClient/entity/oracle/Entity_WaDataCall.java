package gc.apiClient.entity.oracle;

import java.sql.Date;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "WA_DATA_CALL")
public class Entity_WaDataCall {
	
	@Id
	@NotNull
	@Column(name = "WCSEQ")
 	private int wcseq;
	
	@Column(name = "ICID")
	private String icid;
	
	@Column(name = "ENTERED_DATE")
	private Date entered_date;
	
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
	
	public Entity_WaDataCall() {
		
	}

	public Entity_WaDataCall(int wcseq, String icid, Date entered_date, int cep_reps_cnt,
			String cep_prss_stus_cd,Date cep_creation_date,Date cep_update_date) {
		
		this.wcseq=wcseq;
		this.icid=icid;
		this.entered_date=entered_date;
		this.cep_reps_cnt=cep_reps_cnt;
		this.cep_prss_stus_cd=cep_prss_stus_cd;
		this.cep_creation_date=cep_creation_date;
		this.cep_update_date=cep_update_date;
	}

	public int getWcseq() {return wcseq;}
	public String getIcid() { return icid; }
	public Date getEnteredDate() {return entered_date;}
	public int getCepRepsCnt() {return cep_reps_cnt;}
	public String getCepPrssStusCd() {return cep_prss_stus_cd;}
	public Date getCepCreationDate() {return cep_creation_date;}
	public Date getCepUpdateDate() {return cep_update_date;}
	
	public void setWcseq(int wcseq) {this.wcseq = wcseq;}
	public void setIcid(String icid) {this.icid = icid;}
	public void setEnteredDate(Date entered_date) {this.entered_date = entered_date;}
	public void setCepRepsCnt(int cep_reps_cnt) {this.cep_reps_cnt = cep_reps_cnt;}
	public void setCepPrssStusCd(String cep_prss_stus_cd) {this.cep_prss_stus_cd = cep_prss_stus_cd;}
	public void setCepCreationDate(Date cep_creation_date) {this.cep_creation_date = cep_creation_date;}
	public void setCepUpdateDate(Date cep_update_date) {this.cep_update_date = cep_update_date;}
	
	
}
	
