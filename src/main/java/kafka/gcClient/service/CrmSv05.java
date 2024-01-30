package kafka.gcClient.service;

import org.springframework.stereotype.Service;

import kafka.gcClient.interfaceCollection.InterfaceWebClient;
import kafka.gcClient.webclient.WebClientApp;

@Service
public class CrmSv05 extends ServiceJson implements InterfaceWebClient {

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


}
