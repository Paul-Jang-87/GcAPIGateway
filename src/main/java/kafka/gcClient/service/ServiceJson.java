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
	public String ExtractCrm56(String stringMsg) {
		
		String jsonResponse = stringMsg; 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("id").asText();
			result += "|"+jsonNode.path("detail").path("eventBody").path("contactList").path("id").asText();
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String ExtractValCrm12(String stringMsg) {
		
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
	public int ExtractDict(String stringMsg) {
		
		String jsonResponse = stringMsg; 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		int result = 100;

		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode.path("contactRate").path("attempts").asInt();
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		  return result;
	  }

	@Override
	public String ExtractDidtDirt(String stringMsg) {//stringMsg에서 didt,dirt추출헤서 리턴해주는 함수.
		
		System.out.println("ExtractDidtDirt 들어옴.");
		String result = "2023-11-29T11:15:31.705Z|0227"; //didt,dirt
		
		return result;
	}
}
