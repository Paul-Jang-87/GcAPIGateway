package gc.apiClient.kafMsges;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.interfaceCollection.InterfaceKafMsg;
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

	@Override
	/**
	 * 캠페인 마스터와 관련된 메시지를 만들어주는 클래스이다. 
	 * datachgcd(insert,update,delete)에 따라 보내질 메시지 내용이 달라진다. 
	 */
	public String makeMaMsg(Entity_CampMa enCampMa, String datachgcd) throws Exception {

		Entity_CampMaJson enCampMaJson = new Entity_CampMaJson();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";

		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = "";
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

			enCampMaJson.setTenantId(temp_coid);
			enCampMaJson.setCmpnId(cpid);
			enCampMaJson.setCmpnNm(cpna);

			topcDataIsueDtm = formatter.format(now);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("N");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);

			break;

		case "delete":

			enCampMaJson.setTenantId(temp_coid);
			enCampMaJson.setCmpnId(cpid);
			enCampMaJson.setCmpnNm("");

			topcDataIsueDtm = formatter.format(now);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("Y");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
			break;

		default:

			log.info("(makeMaMsg) - 유효하지 않은 CRUD 작업요청입니다. : {}", datachgcd);
			break;
		}

		jsonString = objectMapper.writeValueAsString(enCampMaJson);
		log.info("(makeMaMsg) - enCampMaJson : {}", jsonString);
		return jsonString;
	}
	

	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception {
		
		if( enCampRt.getId().getCoid() == 0 && enCampRt.getId().getRlsq() == 0 ) {//테이블 키 값이 없는경우(정상이 아닐 경우) 바로 함수 종료
			return "";
		}

		JSONObject obj = new JSONObject();
		SimpleDateFormat outputFormat = null;
		String formattedDateString = "";
		String didt = "";
		String campid = "";
		Date date = null;
		int dirt = 0;
		int dict = 0;
		int cpSeq = 0;
		
		try {
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS");
			String topcDataIsueDtm = formatter.format(now);

			dirt = enCampRt.getDirt();
			dict = enCampRt.getDict();
			dirt = enCampRt.getDirt();
			cpSeq = enCampRt.getCamp_seq();
			campid = enCampRt.getCpid();
			date = enCampRt.getDidt();

			if (date != null) {
				outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				formattedDateString = outputFormat.format(date);
				didt = formattedDateString;
			}


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

			return obj.toString();
			
		}catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).
			
			log.error("(makeRtMsg) - 파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", enCampRt.toString());
			errorLogger.error(e.getMessage(), e);
			return "";
		}
	}

}
