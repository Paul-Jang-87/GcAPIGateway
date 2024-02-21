package gc.apiClient.embeddable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ContactLtId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CPID")
    private String cpid;

    @Column(name = "CPSQ")
    private int cpsq;

    // Required default constructor
    public ContactLtId() {
    }

    public String getCpid() {return cpid;}
    public int getCpsq() {return cpsq;}

    public void setCpid(String cpid) {this.cpid = cpid;}
    public void setCpsq(int cpsq) {this.cpsq = cpsq;}


}
