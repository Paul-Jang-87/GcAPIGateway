package gc.apiClient.embeddable;

import java.io.Serializable;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ContactLtId implements Serializable {

	@NotNull
	@Column(name = "CPID")
    private String cpid;

	@NotNull
    @Column(name = "CPSQ")
    private int cpsq;

}
