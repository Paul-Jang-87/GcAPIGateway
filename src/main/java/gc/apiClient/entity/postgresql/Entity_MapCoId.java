package gc.apiClient.entity.postgresql;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MAPCOID")
public class Entity_MapCoId {
	
	@Id
	@NotNull
	@Column(name = "CPID")
	private String cpid;
	
	@Column(name = "COID")
	private int coid;

	public Entity_MapCoId() {
	}
	
}
	
