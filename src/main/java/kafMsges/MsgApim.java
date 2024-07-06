package kafMsges;

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
public class MsgApim implements InterfaceKafMsg {

	@Override
	public String maMessage(Entity_CampMa enCampMa, String datachgcd) throws Exception {

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
	public String rtMessage(Entity_CampRt enCampRt) throws Exception {
		return null;
	}

}
