package gc.apiClient.interfaceCollection;

import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;

public interface InterfaceKafMsg {
	
	String maMessage (Entity_CampMa enCampMa, String datachgcd) throws Exception;
	String rtMessage (Entity_CampRt enCampRt) throws Exception;

}
