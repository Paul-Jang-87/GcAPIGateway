package gc.apiClient.entity.oracle;

import gc.apiClient.embeddable.oracle.DataCallCustomer;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DATA_CALL_CUSTOMER")
public class Entity_DataCallCustomer {
	
	@EmbeddedId
    private DataCallCustomer id;
	
	@Column(name = "CUSTOMER_DATA01")
	private String customer_data01;
	
	@Column(name = "CUSTOMER_DATA02")
	private String customer_data02;
	
	@Column(name = "CUSTOMER_DATA03")
	private String customer_data03;
	
	public Entity_DataCallCustomer(){
		
	}
	
}
	
