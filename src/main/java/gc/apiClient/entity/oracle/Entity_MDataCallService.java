package gc.apiClient.entity.oracle;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "IPIVRADM_MOBILE.DATA_CALL_SERVICE_W")
public class Entity_MDataCallService {
	
	@Id
	@NotNull
	@Column(name = "ORDERID")
	private int orderid;
	
	@NotNull
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "OLD_ENTERED_DATE")
	private String old_entered_date;
	
	@Column(name = "OLD_CALL_SEQ")
	private int old_call_seq;
	
	@Column(name = "OLD_ICID")
	private String old_icid;
	
	@Column(name = "OLD_SITE_CODE")
	private int old_site_code;
	
	@Column(name = "NEW_ENTERED_DATE")
	private String new_entered_date;
	
	@Column(name = "NEW_CALL_SEQ")
	private int new_call_seq;
	
	@Column(name = "NEW_ICID")
	private String new_icid;
	
	@Column(name = "NEW_SITE_CODE")
	private int new_site_code;
	
	@Column(name = "OLD_SERVICE_CODE01")
	private String old_service_code01;
	
	@Column(name = "OLD_SERVICE_CODE02")
	private String old_service_code02;
	
	@Column(name = "OLD_SERVICE_CODE03")
	private String old_service_code03;
	
	@Column(name = "OLD_SERVICE_CODE04")
	private String old_service_code04;
	
	@Column(name = "OLD_SERVICE_CODE05")
	private String old_service_code05;
	
	@Column(name = "OLD_SERVICE_CODE06")
	private String old_service_code06;
	
	@Column(name = "OLD_SERVICE_CODE07")
	private String old_service_code07;
	
	@Column(name = "OLD_SERVICE_CODE08")
	private String old_service_code08;
	
	@Column(name = "OLD_SERVICE_CODE09")
	private String old_service_code09;
	
	@Column(name = "OLD_SERVICE_CODE10")
	private String old_service_code10;
	
	@Column(name = "OLD_SERVICE_CODE11")
	private String old_service_code11;
	
	@Column(name = "OLD_SERVICE_CODE12")
	private String old_service_code12;
	
	@Column(name = "OLD_SERVICE_CODE13")
	private String old_service_code13;
	
	@Column(name = "OLD_SERVICE_CODE14")
	private String old_service_code14;
	
	@Column(name = "OLD_SERVICE_CODE15")
	private String old_service_code15;
	
	@Column(name = "OLD_SERVICE_CODE16")
	private String old_service_code16;
	
	@Column(name = "OLD_SERVICE_CODE17")
	private String old_service_code17;
	
	@Column(name = "OLD_SERVICE_CODE18")
	private String old_service_code18;
	
	@Column(name = "OLD_SERVICE_CODE19")
	private String old_service_code19;
	
	@Column(name = "OLD_SERVICE_CODE20")
	private String old_service_code20;
	
	@Column(name = "OLD_SERVICE_CODE21")
	private String old_service_code21;
	
	@Column(name = "OLD_SERVICE_CODE22")
	private String old_service_code22;
	
	@Column(name = "OLD_SERVICE_CODE23")
	private String old_service_code23;
	
	@Column(name = "OLD_SERVICE_CODE24")
	private String old_service_code24;
	
	@Column(name = "OLD_SERVICE_CODE25")
	private String old_service_code25;
	
	@Column(name = "OLD_SERVICE_CODE26")
	private String old_service_code26;
	
	@Column(name = "OLD_SERVICE_CODE27")
	private String old_service_code27;
	
	@Column(name = "OLD_SERVICE_CODE28")
	private String old_service_code28;
	
	@Column(name = "OLD_SERVICE_CODE29")
	private String old_service_code29;
	
	@Column(name = "OLD_SERVICE_CODE30")
	private String old_service_code30;
	
	@Column(name = "OLD_SERVICE_CODE31")
	private String old_service_code31;
	
	@Column(name = "OLD_SERVICE_CODE32")
	private String old_service_code32;
	
	@Column(name = "OLD_SERVICE_CODE33")
	private String old_service_code33;
	
	@Column(name = "OLD_SERVICE_CODE34")
	private String old_service_code34;
	
	@Column(name = "OLD_SERVICE_CODE35")
	private String old_service_code35;
	
	@Column(name = "OLD_SERVICE_CODE36")
	private String old_service_code36;
	
	@Column(name = "OLD_SERVICE_CODE37")
	private String old_service_code37;
	
	@Column(name = "OLD_SERVICE_CODE38")
	private String old_service_code38;
	
	@Column(name = "OLD_SERVICE_CODE39")
	private String old_service_code39;
	
	@Column(name = "OLD_SERVICE_CODE40")
	private String old_service_code40;
	
	@Column(name = "NEW_SERVICE_CODE1")
	private String NEW_service_code1;
	
	@Column(name = "NEW_SERVICE_CODE2")
	private String new_service_code2;
	
	@Column(name = "NEW_SERVICE_CODE3")
	private String new_service_code3;
	
	@Column(name = "NEW_SERVICE_CODE4")
	private String new_service_code4;
	
	@Column(name = "NEW_SERVICE_CODE5")
	private String new_service_code5;
	
	@Column(name = "NEW_SERVICE_CODE6")
	private String new_service_code6;
	
	@Column(name = "NEW_SERVICE_CODE7")
	private String new_service_code7;
	
	@Column(name = "NEW_SERVICE_CODE8")
	private String new_service_code8;
	
	@Column(name = "NEW_SERVICE_CODE9")
	private String new_service_code9;
	
	@Column(name = "NEW_SERVICE_CODE10")
	private String new_service_code10;
	
	@Column(name = "NEW_SERVICE_CODE11")
	private String new_service_code11;
	
	@Column(name = "NEW_SERVICE_CODE12")
	private String new_service_code12;
	
	@Column(name = "NEW_SERVICE_CODE13")
	private String new_service_code13;
	
	@Column(name = "NEW_SERVICE_CODE14")
	private String new_service_code14;
	
	@Column(name = "NEW_SERVICE_CODE15")
	private String new_service_code15;
	
	@Column(name = "NEW_SERVICE_CODE16")
	private String new_service_code16;
	
	@Column(name = "NEW_SERVICE_CODE17")
	private String new_service_code17;
	
	@Column(name = "NEW_SERVICE_CODE18")
	private String new_service_code18;
	
	@Column(name = "NEW_SERVICE_CODE19")
	private String new_service_code19;
	
	@Column(name = "NEW_SERVICE_CODE20")
	private String new_service_code20;
	
	@Column(name = "NEW_SERVICE_CODE21")
	private String new_service_code21;
	
	@Column(name = "NEW_SERVICE_CODE22")
	private String new_service_code22;
	
	@Column(name = "NEW_SERVICE_CODE23")
	private String new_service_code23;
	
	@Column(name = "NEW_SERVICE_CODE24")
	private String new_service_code24;
	
	@Column(name = "NEW_SERVICE_CODE25")
	private String new_service_code25;
	
	@Column(name = "NEW_SERVICE_CODE26")
	private String new_service_code26;
	
	@Column(name = "NEW_SERVICE_CODE27")
	private String new_service_code27;
	
	@Column(name = "NEW_SERVICE_CODE28")
	private String new_service_code28;
	
	@Column(name = "NEW_SERVICE_CODE29")
	private String new_service_code29;
	
	@Column(name = "NEW_SERVICE_CODE30")
	private String new_service_code30;
	
	@Column(name = "NEW_SERVICE_CODE31")
	private String new_service_code31;
	
	@Column(name = "NEW_SERVICE_CODE32")
	private String new_service_code32;
	
	@Column(name = "NEW_SERVICE_CODE33")
	private String new_service_code33;
	
	@Column(name = "NEW_SERVICE_CODE34")
	private String new_service_code34;
	
	@Column(name = "NEW_SERVICE_CODE35")
	private String new_service_code35;
	
	@Column(name = "NEW_SERVICE_CODE36")
	private String new_service_code36;
	
	@Column(name = "NEW_SERVICE_CODE37")
	private String new_service_code37;
	
	@Column(name = "NEW_SERVICE_CODE38")
	private String new_service_code38;
	
	@Column(name = "NEW_SERVICE_CODE39")
	private String new_service_code39;
	
	@Column(name = "NEW_SERVICE_CODE40")
	private String new_service_code40;
	
	
	public Entity_MDataCallService(){
		
	}
	
}
	
