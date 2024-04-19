package gc.apiClient.embeddable;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CallBotCampRt implements Serializable {
    private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CPID")
	private String cpid;
	
	@NotNull
	@Column(name = "CPSQ")
    private String cpsq;
	
	public CallBotCampRt(){
		
	}

}
