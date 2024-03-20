package gc.apiClient.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.aspectj.weaver.ast.Instanceof;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.configurationprocessor.json.JSONException;
import com.google.gson.Gson;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;
import gc.apiClient.entity.oracleM.Entity_MDataCall;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceMsgObjOrcl implements InterfaceMsgObjOrcl {

	@Override
	public <T> String DataCallMsg(T t, String crudtype) {
	    JSONObject obj = new JSONObject();

	    try {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
	        String topcDataIsueDtm = now.format(formatter);

	        obj.put("topcDataIsueDtm", topcDataIsueDtm);
	        obj.put("dataChgCd", crudtype);
	        obj.put("dataDelYn", crudtype.equals("delete") ? "Y" : "N");

	        // Check the type of t and cast it accordingly
	        if (t instanceof Entity_DataCall) {
	            Entity_DataCall en = (Entity_DataCall) t;
	            obj.put("entDate", en.getNew_entered_time());
	            obj.put("entTime", en.getNew_entered_date());
	            obj.put("callSeq", en.getNew_call_seq());
	            obj.put("icId", en.getNew_icid());
	            obj.put("siteCd", en.getNew_site_code());
	        } else if (t instanceof Entity_MDataCall) {
	            Entity_MDataCall en = (Entity_MDataCall) t;
	            obj.put("entDate", en.getNew_entered_time());
	            obj.put("entTime", en.getNew_entered_date());
	            obj.put("callSeq", en.getNew_call_seq());
	            obj.put("icId", en.getNew_icid());
	            obj.put("siteCd", en.getNew_site_code());
	        }

	    } catch (JSONException e) {
	        e.printStackTrace();
	    }

	    return obj.toString();
	}

	@Override
	public <T> String WaDataCallOptionalMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();

	    try {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
	        String topcDataIsueDtm = now.format(formatter);

	        obj.put("topcDataIsueDtm", topcDataIsueDtm);
	        obj.put("dataChgCd", crudtype);
	        obj.put("dataDelYn", crudtype.equals("delete") ? "Y" : "N");

	        // Check the type of t and cast it accordingly
	        if (t instanceof Entity_WaDataCallOptional) {
	        	Entity_WaDataCallOptional en = (Entity_WaDataCallOptional) t;
	            obj.put("wcSeq", en.getNew_wcseq());
	            obj.put("data02", en.getNew_data02());
	        } else if (t instanceof Entity_MWaDataCallOptional) {
	        	Entity_MWaDataCallOptional en = (Entity_MWaDataCallOptional) t;
	            obj.put("wcSeq", en.getNew_wcseq());
	            obj.put("data02", en.getNew_data02());
	        }

	    } catch (JSONException e) {
	        e.printStackTrace();
	    }

	    return obj.toString();
	}



}
