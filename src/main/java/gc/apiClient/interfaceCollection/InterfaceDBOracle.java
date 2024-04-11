package gc.apiClient.interfaceCollection;

import java.util.List;

import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;

public interface InterfaceDBOracle {

	// insert
//	Entity_WaDataCallOptional InsertWaDataCallOptional(Entity_WaDataCallOptional entityWaDataCallOptional);

	// select
	Entity_WaDataCallOptional findWaDataCallOptional(int wcseq)throws Exception;

	Entity_DataCall findDataCallByCpid(int orderid)throws Exception;

	int getRecordCount(String topic_id)throws Exception;

//	List<Entity_WaDataCallOptional> getAllWaDataCallOptional();
	
	<T> List<T> getAll(Class<T> clazz)throws Exception;
	<T> void deleteAll(Class<T> clazz,int orderid)throws Exception;

}
