package gc.apiClient.embeddable.oracle;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class DataCallCustomer implements Serializable {

	@NotNull
	@Column(name = "CSEQ")
	private int cseq;
	
	@NotNull
	@Column(name = "ENTERED_DATE")
	private String entered_date;
	
	@NotNull
	@Column(name = "SITE_CODE")
	private int site_code;
	
	@NotNull
	@Column(name = "ICID")
    private String icid;
	
	@NotNull
	@Column(name = "CALL_SEQ")
    private int call_seq;
	
}
