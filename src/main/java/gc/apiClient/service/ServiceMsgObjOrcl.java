package gc.apiClient.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_DataCallCustomer;
import gc.apiClient.entity.oracleH.Entity_DataCallService;
import gc.apiClient.entity.oracleH.Entity_MasterServiceCode;
import gc.apiClient.entity.oracleH.Entity_WaDataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;
import gc.apiClient.entity.oracleH.Entity_WaDataCallTrace;
import gc.apiClient.entity.oracleH.Entity_WaMTracecode;
import gc.apiClient.entity.oracleM.Entity_MDataCall;
import gc.apiClient.entity.oracleM.Entity_MDataCallCustomer;
import gc.apiClient.entity.oracleM.Entity_MDataCallService;
import gc.apiClient.entity.oracleM.Entity_MMasterServiceCode;
import gc.apiClient.entity.oracleM.Entity_MWaDataCall;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallTrace;
import gc.apiClient.entity.oracleM.Entity_MWaMTracecode;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceMsgObjOrcl implements InterfaceMsgObjOrcl  {

	@Override
	public <T> String DataCallMsg(T t, String crudtype){

		log.info(" ");
		log.info("====== ClassName : ServiceMsgObjOrcl & Method : DataCallMsg ======");
		
		JSONObject obj = new JSONObject();
		try {
			

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
			String topcDataIsueDtm = now.format(formatter);

			obj.put("topcDataIsueDtm", topcDataIsueDtm);
			obj.put("dataChgCd", crudtype);

			// Check the type of t and cast it accordingly
			if (t instanceof Entity_DataCall) {
				Entity_DataCall en = (Entity_DataCall) t;
				obj.put("entTime", en.getNew_entered_time());
				obj.put("entDate", en.getNew_entered_date());
				obj.put("callSeq", en.getNew_call_seq());
				obj.put("icId", en.getNew_icid());
				obj.put("siteCd", en.getNew_site_code());
				
				log.info("Home obj toString : {}",obj.toString());
				
			} else if (t instanceof Entity_MDataCall) {
				Entity_MDataCall en = (Entity_MDataCall) t;
				obj.put("entTime", en.getNew_entered_time());
				obj.put("entDate", en.getNew_entered_date());
				obj.put("callSeq", en.getNew_call_seq());
				obj.put("icId", en.getNew_icid());
				obj.put("siteCd", en.getNew_site_code());
				log.info("Mobile obj toString : {}",obj.toString());
			}

			log.info("====== End DataCallMsg ======");
			
		} catch (Exception e) {
			
			log.error(e.getMessage());
			e.printStackTrace();	
		}
		return obj.toString();
		
		
	}

	
	@Override
	public <T> String DataCallCustomerMsg(T t, String crudtype) {

		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		// Check the type of t and cast it accordingly
		if (t instanceof Entity_DataCallCustomer) {
			Entity_DataCallCustomer en = (Entity_DataCallCustomer) t;
			obj.put("custDt01", en.getNew_customer_data01());
			obj.put("custDt02", en.getNew_customer_data02());
			obj.put("custDt03", en.getNew_customer_data03());
			obj.put("cSeq", en.getNew_cseq());
			obj.put("entDate", en.getNew_entered_date());
			obj.put("siteCd", en.getNew_site_code());
			obj.put("icId", en.getNew_icid());
			obj.put("callSeq", en.getNew_call_seq());
		} else if (t instanceof Entity_MDataCallCustomer) {
			Entity_MDataCallCustomer en = (Entity_MDataCallCustomer) t;
			obj.put("custDt01", en.getNew_customer_data01());
			obj.put("custDt02", en.getNew_customer_data02());
			obj.put("custDt03", en.getNew_customer_data03());
			obj.put("cSeq", en.getNew_cseq());
			obj.put("entDate", en.getNew_entered_date());
			obj.put("siteCd", en.getNew_site_code());
			obj.put("icId", en.getNew_icid());
			obj.put("callSeq", en.getNew_call_seq());
		}

		
		return obj.toString();
	}
	
	
	@Override
	public <T> String DataCallService(T t, String crudtype) {
		
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		// Check the type of t and cast it accordingly
		if (t instanceof Entity_DataCallService) {
			Entity_DataCallService en = (Entity_DataCallService) t;
			obj.put("entDate", en.getNew_entered_date());
			obj.put("siteCd", en.getNew_site_code());
			obj.put("icId", en.getNew_icid());
			obj.put("callSeq", en.getNew_call_seq());
			obj.put("svcCd01", en.getNEW_service_code1());
			obj.put("svcCd02", en.getNew_service_code2());
			obj.put("svcCd03", en.getNew_service_code3());
			obj.put("svcCd04", en.getNew_service_code4());
			obj.put("svcCd05", en.getNew_service_code5());
			obj.put("svcCd06", en.getNew_service_code6());
			obj.put("svcCd07", en.getNew_service_code7());
			obj.put("svcCd08", en.getNew_service_code8());
			obj.put("svcCd09", en.getNew_service_code9());
			obj.put("svcCd10", en.getNew_service_code10());
			obj.put("svcCd11", en.getNew_service_code11());
			obj.put("svcCd12", en.getNew_service_code12());
			obj.put("svcCd13", en.getNew_service_code13());
			obj.put("svcCd14", en.getNew_service_code14());
			obj.put("svcCd15", en.getNew_service_code15());
			obj.put("svcCd16", en.getNew_service_code16());
			obj.put("svcCd17", en.getNew_service_code17());
			obj.put("svcCd18", en.getNew_service_code18());
			obj.put("svcCd19", en.getNew_service_code19());
			obj.put("svcCd20", en.getNew_service_code20());
			obj.put("svcCd21", en.getNew_service_code21());
			obj.put("svcCd22", en.getNew_service_code22());
			obj.put("svcCd23", en.getNew_service_code23());
			obj.put("svcCd23", en.getNew_service_code23());
			obj.put("svcCd24", en.getNew_service_code24());
			obj.put("svcCd25", en.getNew_service_code25());
			obj.put("svcCd26", en.getNew_service_code26());
			obj.put("svcCd27", en.getNew_service_code27());
			obj.put("svcCd28", en.getNew_service_code28());
			obj.put("svcCd29", en.getNew_service_code29());
			obj.put("svcCd30", en.getNew_service_code30());
			obj.put("svcCd31", en.getNew_service_code31());
			obj.put("svcCd32", en.getNew_service_code32());
			obj.put("svcCd33", en.getNew_service_code33());
			obj.put("svcCd33", en.getNew_service_code33());
			obj.put("svcCd34", en.getNew_service_code34());
			obj.put("svcCd35", en.getNew_service_code35());
			obj.put("svcCd36", en.getNew_service_code36());
			obj.put("svcCd37", en.getNew_service_code37());
			obj.put("svcCd38", en.getNew_service_code38());
			obj.put("svcCd39", en.getNew_service_code39());
			obj.put("svcCd40", en.getNew_service_code40());
		} else if (t instanceof Entity_MDataCallService) {
			Entity_MDataCallService en = (Entity_MDataCallService) t;
			obj.put("entDate", en.getNew_entered_date());
			obj.put("siteCd", en.getNew_site_code());
			obj.put("icId", en.getNew_icid());
			obj.put("callSeq", en.getNew_call_seq());
			obj.put("svcCd01", en.getNEW_service_code1());
			obj.put("svcCd02", en.getNew_service_code2());
			obj.put("svcCd03", en.getNew_service_code3());
			obj.put("svcCd04", en.getNew_service_code4());
			obj.put("svcCd05", en.getNew_service_code5());
			obj.put("svcCd06", en.getNew_service_code6());
			obj.put("svcCd07", en.getNew_service_code7());
			obj.put("svcCd08", en.getNew_service_code8());
			obj.put("svcCd09", en.getNew_service_code9());
			obj.put("svcCd10", en.getNew_service_code10());
			obj.put("svcCd11", en.getNew_service_code11());
			obj.put("svcCd12", en.getNew_service_code12());
			obj.put("svcCd13", en.getNew_service_code13());
			obj.put("svcCd14", en.getNew_service_code14());
			obj.put("svcCd15", en.getNew_service_code15());
			obj.put("svcCd16", en.getNew_service_code16());
			obj.put("svcCd17", en.getNew_service_code17());
			obj.put("svcCd18", en.getNew_service_code18());
			obj.put("svcCd19", en.getNew_service_code19());
			obj.put("svcCd20", en.getNew_service_code20());
			obj.put("svcCd21", en.getNew_service_code21());
			obj.put("svcCd22", en.getNew_service_code22());
			obj.put("svcCd23", en.getNew_service_code23());
			obj.put("svcCd23", en.getNew_service_code23());
			obj.put("svcCd24", en.getNew_service_code24());
			obj.put("svcCd25", en.getNew_service_code25());
			obj.put("svcCd26", en.getNew_service_code26());
			obj.put("svcCd27", en.getNew_service_code27());
			obj.put("svcCd28", en.getNew_service_code28());
			obj.put("svcCd29", en.getNew_service_code29());
			obj.put("svcCd30", en.getNew_service_code30());
			obj.put("svcCd31", en.getNew_service_code31());
			obj.put("svcCd32", en.getNew_service_code32());
			obj.put("svcCd33", en.getNew_service_code33());
			obj.put("svcCd33", en.getNew_service_code33());
			obj.put("svcCd34", en.getNew_service_code34());
			obj.put("svcCd35", en.getNew_service_code35());
			obj.put("svcCd36", en.getNew_service_code36());
			obj.put("svcCd37", en.getNew_service_code37());
			obj.put("svcCd38", en.getNew_service_code38());
			obj.put("svcCd39", en.getNew_service_code39());
			obj.put("svcCd40", en.getNew_service_code40());
		}

		
		return obj.toString();
	}
	
	
	@Override
	public <T> String MstrSvcCdMsg(T t, String crudtype) {
		
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		// Check the type of t and cast it accordingly
		if (t instanceof Entity_MasterServiceCode) {
			Entity_MasterServiceCode en = (Entity_MasterServiceCode) t;
			obj.put("siteCd", en.getNew_site_code());
			obj.put("svCd", en.getNew_site_code());
			obj.put("svCdNm", en.getNew_service_name());
			obj.put("svCdType", en.getNew_service_code_type());
		} else if (t instanceof Entity_MMasterServiceCode) {
			Entity_MMasterServiceCode en = (Entity_MMasterServiceCode) t;
			obj.put("siteCd", en.getNew_site_code());
			obj.put("svCd", en.getNew_site_code());
			obj.put("svCdNm", en.getNew_service_name());
			obj.put("svCdType", en.getNew_service_code_type());
		}

		return obj.toString();
	}
	
	
	@Override
	public <T> String WaDataCallMsg(T t, String crudtype) {
		
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		// Check the type of t and cast it accordingly
		if (t instanceof Entity_WaDataCall) {
			Entity_WaDataCall en = (Entity_WaDataCall) t;
			obj.put("wcSeq", en.getNew_wcseq());
			obj.put("icId", en.getNew_icid());
			obj.put("entDate", en.getNew_entered_date());
		} else if (t instanceof Entity_MWaDataCall) {
			Entity_MWaDataCall en = (Entity_MWaDataCall) t;
			obj.put("wcSeq", en.getNew_wcseq());
			obj.put("icId", en.getNew_icid());
			obj.put("entDate", en.getNew_entered_date());
			obj.put("ctn", en.getNew_ctn());
			
		}

		return obj.toString();
	}
	

	@Override
	public <T> String WaDataCallOptionalMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
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

		return obj.toString();
	}


	@Override
	public <T> String WaDataCallTraceMsg(T t, String crudtype) {
		
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		// Check the type of t and cast it accordingly
		if (t instanceof Entity_WaDataCallTrace) {
			Entity_WaDataCallTrace en = (Entity_WaDataCallTrace) t;
			obj.put("wcSeq", en.getNew_wcseq());
			obj.put("tcSeq", en.getNew_tc_seq());
			obj.put("trCd", en.getNew_tracecode());
		} else if (t instanceof Entity_MWaDataCallTrace) {
			Entity_MWaDataCallTrace en = (Entity_MWaDataCallTrace) t;
			obj.put("wcSeq", en.getNew_wcseq());
			obj.put("tcSeq", en.getNew_tc_seq());
			obj.put("trCd", en.getNew_tracecode());
		}

		return obj.toString();
	}


	@Override
	public <T> String WaMTraceCdMsg(T t, String crudtype) {
		
		JSONObject obj = new JSONObject();

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		obj.put("topcDataIsueDtm", topcDataIsueDtm);
		obj.put("dataChgCd", crudtype);
		
		if (t instanceof Entity_WaMTracecode) {
			Entity_WaMTracecode en = (Entity_WaMTracecode) t;
			obj.put("siteCd", en.getNew_site_code());
			obj.put("trCd", en.getNew_tracecode());
			obj.put("trCdNm", en.getNew_tracecode_name());
			obj.put("trCdType", en.getNew_tracecode_type());
			
		} else if (t instanceof Entity_MWaMTracecode) {
			Entity_MWaMTracecode en = (Entity_MWaMTracecode) t;
			obj.put("siteCd", en.getNew_site_code());
			obj.put("trCd", en.getNew_tracecode());
			obj.put("trCdNm", en.getNew_tracecode_name());
			obj.put("trCdType", en.getNew_tracecode_type());
		}

		return obj.toString();
	}


	


	


	

}
