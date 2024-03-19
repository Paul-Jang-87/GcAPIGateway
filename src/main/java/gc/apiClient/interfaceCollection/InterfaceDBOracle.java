package gc.apiClient.interfaceCollection;

import java.util.List;

import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;

public interface InterfaceDBOracle {

	// insert
//	Entity_WaDataCallOptional InsertWaDataCallOptional(Entity_WaDataCallOptional entityWaDataCallOptional);

	// select
	Entity_WaDataCallOptional findWaDataCallOptional(int wcseq);

	Entity_DataCall findDataCallByCpid(Entity_DataCall enDataCall);

	int getRecordCount(String topic_id);

	List<Entity_WaDataCallOptional> getAllWaDataCallOptional();
	List<Entity_DataCall> getAllDataCall();
	
	<T> List<T> getAll(Class<T> clazz);

}
