package gc.apiClient.entity.oracle;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "IPIVRADM_MOBILE.DATA_CALL_CUSTOMER_W")
public class Entity_MDataCallCustomer {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_CUSTOMER_DATA01")
	private String old_customer_data01;
	
	@Column(name = "OLD_CUSTOMER_DATA02")
	private String old_customer_data02;
	
	@Column(name = "OLD_CUSTOMER_DATA03")
	private String old_customer_data03;
	
	@Column(name = "OLD_CSEQ")
	private int old_cseq;
	
	@Column(name = "OLD_ENTERED_DATE")
	private String old_entered_date;
	
	@Column(name = "OLD_CALL_SEQ")
	private int old_call_seq;
	
	@Column(name = "OLD_ICID")
	private String old_icid;
	
	@Column(name = "OLD_SITE_CODE")
	private int old_site_code;
	
	@Column(name = "NEW_CUSTOMER_DATA01")
	private String new_customer_data01;
	
	@Column(name = "NEW_CUSTOMER_DATA02")
	private String new_customer_data02;
	
	@Column(name = "NEW_CUSTOMER_DATA03")
	private String new_customer_data03;
	
	@Column(name = "NEW_CSEQ")
	private int new_cseq;
	
	@Column(name = "NEW_ENTERED_DATE")
	private String new_entered_date;
	
	@Column(name = "NEW_CALL_SEQ")
	private int new_call_seq;
	
	@Column(name = "NEW_ICID")
	private String new_icid;
	
	@Column(name = "NEW_SITE_CODE")
	private int new_site_code;
	
	public Entity_MDataCallCustomer(){
		
	}
	
}
	
