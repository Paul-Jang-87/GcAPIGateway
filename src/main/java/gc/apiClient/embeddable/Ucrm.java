package gc.apiClient.embeddable;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Ucrm implements Serializable {

	@NotNull
	@Column(name = "ctiCmpnId")
    private String cpid;

	@NotNull
    @Column(name = "ctiCmpnSno")
    private String cpsq;
	
	public Ucrm(){
		
	}
	

}
