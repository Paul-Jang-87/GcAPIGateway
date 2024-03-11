package gc.apiClient.interfaceCollection;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;

public interface InterfaceDBOracle {
	
	//insert
	Entity_WaDataCallOptional InsertWaDataCallOptional(Entity_WaDataCallOptional entityWaDataCallOptional);
	
	//select
	Entity_WaDataCallOptional findWaDataCallOptional(int wcseq);
	
	
}
