package gc.apiClient.embeddable;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ApimCampRt implements Serializable {
    private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CAMP_ID")
	private String cpid;
	
	@NotNull
	@Column(name = "CAMP_SEQ")
    private String cpsq;
	
	public ApimCampRt(){
		
	}

}
