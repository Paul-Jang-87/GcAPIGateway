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
		
		if(enCampMa.getCpid().equals("")) {//키 값이 없으면(정상이 아닐 경우) 함수 바로 종료
			return "";
		}

		JSONObject obj = new JSONObject();
		MappingCenter mappingData = new MappingCenter();
		String cpid = "";
		String coid = "";
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

			coid = mappingData.getCentercodeById(temp_coid);
			coid = coid != null ? coid : "EX";
			obj.put("cpid", cpid);
			obj.put("gubun", coid);
			obj.put("cpna", cpna);
			obj.put("cmd", datachgcd);

			break;

		case "update":

			coid = mappingData.getCentercodeById(temp_coid);
			coid = coid != null ? coid : "EX";
			obj.put("cpid", cpid);
			obj.put("gubun", coid);
			obj.put("cpna", cpna);
			obj.put("cmd", datachgcd);

			break;
			
		case "delete":
			
			coid = mappingData.getCentercodeById(temp_coid);
			coid = coid != null ? coid : "EX";
			obj.put("cpid", cpid);
			obj.put("gubun", coid);
			obj.put("cpna", cpna);
			obj.put("cmd", datachgcd);
			
			break;

		default:
			log.info("(makeMaMsg) - 유효하지 않은 CRUD 작업요청입니다. : {}",datachgcd);
			break;
		}
		log.info("(makeMaMsg) - enCampMaJson : {}", obj.toString());
		return obj.toString();
	}

	
	public Entity_ToApim rstMassage(Entity_CampRt enCampRt) throws Exception {
		
		Entity_ToApim enToApim = new Entity_ToApim();
		int dirt = 0 ;
		int dict = 0 ; 
		String tokendata = ""; 
		
		try {
			
			dirt = enCampRt.getDirt();// 응답코드
			dict = enCampRt.getDict();// 발신시도 횟수
			tokendata = enCampRt.getTkda();// 토큰데이터
			
			enToApim.setDirt(dirt);
			enToApim.setDict(dict);
			enToApim.setTkda(tokendata);
			
			return enToApim;
			
		} catch (Exception e) {// 2024-07-31 파싱 에러가 나면 초기 인입 값이 어떻게 들어왔는지 확인하기 쉽게 에러 로그에 찍어주고 초기값으로 세팅해줌(로직이 막혀서 서비스에 지장 줄 일 없게 끔).
			log.error("(rstMassage) - 파싱 중 에러가 발생했습니다. 초기 인입 값을 다시 확인해주세요 : {}", enCampRt.toString());
			return null;
		}
		
	}


	@Override
	public String makeRtMsg(Entity_CampRt enCampRt) throws Exception {
		return null;
	}

}
