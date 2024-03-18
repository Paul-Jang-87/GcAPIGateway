package gc.apiClient.entity;

import lombok.Data;

@Data
public class Entity_CampRtJson {
	
	private String topcDataIsueDtm;
	private int ibmHubId;
	private String lastAttempt;
	private int lastResult;
 	private int totAttempt;
 	private String centerCd;

	public Entity_CampRtJson() {
	}

}
	
