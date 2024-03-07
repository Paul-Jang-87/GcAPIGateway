package gc.apiClient.embeddable.oracle;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class WaDataCallTrace implements Serializable {

	@NotNull
	@Column(name = "WCSEQ")
	private int wcseq;
	
	@NotNull
	@Column(name = "TC_SEQ")
    private int tc_seq;

    public WaDataCallTrace() {
    }

    public int getWcseq() {return wcseq;}
    public int getTcSeq() {return tc_seq;}

	public void setWcseq(int wcseq) {this.wcseq = wcseq;}
	public void setTcSeq(int tc_seq) {this.tc_seq = tc_seq;}
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaDataCallTrace that = (WaDataCallTrace) o;
        return getWcseq() == that.getWcseq() &&
                Objects.equals(getTcSeq(), that.getTcSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTcSeq(), getWcseq());
    }


}
