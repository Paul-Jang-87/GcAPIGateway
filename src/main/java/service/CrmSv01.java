package service;

import org.springframework.stereotype.Service;

import interfaceCollection.InterfaceWebClient;
import webclient.WebClientApp;

@Service
public class CrmSv01 implements InterfaceWebClient{

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint);
		result = webClientExample.makeApiRequest();
		
		System.out.println(result);
		
		return result;
	}

}
