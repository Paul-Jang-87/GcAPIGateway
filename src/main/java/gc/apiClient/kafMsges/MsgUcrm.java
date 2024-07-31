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
import gc.apiClient.interfaceCollection.InterfaceKafMsg;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/**
 * 'MessageToProducer'클래스를 보면 'sendMsgToProducer' 함수에 두번째 매개변수로 메시지가 들어간다. 
 * 이 클래스는 거기에 들어가 메시지를 만드는 클래스이다. 특지 UCRM비지니스 로직과 관련된 메시지만을 다룬다.
 * 
 */
public class MsgUcrm implements InterfaceKafMsg { 
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	
	@Override
	/**
	 * 캠페인 마스터와 관련된 메시지를 만들어주는 클래스이다. 
	 * datachgcd(insert,update,delete)에 따라 보내질 메시지 내용이 달라진다. 
	 */
	public String makeMaMsg(Entity_CampMa enCampMa, String datachgcd) throws Exception {  // MA 메시지
		
		if(enCampMa.getCpid().equals("")) {//키 값이 없으면(정상이 아닐 경우) 함수 바로 종료
			return "";
		}

		Entity_CampMaJsonUcrm enCampMaJson = new Entity_CampMaJsonUcrm();
		ObjectMapper objectMapper = new ObjectMapper();
		MappingCenter mappingData = new MappingCenter();
		String jsonString = "";

		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = "";
		String coid = "";
		String cpid = "";
		String cpna = "";
		String temp_coid = "";
		
		try {
			
			cpid = enCampMa.getCpid();
			cpna = enCampMa.getCpna();
			temp_coid = Integer.toString(enCampMa.getCoid());
			
		} catch (Exception e) {//2024-07-31 데이터 가져오는 과정 중 에러 발생 시 공백으로 리턴 후 종료
			return "";
		}

		switch (datachgcd.trim()) {
			case "insert":
			case "update":
	
				coid = mappingData.getCentercodeById(temp_coid);//센터코드 맵핑된 거 'id'를 키 값으로 하여 가지고 옴. 
				coid = coid != null ? coid : "EX";
				enCampMaJson.setCenterCd(coid);//센터코드
				enCampMaJson.setCmpnId(cpid);//캠페인아이디
				enCampMaJson.setCmpnNm(cpna);//캠페인명
	
				topcDataIsueDtm = formatter.format(now);
	
				enCampMaJson.setDataChgCd(datachgcd);
				enCampMaJson.setDataDelYn("N");
				enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
				break;
				
			case "delete":
				coid = mappingData.getCentercodeById(temp_coid);
				coid = coid != null ? coid : "EX";
				enCampMaJson.setCenterCd(coid);
				enCampMaJson.setCmpnId(cpid);
				enCampMaJson.setCmpnNm("");
	
				topcDataIsueDtm = formatter.format(now);
	
				enCampMaJson.setDataChgCd(datachgcd);
				enCampMaJson.setDataDelYn("Y");
				enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
				break;
				
			default:
			
				log.info("(makeMaMsg) - 유효하지 않은 CRUD 작업요청입니다. : {}",datachgcd);
				break;
		}

		jsonString = objectMapper.writeValueAsString(enCampMaJson);	// 객체를 String 타입으로 변환. 
		log.info("(makeMaMsg) - enCampMaJson : {}", jsonString);
		return jsonString;
	}

	
	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception { //RT 메시지, 발신결과 메시지.
		
		if( enCampRt.getId().getCoid() == 0 && enCampRt.getId().getRlsq() == 0 ) {//테이블 키 값이 없는경우(정상이 아닐 경우) 바로 함수 종료
			return "";
		}

		JSONObject obj = new JSONObject();
		String coid = "";
		String temp_coid = "";
		String didt = "";
		String formattedTime = "";
		Date date = null;
		long hubId = 0;
		int dirt = 0;
		int dict = 0;
		
		try {
			
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
			String topcDataIsueDtm = formatter.format(now);

			hubId = enCampRt.getHubid();
			dirt = enCampRt.getDirt();
			dict = enCampRt.getDict();
			date = enCampRt.getDidt();
			dirt = enCampRt.getDirt();
			temp_coid = Integer.toString(enCampRt.getId().getCoid());

			if( date !=null ) {
				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				String formattedDateString = outputFormat.format(date);
				didt = formattedDateString;
			}

			coid = temp_coid;
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

			return obj.toString();
			
		} catch (Exception e) {
			
			log.error("(makeRtMsg) - 파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", enCampRt.toString());
			errorLogger.error(e.getMessage(), e);
			return "";
		}

	}

}
