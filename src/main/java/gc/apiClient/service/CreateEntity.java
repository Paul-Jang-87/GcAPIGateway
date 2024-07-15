package gc.apiClient.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import gc.apiClient.embeddable.CampRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateEntity {
	
//	public Entity_CampRt createCampRtMsg(String cpid, Entity_CampMa enCampMa,int rlsq) {
//		// contactid::contactListId::cpid::CPSQ::dirt::tkda::dateCreated
//
//		log.info("====== Method : createCampRtMsg ======");
//
//		log.info("들어온 rs : {}", cpid);
//		Entity_CampRt enCampRt = new Entity_CampRt();
//		CampRt id = new CampRt();
//		String parts[] = cpid.split("::");
//
//		int coid = 0;
//		int cpsq = Integer.parseInt(parts[3]);
//		long hubId = 0;
//		int dirt = 0;
//		int dict = 0;
//		String campid = parts[2];
//		String contactLtId = parts[1];
//		String contactId = parts[0];
//		String tkda = parts[5];
//		Date didt = null;
//
//		try {
//
//			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 ------");
//			log.info("coid: {}", coid);
//			log.info("cpsq: {}", cpsq);
//			log.info("hubid: {}", hubId);
//			log.info("dirt: {}", dirt);
//			log.info("dict: {}", dict);
//			log.info("campid: {}", campid);
//			log.info("contactLtId: {}", contactLtId);
//			log.info("contactId: {}", contactId);
//			log.info("tkda: {}", tkda);
//			log.info("didt: {}", parts[6]);
//			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 끝 ------");
//
//			if (tkda.charAt(0) == 'C') {
//				hubId = Long.parseLong(tkda.split(",")[1]);
//			} else if (tkda.charAt(0) == 'A') {
//				cpsq = Integer.parseInt(tkda.split("\\|\\|")[5]);
//			} else {
//			}
//
//			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//			log.info("didt(포맷 변경 전) : {}", parts[6]);
//			Date parsedDate = inputFormat.parse(parts[6]);
//
//			// Formatting the parsed date to the desired format
//			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//			String formattedDateString = outputFormat.format(parsedDate);
//			Date formattedDate = outputFormat.parse(formattedDateString);
//			didt = formattedDate;
//			log.info("didt(포맷 변경 후) : {}", didt);
//
//			log.info("dirt(맵핑 전) : {}", parts[4]);
//			Map<String, String> properties = customProperties.getProperties();
//			dirt = Integer.parseInt(properties.getOrDefault(parts[4], "1").trim());
//			log.info("dirt(맵핑 후) : {}", dirt);
//
//			ServiceWebClient crmapi = new ServiceWebClient();
//			String result = crmapi.getStatusApiReq("campaign_stats", campid);
//			dict = ServiceJson.extractIntVal("ExtractDict", result);
//
//			coid = enCampMa.getCoid();
//			log.info("campid({})로 조회한 레코드의 coid : {}", campid, coid);
//
//			log.info("camprt테이블에서 현재 가장 큰 rlsq 값 : {}", rlsq);
//			rlsq++;
//			log.info("가져온 rlsq의 값에 +1 : {}", rlsq);
//
//			id.setRlsq(rlsq);
//			id.setCoid(coid);
//			enCampRt.setId(id);
//			enCampRt.setContactLtId(contactLtId);
//			enCampRt.setContactid(contactId);
//			enCampRt.setCpid(campid);
//			enCampRt.setTkda(tkda);
//			enCampRt.setCamp_seq(cpsq);
//			enCampRt.setHubid(hubId);
//			enCampRt.setDidt(didt);
//			enCampRt.setDirt(dirt);
//			enCampRt.setDict(dict);
//
//			log.info("------ return 하기 전 변수들의 최종 값 확인 ------");
//			log.info("rlsq: {}", rlsq);
//			log.info("coid: {}", coid);
//			log.info("campid: {}", campid);
//			log.info("cpsq: {}", cpsq);
//			log.info("contactLtId: {}", contactLtId);
//			log.info("contactId: {}", contactId);
//			log.info("tkda: {}", tkda);
//			log.info("hubid: {}", hubId);
//			log.info("didt: {}", didt);
//			log.info("dirt: {}", dirt);
//			log.info("dict: {}", dict);
//			log.info("------ return 하기 전 변수들의 최종 값 확인 ------");
//
//		} catch (Exception e) {
//			log.error("Error Message : {}", e.getMessage());
//			errorLogger.error(e.getMessage(), e);
//		}
//
//		return enCampRt;
//	}
	
	
	

}
