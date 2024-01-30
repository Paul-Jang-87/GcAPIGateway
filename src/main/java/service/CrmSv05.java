package service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import interfaceCollection.InterfaceJson;
import interfaceCollection.InterfaceWebClient;
import webclient.WebClientApp;

@Service
public class CrmSv05 implements InterfaceWebClient, InterfaceJson {

	@Override
	public String GetApiRequet(String endpoint) {

		String result = "";

		WebClientApp api = new WebClientApp("IF-CRM-001");
		result = api.makeApiRequest();
		String campaignId = ExtractVal(result);
		System.out.println("추출 : "+ campaignId);
		
		WebClientApp webClientExample = new WebClientApp(endpoint);
		result = webClientExample.makeApiRequest(campaignId);
		System.out.println("결과 : "+ result);

		return result;
	}

	@Override
	public String ExtractVal(String stringMsg) {

		String jsonResponse = stringMsg;
		String result = "";

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(jsonResponse);
			result = jsonNode
					.path("entities")
					.path(0).path("id")
					.asText();
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
