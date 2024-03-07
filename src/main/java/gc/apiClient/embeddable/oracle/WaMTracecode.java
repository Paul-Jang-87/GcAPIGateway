package gc.apiClient.embeddable.oracle;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class WaMTracecode implements Serializable {

	@NotNull
	@Column(name = "SITE_CODE")
	private int site_code;
	
	@NotNull
	@Column(name = "TRACECODE")
    private String tracecode;

    public WaMTracecode() {
    }

    public int getSite_code() {return site_code;}
    public String getTracecode() {return tracecode;}

	public void setSite_code(int site_code) {this.site_code = site_code;}
	public void setTracecode(String tracecode) {this.tracecode = tracecode;}
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaMTracecode that = (WaMTracecode) o;
        return getSite_code() == that.getSite_code() &&
                Objects.equals(getTracecode(), that.getTracecode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTracecode(), getSite_code());
    }


}
