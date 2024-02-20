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

    // Getter and setter for CPID
    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    // Getter and setter for CPSQ
    public int getCpsq() {
        return cpsq;
    }

    public void setCpsq(int cpsq) {
        this.cpsq = cpsq;
    }

    // Override equals and hashCode methods if necessary

}
