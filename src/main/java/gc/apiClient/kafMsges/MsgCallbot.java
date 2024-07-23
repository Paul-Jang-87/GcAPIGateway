package gc.apiClient.kafMsges;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.datamapping.MappingCenter;
import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceKafMsg;
import gc.apiClient.service.ServiceJson;
import gc.apiClient.service.ServiceWebClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/**
 * 'MessageToProducer'클래스를 보면 'sendMsgToProducer' 함수에 두번째 매개변수로 메시지가 들어간다. 
 * 이 클래스는 거기에 들어가 메시지를 만드는 클래스이다. 특지 CallBot비지니스 로직과 관련된 메시지만을 다룬다.
 * 
 */
public class MsgCallbot implements InterfaceKafMsg {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private InterfaceDBPostgreSQL serviceDb;

	public MsgCallbot(InterfaceDBPostgreSQL serviceDb) {
		this.serviceDb = serviceDb;
	}

	public MsgCallbot() {
	}

	@Override
	/**
	 * 캠페인 마스터와 관련된 메시지를 만들어주는 클래스이다. 
	 * datachgcd(insert,update,delete)에 따라 보내질 메시지 내용이 달라진다. 
	 */
	public String makeMaMsg(Entity_CampMa enCampMa, String datachgcd) throws Exception {

		log.info("====== Method : maMassage ======");
		Entity_CampMaJson enCampMaJson = new Entity_CampMaJson();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";

		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = "";

		switch (datachgcd.trim()) {

		case "insert":
		case "update":

			enCampMaJson.setTenantId(Integer.toString(enCampMa.getCoid()));
			enCampMaJson.setCmpnId(enCampMa.getCpid());
			enCampMaJson.setCmpnNm(enCampMa.getCpna());

			topcDataIsueDtm = formatter.format(now);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("N");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);

			break;

		case "delete":

			enCampMaJson.setTenantId(Integer.toString(enCampMa.getCoid()));
			enCampMaJson.setCmpnId(enCampMa.getCpid());
			enCampMaJson.setCmpnNm("");

			topcDataIsueDtm = formatter.format(now);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("Y");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
			break;

		default:

			log.info("유효하지 않은 CRUD 작업요청입니다. : {}", datachgcd);
			break;
		}

		jsonString = objectMapper.writeValueAsString(enCampMaJson);
		log.info("enCampMaJson : {}", jsonString);
		return jsonString;
	}

	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception {

		JSONObject obj = new JSONObject();
		try {
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
			String topcDataIsueDtm = formatter.format(now);

			int dirt = enCampRt.getDirt();
			int dict = enCampRt.getDict();
			int cpSeq = enCampRt.getCamp_seq();
			String coid = "";
			String campid = enCampRt.getCpid();
			String didt = "";
			SimpleDateFormat outputFormat = null;
			String formattedDateString = "";

			if (enCampRt.getDidt() != null) {
				outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				formattedDateString = outputFormat.format(enCampRt.getDidt());
				didt = formattedDateString;
			}

			dirt = enCampRt.getDirt();

			ServiceWebClient crmapi = new ServiceWebClient();
			String result = crmapi.getStatusApiReq("campaign_stats", campid);
			dict = ServiceJson.extractIntVal("ExtractDict", result);

			Entity_CampMa enCampMa = new Entity_CampMa();

			enCampMa = serviceDb.findCampMaByCpid(campid);
			coid = Integer.toString(enCampMa.getCoid());
			MappingCenter mappingData = new MappingCenter();
			coid = mappingData.getCentercodeById(coid);
			coid = coid != null ? coid : "EX";

			if (!didt.equals("")) {
				outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				formattedDateString = outputFormat.format(enCampRt.getDidt());
				didt = formattedDateString;
			}

			obj.put("topcDataIsueDtm", topcDataIsueDtm);
			obj.put("cpId", campid);
			obj.put("cpSeq", cpSeq);
			obj.put("lastAttempt", didt);
			obj.put("attmpNo", dict);
			obj.put("lastResult", dirt);

		}

		catch (Exception e) {
			errorLogger.error(e.getMessage(), e);
		}

		return obj.toString();
	}

}
