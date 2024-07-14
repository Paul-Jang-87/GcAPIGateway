package gc.apiClient.entity.postgresql;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CAMPMA")
public class Entity_CampMa {
	
	@Column(name = "COID")
	private int coid;

	@Id
	@NotNull
	@Column(name = "CPID")
	private String cpid;
	
	@Column(name = "CPNA")
 	private String cpna;
	
	@Column(name = "CONTACTLTID")
	private String contactltid;
	
	@Column(name = "CONTACTLTNM")
	private String contactltnm;
	
	@Column(name = "QUEUEID")
	private String queueid;
	
	@Column(name = "DIVISIONID")
	private String divisionid;
	
	@Column(name = "DIVISIONNM")
	private String divisionnm;
	
	@Column(name = "INSDATE")
	private String insdate;
	
	@Column(name = "MODDATE")
	private String moddate;

	public Entity_CampMa() {
	}

}
	
