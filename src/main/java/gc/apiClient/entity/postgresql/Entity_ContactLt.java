package gc.apiClient.entity.postgresql;

import gc.apiClient.embeddable.ContactLtId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CONTACTLT")

public class Entity_ContactLt {
	
	@EmbeddedId
    private ContactLtId id;
	
	@Column(name = "CSKE")
 	private String cske;

	@Column(name = "TNO1")
	private String tno1;

	@Column(name = "TNO2")
	private String tno2;
	
	@Column(name = "TNO3")
	private String tno3;
	
	@Column(name = "CSNA")
	private String csna;
	
	@Column(name = "TKDA")
	private String tkda;
	
	@Column(name = "FLAG")
	private String flag;
	
}
	
