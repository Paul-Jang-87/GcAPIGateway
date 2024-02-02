package kafka.gcClient.service;

import org.springframework.stereotype.Service;

import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.interfaceCollection.InterfaceWebClient;
import kafka.gcClient.webclient.WebClientApp;

@Service
public class CrmSv01 implements InterfaceWebClient {
	
	private final InterfaceDB serviceDb;

	public CrmSv01(InterfaceDB serviceDb) {
		this.serviceDb = serviceDb;
	}

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET",serviceDb);
		result = webClientExample.makeApiRequest();
		
		System.out.println(result);
		
		return result;
	}

}
