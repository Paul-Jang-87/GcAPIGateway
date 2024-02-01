package kafka.gcClient.service;

import org.springframework.stereotype.Service;

import kafka.gcClient.interfaceCollection.InterfaceWebClient;
import kafka.gcClient.webclient.WebClientApp;

@Service
public class CrmSv01 implements InterfaceWebClient {
	
	private final ServicePostgre servicePostgre;

    public CrmSv01(ServicePostgre servicePostgre) {
        this.servicePostgre = servicePostgre;
    }

	@Override
	public String GetApiRequet(String endpoint) {
		
		String result = "";
		
		WebClientApp webClientExample = new WebClientApp(endpoint,"GET",servicePostgre);
		result = webClientExample.makeApiRequest();
		
		System.out.println(result);
		
		return result;
	}

}
