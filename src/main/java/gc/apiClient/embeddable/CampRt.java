package gc.apiClient.embeddable;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CampRt implements Serializable {
    private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "RLSQ")
	private int rlsq;
	
	@NotNull
	@Column(name = "COID")
    private int coid;
	
	public CampRt(){
		
	}

}
