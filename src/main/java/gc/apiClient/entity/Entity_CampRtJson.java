package gc.apiClient.entity;

import lombok.Data;

@Data
public class Entity_CampRtJson {
	
	private String topcDataIsueDtm;
	private int ibmHubId;
	private String lastAttempt;
	private int lastResult;
 	private int totAttempt;
 	private String cpid;
 	private String centerCd;
 	private int camp_seq;

	public Entity_CampRtJson() {
	}

}
	
