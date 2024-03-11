package gc.apiClient.embeddable.oracle;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class WaDataCallTrace implements Serializable {

	@NotNull
	@Column(name = "WCSEQ")
	private int wcseq;
	
	@NotNull
	@Column(name = "TC_SEQ")
    private int tc_seq;
	
	public WaDataCallTrace(){
		
	}
}
