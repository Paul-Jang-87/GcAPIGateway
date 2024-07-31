package gc.apiClient.kafMsges;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import gc.apiClient.datamapping.MappingCenter;
import gc.apiClient.entity.Entity_ToApim;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.interfaceCollection.InterfaceKafMsg;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

/**
 * APIM으로 보낼 데이터들을 APIM의 형식에 맞는 메시지 형태로 변환해주는 클래스
 * UCRM,CallBot,APIM 세 로직 모두 첫째, 캠페인 마스터 관련 정보(신규 캠페인 정보, 캠페인 업데이트정보)와
 * 둘째, 콜에 대한 발신 결과 정보를 필요로한다. 세 로직 정보 필요로하는 정보의 종류는 같지만 
 * 상세 데이터와 데이터 형식은 로직마다 다르다. 
 */


public class MsgApim implements InterfaceKafMsg {

	@Override
	public String makeMaMsg(Entity_CampMa enCampMa, String datachgcd) throws Exception {

		log.info("====== Method : maMassage ======");

		JSONObject obj = new JSONObject();
		String coid = "";
		MappingCenter mappingData = new MappingCenter();

		switch (datachgcd.trim()) {

		case "insert":

			coid = mappingData.getCentercodeById(Integer.toString(enCampMa.getCoid()));
			coid = coid != null ? coid : "EX";
			obj.put("cpid", enCampMa.getCpid());
			obj.put("gubun", coid);
			obj.put("cpna", enCampMa.getCpna());
			obj.put("cmd", datachgcd);

			break;

		case "update":

			coid = mappingData.getCentercodeById(Integer.toString(enCampMa.getCoid()));
			coid = coid != null ? coid : "EX";
			obj.put("cpid", enCampMa.getCpid());
			obj.put("gubun", coid);
			obj.put("cpna", enCampMa.getCpna());
			obj.put("cmd", datachgcd);

			break;
			
		case "delete":
			
			coid = mappingData.getCentercodeById(Integer.toString(enCampMa.getCoid()));
			coid = coid != null ? coid : "EX";
			obj.put("cpid", enCampMa.getCpid());
			obj.put("gubun", coid);
			obj.put("cpna", enCampMa.getCpna());
			obj.put("cmd", datachgcd);
			
			break;

		default:
			log.info("유효하지 않은 CRUD 작업요청입니다. : {}",datachgcd);
			break;
		}
		log.info("enCampMaJson : {}", obj.toString());
		return obj.toString();
	}

	
	public Entity_ToApim rstMassage(Entity_CampRt enCampRt) throws Exception {
		
		int dirt = enCampRt.getDirt();// 응답코드
		int dict = enCampRt.getDict();// 발신시도 횟수
		String tokendata = enCampRt.getTkda();// 토큰데이터
		
		Entity_ToApim enToApim = new Entity_ToApim();
		enToApim.setDirt(dirt);
		enToApim.setDict(dict);
		enToApim.setTkda(tokendata);
		
		return enToApim;
		
	}


	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception {
		return null;
	}

}
