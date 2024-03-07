package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import gc.apiClient.embeddable.oracle.WaMTracecode;

@Entity
@Table(name = "WA_M_TRACECODE")
public class Entity_WaDataCallTrace {
	
	@EmbeddedId
    private WaMTracecode id;
	
	@Column(name = "TRACECODE_NAME")
 	private String tracecode_name;
	
	@Column(name = "TRACECODE_TYPE")
	private int tracecode_type;
	
	public Entity_WaDataCallTrace() {
		
	}

	public Entity_WaDataCallTrace( String tracecode_name, int tracecode_type, WaMTracecode id) {
		this.tracecode_name=tracecode_name;
		this.tracecode_type=tracecode_type;
		this.id = new WaMTracecode();
	}

	public WaMTracecode getId() {return id;}
	public int getSitecode() {return id.getSite_code();}
    public String getTraceCode() {return id.getTracecode();}
	public String getTracecodeName() { return tracecode_name; }
	public int getTracecodeType() {return tracecode_type;}
	
	public void setId(WaMTracecode id) {this.id = id;}
	public void setTracecodeName(String tracecode_name) {this.tracecode_name = tracecode_name;}
	public void setTracecodeType(int tracecode_type) {this.tracecode_type = tracecode_type;}
	
	
}
	
