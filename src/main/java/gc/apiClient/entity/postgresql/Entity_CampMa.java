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

	public Entity_CampMa() {
	}

}
	
