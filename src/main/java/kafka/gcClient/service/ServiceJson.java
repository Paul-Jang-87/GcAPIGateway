package kafka.gcClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.gcClient.interfaceCollection.InterfaceJson;

public class ServiceJson implements InterfaceJson {

	@Override
	public String ExtractVal(String stringMsg) { //campMa 테이블의 3가지 속성(coid,cpid,cpna)에 넣기 위한 가공 작업
		//(현재는 아무거나 임의로 뽑아봄)-매개변수로 들어온 JsonString data 'stringMsg'에서 'id','name','dialingMode'값 그냥 뽑아봄.

		String jsonResponse = stringMsg; 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);

			result = jsonNode.path("entities").path(0).path("id").asText();

			result = result + "|" + jsonNode.path("entities").path(0).path("queue").path("name").asText();

			result = result + "|" + jsonNode.path("entities").path(0).path("dialingMode").asText();
			
			
		} catch (JsonMappingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//리턴 데이터 형식 예) adhoahfd|oadiifaj|ohdhfa 
		return result;
	}
	
	@Override
	public String ExtractCpidfromThird(String stringMsg) {//나중에 변경해야함.
		
		String jsonResponse = stringMsg; 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String ExtractCpid(String stringMsg) {
		
		String jsonResponse = stringMsg; 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("cpid").asText();
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
