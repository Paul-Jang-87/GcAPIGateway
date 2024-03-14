package gc.apiClient.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.interfaceCollection.InterfaceJsonOracle;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceJsonOracle implements InterfaceJsonOracle {

	@Override
	public String ExtractDataCall(String stringMsg) {
		
		String jsonResponse = stringMsg;
		log.info("msg : {}",jsonResponse); 

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		String result = "";

		try {
			//테이블별고 어떤 거 추출해서 리턴할지 정함(변동부분.)
			
			jsonNode = objectMapper.readTree(jsonResponse);
//			result = jsonNode.path("WCSEQ").asText();
//			log.info("wcseq : {}",result);

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("result : {}",result);
		return result;
	}
	
	

//	public <T> T returnKey(String topic, String msg, Class<T> returnType) {
//
//		T result = null;
//		switch (topic) {
//
//		case "a":
//
//			result = (T) ExtractDataCall(msg);
//			
//			break;
//
//		case "b":
//			
//			Entity_ContactLt entityContactLt = new Entity_ContactLt();
//			result = (T) entityContactLt.getId();
//			
//			break;
//		default:
//		}
//		
//		return result; 
//
//	}

}
