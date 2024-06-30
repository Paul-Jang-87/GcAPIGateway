package gc.apiClient.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ServiceMsgObjOrcl implements InterfaceMsgObjOrcl {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");

	private String getCurrentTimestamp() {
		return formatter.format(new Date());
	}

	private void populateCommonFields(JSONObject obj, String crudtype) {
		obj.put("topcDataIsueDtm", getCurrentTimestamp());
		obj.put("dataChgCd", crudtype != null ? crudtype : "");
	}

	@Override
	public <T> String dataCallMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_DataCall en) {
				populateDataCallFields(obj, en);
			} else if (t instanceof Entity_MDataCall en) {
				populateMDataCallFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}

	private void populateDataCallFields(JSONObject obj, Entity_DataCall en) {
		obj.put("entTime", en.getNew_entered_time() != null ? en.getNew_entered_time() : "");
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
	}

	private void populateMDataCallFields(JSONObject obj, Entity_MDataCall en) {
		obj.put("entTime", en.getNew_entered_time() != null ? en.getNew_entered_time() : "");
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
	}

	@Override
	public <T> String dataCallCustomerMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_DataCallCustomer en) {
				populateDataCallCustomerFields(obj, en);
			} else if (t instanceof Entity_MDataCallCustomer en) {
				populateMDataCallCustomerFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}

	private void populateDataCallCustomerFields(JSONObject obj, Entity_DataCallCustomer en) {
		obj.put("custDt01", en.getNew_customer_data01() != null ? en.getNew_customer_data01() : "");
		obj.put("custDt02", en.getNew_customer_data02() != null ? en.getNew_customer_data02() : "");
		obj.put("custDt03", en.getNew_customer_data03() != null ? en.getNew_customer_data03() : "");
		obj.put("cSeq", en.getNew_cseq() != null ? en.getNew_cseq() : 0);
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
	}

	private void populateMDataCallCustomerFields(JSONObject obj, Entity_MDataCallCustomer en) {
		obj.put("custDt01", en.getNew_customer_data01() != null ? en.getNew_customer_data01() : "");
		obj.put("custDt02", en.getNew_customer_data02() != null ? en.getNew_customer_data02() : "");
		obj.put("custDt03", en.getNew_customer_data03() != null ? en.getNew_customer_data03() : "");
		obj.put("cSeq", en.getNew_cseq() != null ? en.getNew_cseq() : 0);
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
	}

	@Override
	public <T> String dataCallService(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_DataCallService en) {
				populateDataCallServiceFields(obj, en);
			} else if (t instanceof Entity_MDataCallService en) {
				populateMDataCallServiceFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}

	private void populateDataCallServiceFields(JSONObject obj, Entity_DataCallService en) {
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
		obj.put("svcCd01", en.getNEW_service_code1() != null ? en.getNEW_service_code1() : "");
		obj.put("svcCd02", en.getNew_service_code2() != null ? en.getNew_service_code2() : "");
		obj.put("svcCd03", en.getNew_service_code3() != null ? en.getNew_service_code3() : "");
		obj.put("svcCd04", en.getNew_service_code4() != null ? en.getNew_service_code4() : "");
		obj.put("svcCd05", en.getNew_service_code5() != null ? en.getNew_service_code5() : "");
		obj.put("svcCd06", en.getNew_service_code6() != null ? en.getNew_service_code6() : "");
		obj.put("svcCd07", en.getNew_service_code7() != null ? en.getNew_service_code7() : "");
		obj.put("svcCd08", en.getNew_service_code8() != null ? en.getNew_service_code8() : "");
		obj.put("svcCd09", en.getNew_service_code9() != null ? en.getNew_service_code9() : "");
		obj.put("svcCd10", en.getNew_service_code10() != null ? en.getNew_service_code10() : "");
		obj.put("svcCd11", en.getNew_service_code11() != null ? en.getNew_service_code11() : "");
		obj.put("svcCd12", en.getNew_service_code12() != null ? en.getNew_service_code12() : "");
		obj.put("svcCd13", en.getNew_service_code13() != null ? en.getNew_service_code13() : "");
		obj.put("svcCd14", en.getNew_service_code14() != null ? en.getNew_service_code14() : "");
		obj.put("svcCd15", en.getNew_service_code15() != null ? en.getNew_service_code15() : "");
		obj.put("svcCd16", en.getNew_service_code16() != null ? en.getNew_service_code16() : "");
		obj.put("svcCd17", en.getNew_service_code17() != null ? en.getNew_service_code17() : "");
		obj.put("svcCd18", en.getNew_service_code18() != null ? en.getNew_service_code18() : "");
		obj.put("svcCd19", en.getNew_service_code19() != null ? en.getNew_service_code19() : "");
		obj.put("svcCd20", en.getNew_service_code20() != null ? en.getNew_service_code20() : "");
		obj.put("svcCd21", en.getNew_service_code21() != null ? en.getNew_service_code21() : "");
		obj.put("svcCd22", en.getNew_service_code22() != null ? en.getNew_service_code22() : "");
		obj.put("svcCd23", en.getNew_service_code23() != null ? en.getNew_service_code23() : "");
		obj.put("svcCd24", en.getNew_service_code24() != null ? en.getNew_service_code24() : "");
		obj.put("svcCd25", en.getNew_service_code25() != null ? en.getNew_service_code25() : "");
		obj.put("svcCd26", en.getNew_service_code26() != null ? en.getNew_service_code26() : "");
		obj.put("svcCd27", en.getNew_service_code27() != null ? en.getNew_service_code27() : "");
		obj.put("svcCd28", en.getNew_service_code28() != null ? en.getNew_service_code28() : "");
		obj.put("svcCd29", en.getNew_service_code29() != null ? en.getNew_service_code29() : "");
		obj.put("svcCd30", en.getNew_service_code30() != null ? en.getNew_service_code30() : "");
		obj.put("svcCd31", en.getNew_service_code31() != null ? en.getNew_service_code31() : "");
		obj.put("svcCd32", en.getNew_service_code32() != null ? en.getNew_service_code32() : "");
		obj.put("svcCd33", en.getNew_service_code33() != null ? en.getNew_service_code33() : "");
		obj.put("svcCd34", en.getNew_service_code34() != null ? en.getNew_service_code34() : "");
		obj.put("svcCd35", en.getNew_service_code35() != null ? en.getNew_service_code35() : "");
		obj.put("svcCd36", en.getNew_service_code36() != null ? en.getNew_service_code36() : "");
		obj.put("svcCd37", en.getNew_service_code37() != null ? en.getNew_service_code37() : "");
		obj.put("svcCd38", en.getNew_service_code38() != null ? en.getNew_service_code38() : "");
		obj.put("svcCd39", en.getNew_service_code39() != null ? en.getNew_service_code39() : "");
		obj.put("svcCd40", en.getNew_service_code40() != null ? en.getNew_service_code40() : "");
	}

	private void populateMDataCallServiceFields(JSONObject obj, Entity_MDataCallService en) {
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("callSeq", en.getNew_call_seq() != null ? en.getNew_call_seq() : 0);
		obj.put("svcCd01", en.getNEW_service_code1() != null ? en.getNEW_service_code1() : "");
		obj.put("svcCd02", en.getNew_service_code2() != null ? en.getNew_service_code2() : "");
		obj.put("svcCd03", en.getNew_service_code3() != null ? en.getNew_service_code3() : "");
		obj.put("svcCd04", en.getNew_service_code4() != null ? en.getNew_service_code4() : "");
		obj.put("svcCd05", en.getNew_service_code5() != null ? en.getNew_service_code5() : "");
		obj.put("svcCd06", en.getNew_service_code6() != null ? en.getNew_service_code6() : "");
		obj.put("svcCd07", en.getNew_service_code7() != null ? en.getNew_service_code7() : "");
		obj.put("svcCd08", en.getNew_service_code8() != null ? en.getNew_service_code8() : "");
		obj.put("svcCd09", en.getNew_service_code9() != null ? en.getNew_service_code9() : "");
		obj.put("svcCd10", en.getNew_service_code10() != null ? en.getNew_service_code10() : "");
		obj.put("svcCd11", en.getNew_service_code11() != null ? en.getNew_service_code11() : "");
		obj.put("svcCd12", en.getNew_service_code12() != null ? en.getNew_service_code12() : "");
		obj.put("svcCd13", en.getNew_service_code13() != null ? en.getNew_service_code13() : "");
		obj.put("svcCd14", en.getNew_service_code14() != null ? en.getNew_service_code14() : "");
		obj.put("svcCd15", en.getNew_service_code15() != null ? en.getNew_service_code15() : "");
		obj.put("svcCd16", en.getNew_service_code16() != null ? en.getNew_service_code16() : "");
		obj.put("svcCd17", en.getNew_service_code17() != null ? en.getNew_service_code17() : "");
		obj.put("svcCd18", en.getNew_service_code18() != null ? en.getNew_service_code18() : "");
		obj.put("svcCd19", en.getNew_service_code19() != null ? en.getNew_service_code19() : "");
		obj.put("svcCd20", en.getNew_service_code20() != null ? en.getNew_service_code20() : "");
		obj.put("svcCd21", en.getNew_service_code21() != null ? en.getNew_service_code21() : "");
		obj.put("svcCd22", en.getNew_service_code22() != null ? en.getNew_service_code22() : "");
		obj.put("svcCd23", en.getNew_service_code23() != null ? en.getNew_service_code23() : "");
		obj.put("svcCd24", en.getNew_service_code24() != null ? en.getNew_service_code24() : "");
		obj.put("svcCd25", en.getNew_service_code25() != null ? en.getNew_service_code25() : "");
		obj.put("svcCd26", en.getNew_service_code26() != null ? en.getNew_service_code26() : "");
		obj.put("svcCd27", en.getNew_service_code27() != null ? en.getNew_service_code27() : "");
		obj.put("svcCd28", en.getNew_service_code28() != null ? en.getNew_service_code28() : "");
		obj.put("svcCd29", en.getNew_service_code29() != null ? en.getNew_service_code29() : "");
		obj.put("svcCd30", en.getNew_service_code30() != null ? en.getNew_service_code30() : "");
		obj.put("svcCd31", en.getNew_service_code31() != null ? en.getNew_service_code31() : "");
		obj.put("svcCd32", en.getNew_service_code32() != null ? en.getNew_service_code32() : "");
		obj.put("svcCd33", en.getNew_service_code33() != null ? en.getNew_service_code33() : "");
		obj.put("svcCd34", en.getNew_service_code34() != null ? en.getNew_service_code34() : "");
		obj.put("svcCd35", en.getNew_service_code35() != null ? en.getNew_service_code35() : "");
		obj.put("svcCd36", en.getNew_service_code36() != null ? en.getNew_service_code36() : "");
		obj.put("svcCd37", en.getNew_service_code37() != null ? en.getNew_service_code37() : "");
		obj.put("svcCd38", en.getNew_service_code38() != null ? en.getNew_service_code38() : "");
		obj.put("svcCd39", en.getNew_service_code39() != null ? en.getNew_service_code39() : "");
		obj.put("svcCd40", en.getNew_service_code40() != null ? en.getNew_service_code40() : "");
	}

	@Override
	public <T> String mstrSvcCdMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_MasterServiceCode en) {
				populateMstrSvcCdMsgFields(obj, en);
			} else if (t instanceof Entity_MMasterServiceCode en) {
				populateMMstrSvcCdMsgFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}

	private void populateMstrSvcCdMsgFields(JSONObject obj, Entity_MasterServiceCode en) {
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("svCd", en.getNew_service_code() != null ? en.getNew_service_code() : "");
		obj.put("svCdNm", en.getNew_service_name() != null ? en.getNew_service_name() : "");
		obj.put("svCdType", en.getNew_service_code_type() != null ? en.getNew_service_code_type() : 0);
	}

	private void populateMMstrSvcCdMsgFields(JSONObject obj, Entity_MMasterServiceCode en) {
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("svCd", en.getNew_service_code() != null ? en.getNew_service_code() : "");
		obj.put("svCdNm", en.getNew_service_name() != null ? en.getNew_service_name() : "");
		obj.put("svCdType", en.getNew_service_code_type() != null ? en.getNew_service_code_type() : 0);
	}
	
	
	
	@Override
	public <T> String waDataCallMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_WaDataCall en) {
				populateWaDataCallFields(obj, en);
			} else if (t instanceof Entity_MWaDataCall en) {
				populateMWaDataCallFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}
	
	private void populateWaDataCallFields(JSONObject obj, Entity_WaDataCall en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
	}

	private void populateMWaDataCallFields(JSONObject obj, Entity_MWaDataCall en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("icId", en.getNew_icid() != null ? en.getNew_icid() : "");
		obj.put("entDate", en.getNew_entered_date() != null ? en.getNew_entered_date() : "");
		obj.put("ctn", en.getNew_ctn() != null ? en.getNew_ctn() : 0);
	}
	
	
	
	@Override
	public <T> String waDataCallOptionalMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_WaDataCallOptional en) {
				populateWaDataCallOptionalFields(obj, en);
			} else if (t instanceof Entity_MWaDataCallOptional en) {
				populateMWaDataCallOptionalFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}
	
	private void populateWaDataCallOptionalFields(JSONObject obj, Entity_WaDataCallOptional en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("data02", en.getNew_data02() != null ? en.getNew_data02() : "");
	}

	private void populateMWaDataCallOptionalFields(JSONObject obj, Entity_MWaDataCallOptional en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("data02", en.getNew_data02() != null ? en.getNew_data02() : "");
	}
	
	
	
	@Override
	public <T> String waDataCallTraceMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_WaDataCallTrace en) {
				populateWaDataCallTraceFields(obj, en);
			} else if (t instanceof Entity_MWaDataCallTrace en) {
				populateMWaDataCallTraceFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}
	
	private void populateWaDataCallTraceFields(JSONObject obj, Entity_WaDataCallTrace en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("tcSeq", en.getNew_tc_seq() != null ? en.getNew_tc_seq() : 0);
		obj.put("trCd", en.getNew_tracecode() != null ? en.getNew_tracecode() : "");
	}

	private void populateMWaDataCallTraceFields(JSONObject obj, Entity_MWaDataCallTrace en) {
		obj.put("wcSeq", en.getNew_wcseq() != null ? en.getNew_wcseq() : 0);
		obj.put("tcSeq", en.getNew_tc_seq() != null ? en.getNew_tc_seq() : 0);
		obj.put("trCd", en.getNew_tracecode() != null ? en.getNew_tracecode() : "");
	}
	
	
	
	@Override
	public <T> String waMTraceCdMsg(T t, String crudtype) {
		JSONObject obj = new JSONObject();
		try {
			populateCommonFields(obj, crudtype);
			if (t instanceof Entity_WaMTracecode en) {
				populateWaMTraceCdFields(obj, en);
			} else if (t instanceof Entity_MWaMTracecode en) {
				populateMWaMTraceCdFields(obj, en);
			}
		} catch (Exception e) {
			logError(e);
		}
		return obj.toString();
	}
	
	private void populateWaMTraceCdFields(JSONObject obj, Entity_WaMTracecode en) {
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("trCd", en.getNew_tracecode() != null ? en.getNew_tracecode() : "");
		obj.put("trCdNm", en.getNew_tracecode_name() != null ? en.getNew_tracecode_name() : "");
		obj.put("trCdType", en.getNew_tracecode_type() != null ? en.getNew_tracecode_type() : 0);
	}

	private void populateMWaMTraceCdFields(JSONObject obj, Entity_MWaMTracecode en) {
		obj.put("siteCd", en.getNew_site_code() != null ? en.getNew_site_code() : 0);
		obj.put("trCd", en.getNew_tracecode() != null ? en.getNew_tracecode() : "");
		obj.put("trCdNm", en.getNew_tracecode_name() != null ? en.getNew_tracecode_name() : "");
		obj.put("trCdType", en.getNew_tracecode_type() != null ? en.getNew_tracecode_type() : 0);
	}
	

	private void logError(Exception e) {
		log.error("에러 메시지 : {}", e.getMessage());
		errorLogger.error("ServiceMsgObjOrcl - 에러메시지 = " + e);
	}


}
