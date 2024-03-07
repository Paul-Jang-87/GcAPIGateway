package gc.apiClient.entity.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "WA_DATA_CALL_OPTIONAL")
public class Entity_WaDataCallOptional {
	
	@Id
	@NotNull
	@Column(name = "WCSEQ")
 	private int wcseq;
	
	@Column(name = "DATA02")
	private String data02;
	
	public Entity_WaDataCallOptional() {
		
	}

	public Entity_WaDataCallOptional( int wcseq, String data02) {
		this.wcseq=wcseq;
		this.data02=data02;
	}

	public int getWcseq() {return wcseq;}
    public String getData02() {return data02;}
	
	public void setWcseq(int wcseq) {this.wcseq = wcseq;}
	public void setData02(String data02) {this.data02 = data02;}
	
	
}
	
