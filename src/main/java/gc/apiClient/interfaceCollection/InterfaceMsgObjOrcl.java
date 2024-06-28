package gc.apiClient.interfaceCollection;

public interface InterfaceMsgObjOrcl {

	<T> String DataCallMsg(T t, String crudtype) throws Exception;
	<T> String WaDataCallOptionalMsg(T t, String crudtype)throws Exception;
	<T> String DataCallCustomerMsg(T t, String crudtype)throws Exception;
	<T> String DataCallService(T t, String crudtype)throws Exception;
	<T> String MstrSvcCdMsg(T t, String crudtype)throws Exception;
	<T> String WaDataCallMsg(T t, String crudtype)throws Exception;
	<T> String WaDataCallTraceMsg(T t, String crudtype)throws Exception;
	<T> String WaMTraceCdMsg(T t, String crudtype)throws Exception;
	

}
