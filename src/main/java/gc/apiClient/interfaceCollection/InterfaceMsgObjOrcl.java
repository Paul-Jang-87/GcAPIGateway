package gc.apiClient.interfaceCollection;

public interface InterfaceMsgObjOrcl {

	<T> String dataCallMsg(T t, String crudtype) throws Exception;
	<T> String waDataCallOptionalMsg(T t, String crudtype)throws Exception;
	<T> String dataCallCustomerMsg(T t, String crudtype)throws Exception;
	<T> String dataCallService(T t, String crudtype)throws Exception;
	<T> String mstrSvcCdMsg(T t, String crudtype)throws Exception;
	<T> String waDataCallMsg(T t, String crudtype)throws Exception;
	<T> String waDataCallTraceMsg(T t, String crudtype)throws Exception;
	<T> String waMTraceCdMsg(T t, String crudtype)throws Exception;
	

}
