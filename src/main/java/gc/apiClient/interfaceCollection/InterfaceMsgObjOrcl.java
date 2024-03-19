package gc.apiClient.interfaceCollection;

import gc.apiClient.entity.oracle.Entity_DataCall;

public interface InterfaceMsgObjOrcl {

	<T>String DataCallMsg(T t, String crudtype);
	<T>String WaDataCallOptionalMsg(T t, String crudtype);


}
