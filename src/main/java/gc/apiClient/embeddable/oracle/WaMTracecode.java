package gc.apiClient.embeddable.oracle;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class WaMTracecode implements Serializable {

	@NotNull
	@Column(name = "SITE_CODE")
	private int site_code;
	
	@NotNull
	@Column(name = "TRACECODE")
    private String tracecode;
	
	public WaMTracecode(){
		
	}

}
