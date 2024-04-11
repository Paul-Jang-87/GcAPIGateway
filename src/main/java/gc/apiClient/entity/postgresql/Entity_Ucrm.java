package gc.apiClient.entity.postgresql;

import gc.apiClient.embeddable.Ucrm;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "UCRMLT")

public class Entity_Ucrm {
	
	@EmbeddedId
    private Ucrm id;
	
	@Column(name = "hldrCustId")
 	private String hldrCustId;

	@Column(name = "tlno")
	private String tlno;

	@Column(name = "trdtCntn")
	private String trdtCntn;
	
	@Column(name = "workDivsCd")
	private String workDivsCd;
	
	@Column(name = "topcDataIsueSno")
	private String topcDataIsueSno;
	
	@Column(name = "topcDataIsueDtm")
	private String topcDataIsueDtm;
	
	@Column(name = "subssDataDelYn")
	private String subssDataDelYn;
	
	@Column(name = "subssDataChgCd")
	private String subssDataChgCd;
	
	@Column(name = "custNm")
	private String custNm;
	
	@Column(name = "cablTlno")
	private String cablTlno;
	
	@Column(name = "custTlno")
	private String custTlno;
	
	public Entity_Ucrm(){
		
	}
	
}
	
