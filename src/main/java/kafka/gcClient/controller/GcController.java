package kafka.gcClient.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kafka.gcClient.encryptdecrypt.AESDecryption;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.service.CrmSv01;
import kafka.gcClient.service.CrmSv05;
import kafka.gcClient.service.ServiceJson;
import kafka.gcClient.service.ServicePostgre;
import reactor.core.publisher.Mono;

@RestController
public class GcController extends ServiceJson {
	
	private final InterfaceDB serviceDb;
	private final ServicePostgre servicePostgre;

	public GcController(InterfaceDB serviceDb, ServicePostgre servicePostgre) {
		this.serviceDb = serviceDb;
		this.servicePostgre = servicePostgre;
	}

	// APIM

	// GC API

	
	@GetMapping("/gcapi/get/{topic}")
	public String getApiData(@PathVariable("topic") String tranId) {
		
		String result = "";
		String topic_name = tranId.toUpperCase();
		
		switch (topic_name) {
		case "IF-CRM-001":
			CrmSv01 api1 = new CrmSv01(servicePostgre);
			api1.GetApiRequet("campaignId");
			break;
			
		case "IF-CRM-002":
			break;
			
		case "IF-CRM-005":
			CrmSv05 api5 = new CrmSv05();
			api5.GetApiRequet(topic_name);
			break;
			
		case "IF-CRM-006":
			break;
		default:
			result = "ERROR";
			break;
		}
		return result;
	}

	@PostMapping("/gcapi/post/{topic}")
	public Mono<Void> receiveMessage(@PathVariable("topic") String tranId, @RequestBody String msg) {

		String result = "";
		String topic_id = tranId.toUpperCase(); 

	    switch (topic_id) {
	    
	        case "IF-CRM-003": //시나리오 : 어떤 api호출 후 그 결과 값에서 특정 값을 뽑아 제가공하여 entity 메시지를 만들고 db에 인서트
	           CrmSv01 crmapi1 = new CrmSv01(servicePostgre);
	           result = crmapi1.GetApiRequet("campaignId");//api호출 후 결과 값을 받음. 
	           
	           System.out.println(result);
	        	
//	            Entity_CampMa entity = serviceDb.createCampMaMsg(result);//CMAPMA테이블에 인터트 하기위해 json data 가공작업.
//	            return serviceDb.InsertCampMa(entity)//CMAPMA테이블에 매핑할 수 있는 entity 객체를 테이블에 인서트 
//	                    .flatMap(savedEntity -> {
//	                        return Mono.empty(); 
//	                    });
	            
	        case "IF-CRM-002"://시나리오 : 들어온 cpid로 MapCoid테이블 조회, cpid와 매칭되는 coid가져와 CampMa테이블에 insert. 
	        	
	        	 String cpid = ExtractCpid(msg);
	        	 System.out.println(cpid);

	        	 return serviceDb.findMapCoidByCpid(cpid)
	                        .flatMap(foundEntity -> {
	                            String coid = foundEntity.getCoid();
	                            System.out.println("Found COID: " + coid);
	                            String newdata = coid+"|"+cpid+"|"+"cpna_4";
	                            
	                            Entity_CampMa entityMa = serviceDb.createCampMaMsg(newdata);
	            	            return serviceDb.InsertCampMa(entityMa) 
	            	                    .flatMap(savedEntity -> {
	            	                        return Mono.empty(); 
	            	                    });
	                        });
	        	
	        default:
	            break;
	    }
	    
	    return  Mono.empty();
	}
	
	
	
	@GetMapping("/api/pwd")
	public Mono<String>  getApiData()  {
		
		return serviceDb.findAppConfigByid((long) 1)
                .flatMap(foundEntity -> {
                    String id = foundEntity.getGcClientId();
                    String pwd = foundEntity.getGcClientSecret();
                    
                    String decryptedId;
					try { 
						decryptedId = AESDecryption.decrypt(id);
						String decryptedPassword = AESDecryption.decrypt(pwd);
						
						System.out.println("Decrypted ID: " + decryptedId);
						System.out.println("Decrypted Password: " + decryptedPassword);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    return Mono.empty();
                    
                });
	}
	
}
