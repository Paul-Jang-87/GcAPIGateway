package gc.apiClient.embeddable;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Ucrm implements Serializable {

    private static final long serialVersionUID = 1L;
	@NotNull
	@Column(name = "ctiCmpnId")
    private String cpid;

	@NotNull
    @Column(name = "ctiCmpnSno")
    private String cpsq;
	
	public Ucrm(){
		
	}
	

}
