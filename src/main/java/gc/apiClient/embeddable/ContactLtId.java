package gc.apiClient.embeddable;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ContactLtId implements Serializable {

	@NotNull
	@Column(name = "CPID")
    private String cpid;

	@NotNull
    @Column(name = "CPSQ")
    private int cpsq;

    // Required default constructor
    public ContactLtId() {
    }

    public String getCpid() {return cpid;}
    public int getCpsq() {return cpsq;}

    public void setCpid(String cpid) {this.cpid = cpid;}
    public void setCpsq(int cpsq) {this.cpsq = cpsq;}
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactLtId that = (ContactLtId) o;
        return getCpsq() == that.getCpsq() &&
                Objects.equals(getCpid(), that.getCpid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCpid(), getCpsq());
    }


}
