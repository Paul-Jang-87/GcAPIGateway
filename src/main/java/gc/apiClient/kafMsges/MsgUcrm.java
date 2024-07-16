package gc.apiClient.kafMsges;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.datamapping.MappingCenter;
import gc.apiClient.entity.Entity_CampMaJsonUcrm;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceKafMsg;
import gc.apiClient.service.ServiceJson;
import gc.apiClient.service.ServiceWebClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsgUcrm implements InterfaceKafMsg { //카프카 프로듀서로 보내기 위한 UCRM 메시지만을 모아둔 클래스
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private InterfaceDBPostgreSQL serviceDb;

	public MsgUcrm(InterfaceDBPostgreSQL serviceDb) {
		this.serviceDb = serviceDb;
	}

	public MsgUcrm() {
	}

	@Override
	public String makeMaMsg(Entity_CampMa enCampMa, String datachgcd) throws Exception {  // MA 메시지

		log.info("====== Method : maMassage ======");
		Entity_CampMaJsonUcrm enCampMaJson = new Entity_CampMaJsonUcrm();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";

		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = "";
		String coid = "";
		MappingCenter mappingData = new MappingCenter();

		switch (datachgcd.trim()) {
			case "insert":
			case "update":
	
				coid = mappingData.getCentercodeById(Integer.toString(enCampMa.getCoid()));//센터코드 맵핑된 거 'id'를 키 값으로 하여 가지고 옴. 
				coid = coid != null ? coid : "EX";
				enCampMaJson.setCenterCd(coid);//센터코드
				enCampMaJson.setCmpnId(enCampMa.getCpid());//캠페인아이디
				enCampMaJson.setCmpnNm(enCampMa.getCpna());//캠페인명
	
				topcDataIsueDtm = formatter.format(now);
	
				enCampMaJson.setDataChgCd(datachgcd);
				enCampMaJson.setDataDelYn("N");
				enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
				break;
				
			case "delete":
				coid = mappingData.getCentercodeById(Integer.toString(enCampMa.getCoid()));
				coid = coid != null ? coid : "EX";
				enCampMaJson.setCenterCd(coid);
				enCampMaJson.setCmpnId(enCampMa.getCpid());
				enCampMaJson.setCmpnNm("");
	
				topcDataIsueDtm = formatter.format(now);
	
				enCampMaJson.setDataChgCd(datachgcd);
				enCampMaJson.setDataDelYn("Y");
				enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
				break;
				
			default:
			
				log.info("유효하지 않은 CRUD 작업요청입니다. : {}",datachgcd);
				break;
		}

		jsonString = objectMapper.writeValueAsString(enCampMaJson);	// 객체를 String 타입으로 변환. 
		log.info("enCampMaJson : {}", jsonString);
		return jsonString;
	}

	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception { //RT 메시지, 결과 발신 메시지.

		JSONObject obj = new JSONObject();
		try {
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
			String topcDataIsueDtm = formatter.format(now);

			long hubId = enCampRt.getHubid();
			int dirt = enCampRt.getDirt();
			int dict = enCampRt.getDict();
			String coid = "";
			String campid = enCampRt.getCpid();
			String didt = "";
			String formattedTime = "";

			if( enCampRt.getDidt() !=null ) {
				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				String formattedDateString = outputFormat.format(enCampRt.getDidt());
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

			if(!didt.equals("")) {
				
				String dateString = didt;
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(dateString, format);
				LocalDateTime adjustedDateTime = dateTime.plusHours(9);
				
				ZonedDateTime desiredTime = adjustedDateTime.atZone(ZoneId.of("UTC+09:00"));
				formattedTime = desiredTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}

			obj.put("topcDataIsueDtm", topcDataIsueDtm);
			obj.put("ibmHubId", hubId);
			obj.put("centerCd", coid);
			obj.put("lastAttempt", formattedTime);
			obj.put("totAttempt", dict);
			obj.put("lastResult", dirt);

		} catch (Exception e) {
			log.error("Error Message : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return obj.toString();
	}

}
