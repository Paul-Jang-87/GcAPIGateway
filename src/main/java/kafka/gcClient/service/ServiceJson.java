package kafka.gcClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.gcClient.interfaceCollection.InterfaceJson;

public class ServiceJson implements InterfaceJson {

	@Override
	public String ExtractVal(String stringMsg) {
		
		String jsonResponse = stringMsg; // replace with your actual JSON response

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null ;
        
		try {
			jsonNode = objectMapper.readTree(jsonResponse);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result = jsonNode
              .path("entities")
              .path(0)
              .path("id")
              .asText();

        System.out.println("ID Value: " + result);
        return result;
	}

}
