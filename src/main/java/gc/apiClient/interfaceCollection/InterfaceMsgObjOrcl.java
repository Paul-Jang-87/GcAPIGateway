package gc.apiClient.interfaceCollection;


import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;


public interface InterfaceMsgObjOrcl {
	
	 String msg(Entity_WaDataCallOptional en);
	 String msg(Entity_DataCall en);

}
