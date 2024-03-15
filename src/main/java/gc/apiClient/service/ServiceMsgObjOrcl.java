package gc.apiClient.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import com.google.gson.Gson;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceMsgObjOrcl implements InterfaceMsgObjOrcl{

	@Override
	public String msg(Entity_WaDataCallOptional en) {
		
		int a = en.getWcseq();
		String b = en.getData02();
		
		// Create a JSON object
        JSONObject as = new JSONObject();

        try {
			as.put("wcseq", a);
			as.put("data02", b);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        
        String jsonString = as.toString();
        
		
		return jsonString;
	}
	
	
	@Override
	public String msg(Entity_DataCall en) {
		
//		int a = en.getWcseq();
//		String b = en.getData02();
		
		// Create a JSON object
        JSONObject as = new JSONObject();

//        try {
//			as.put("wcseq", a);
//			as.put("data02", b);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

        
        String jsonString = as.toString();
        
		
		return jsonString;
	}

}
