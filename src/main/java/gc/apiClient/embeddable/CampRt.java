package gc.apiClient.embeddable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CampRt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "RLSQ")
    private int rlsq;

    @Column(name = "COID")
    private int coid;

    // Required default constructor
    public CampRt() {
    }

    public int getRlsq() {return rlsq;}
    public int getCoid() {return coid;}

    public void setRlsq(int rlsq) {this.rlsq = rlsq;}
    public void setCoid(int coid) {this.coid = coid;}


}
