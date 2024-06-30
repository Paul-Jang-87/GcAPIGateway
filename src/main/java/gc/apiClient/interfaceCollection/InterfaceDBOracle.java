package gc.apiClient.interfaceCollection;

import java.util.List;

public interface InterfaceDBOracle {


	//테이블에 있는 레코드의 개수를 리턴해주는 함수.
	int getRecordCount(String topic_id)throws Exception;

	// 모든 레코드를 가져옴.
	<T> List<T> getAll(Class<T> clazz)throws Exception;
	<T> void deleteAll(Class<T> clazz,int orderid)throws Exception;

}
