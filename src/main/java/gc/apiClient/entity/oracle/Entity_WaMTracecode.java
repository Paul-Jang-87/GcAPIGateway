package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import gc.apiClient.embeddable.oracle.WaDataCallTrace;

@Entity
@Table(name = "WA_M_TRACECODE")
public class Entity_WaMTracecode {
	
	@EmbeddedId
    private WaDataCallTrace id;
	
	@Column(name = "TRACECODE")
 	private String tracecode;
	
	public Entity_WaMTracecode() {
		
	}

	public Entity_WaMTracecode( String tracecode, WaDataCallTrace id) {
		this.tracecode = tracecode;
		this.id = new WaDataCallTrace();
	}

	public WaDataCallTrace getId() {return id;}
    public int getWcseq() {return id.getWcseq();}
    public int getTcSeq() {return id.getTcSeq();}
    public String getTraceCode() {return tracecode;}
	
	public void setId(WaDataCallTrace id) {this.id = id;}
	public void setTraceCode(String tracecode) {this.tracecode = tracecode;}
	
	
}
	
