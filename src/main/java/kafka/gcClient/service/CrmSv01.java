package kafka.gcClient.service;

import org.springframework.stereotype.Service;

import kafka.gcClient.interfaceCollection.InterfaceWebClient;
import kafka.gcClient.webclient.WebClientApp;

@Service
public class CrmSv01 implements InterfaceWebClient{

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint);
		result = webClientExample.makeApiRequest();
		
		return result;
	}

}
