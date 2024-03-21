package gc.apiClient.interfaceCollection;


public interface InterfaceMsgObjOrcl {

	<T> String DataCallMsg(T t, String crudtype);
	<T> String WaDataCallOptionalMsg(T t, String crudtype);
	<T> String DataCallCustomerMsg(T t, String crudtype);
	<T> String DataCallService(T t, String crudtype);
	<T> String MstrSvcCdMsg(T t, String crudtype);
	<T> String WaDataCallMsg(T t, String crudtype);
	<T> String WaDataCallTraceMsg(T t, String crudtype);
	<T> String WaMTraceCdMsg(T t, String crudtype);

}
