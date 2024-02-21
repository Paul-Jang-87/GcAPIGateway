package gc.apiClient.embeddable;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CampRt implements Serializable {

	
	@Column(name = "CONTACTID_LIST_ID")
	private String contactLtId;
	
	@Column(name = "CONTACT_ID")
 	private String contactid;
	
    public CampRt() {
    }

    public String getContactLtId() {return contactLtId;}
    public String getContactId() {return contactid;}

	public void setContactLtId(String contactLtId) {this.contactLtId = contactLtId;}
	public void setContactId(String contactid) {this.contactid = contactid;}
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampRt that = (CampRt) o;
        return getContactLtId() == that.getContactLtId() &&
                Objects.equals(getContactId(), that.getContactId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContactId(), getContactLtId());
    }


}
