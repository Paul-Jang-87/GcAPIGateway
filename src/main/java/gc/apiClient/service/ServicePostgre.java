package gc.apiClient.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.datamapping.MappingCenter;
import gc.apiClient.embeddable.CampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.Entity_CampMaJson;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.repository.postgresql.Repository_CampMa;
import gc.apiClient.repository.postgresql.Repository_CampRt;
import gc.apiClient.repository.postgresql.Repository_ContactLt;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicePostgre implements InterfaceDBPostgreSQL {
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_ContactLt repositoryContactLt;
	private final CustomProperties customProperties;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa,
			Repository_ContactLt repositoryContactLt, CustomProperties customProperties) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryContactLt = repositoryContactLt;
		this.customProperties = customProperties;
	}

	// **Create

	@Override
	public Entity_CampRt createCampRtMsg(String cpid) {
		// contactid::contactListId::cpid::CPSQ::dirt::tkda::dateCreated

		log.info(" ");
		log.info("====== Class : ServicePostgre & Method : createCampRtMsg ======");

		log.info("(cpid붙여서)들어온 rs : {}", cpid);
		Entity_CampRt enCampRt = new Entity_CampRt();
		CampRt id = new CampRt();
		String parts[] = cpid.split("::");

		int rlsq = 0;
		int coid = 0;
		int cpsq = Integer.parseInt(parts[3]);
		int hubId = 0;
		int dirt = 0;
		int dict = 0;
		String campid = parts[2];
		String contactLtId = parts[1];
		String contactId = parts[0];
		String tkda = parts[5];
		Date didt = null;

		log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 ------");
		log.info("rlsq: {}", rlsq);
		log.info("coid: {}", coid);
		log.info("cpsq: {}", cpsq);
		log.info("hubid: {}", hubId);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);
		log.info("campid: {}", campid);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("tkda: {}", tkda);
		log.info("didt: {}", parts[6]);
		log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 끝------");

		if (tkda.charAt(0) == 'C') {
			hubId = Integer.parseInt(tkda.split(",")[1]);
		} else if (tkda.charAt(0) == 'A') {
			cpsq = Integer.parseInt(tkda.split("\\|\\|")[5]);
		} else {
		}

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			log.info("didt(포맷 변경 전) : {}", parts[6]);
			Date parsedDate = inputFormat.parse(parts[6]);

			// Formatting the parsed date to the desired format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String formattedDateString = outputFormat.format(parsedDate);
			Date formattedDate = outputFormat.parse(formattedDateString);
			didt = formattedDate;
			log.info("didt(포맷 변경 후) : {}", didt);
		} catch (ParseException e) {
		}

		log.info("dirt(맵핑 전) : {}", parts[4]);
		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[4], "6"));
		log.info("dirt(맵핑 후) : {}", dirt);

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		ServiceJson sv = new ServiceJson();
		dict = sv.ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = enCampMa.getCoid();
		log.info("campid({})로 조회한 레코드의 coid : {}", campid, coid);

		rlsq = findCampRtMaxRlsq().intValue();
		log.info("camprt테이블에서 현재 가장 큰 rlsq 값 : {}", rlsq);
		rlsq++;
		log.info("가져온 rlsq의 값에 +1 : {}", rlsq);

		id.setRlsq(rlsq);
		id.setCoid(coid);
		enCampRt.setId(id);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactid(contactId);
		enCampRt.setCpid(campid);
		enCampRt.setTkda(tkda);
		enCampRt.setCamp_seq(cpsq);
		enCampRt.setHubid(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);

		log.info("------ return 하기 전 변수들의 최종 값 확인 ------");
		log.info("rlsq: {}", rlsq);
		log.info("coid: {}", coid);
		log.info("campid: {}", campid);
		log.info("cpsq: {}", cpsq);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("tkda: {}", tkda);
		log.info("hubid: {}", hubId);
		log.info("didt: {}", didt);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);
		log.info("------ return 하기 전 변수들의 최종 값 확인 ------");

		log.info("===== End createCampRtMsg =====");

		return enCampRt;
	}

	@Override
	public Entity_CampRtJson createCampRtJson(Entity_CampRt enCampRt,String business) {// contactid(고객키)::contactListId::didt::dirt::cpid

		Entity_CampRtJson enCampRtJson = new Entity_CampRtJson();
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = now.format(formatter);

		int hubId = enCampRt.getHubid();
		int dirt = enCampRt.getDirt();
		int dict = enCampRt.getDict();
		int cpSeq = enCampRt.getCamp_seq();
		String coid = "";
		String campid = enCampRt.getCpid();
		String didt = "";

		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String formattedDateString = outputFormat.format(enCampRt.getDidt());
		didt = formattedDateString;

		dirt = enCampRt.getDirt();

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		ServiceJson sv = new ServiceJson();
		dict = sv.ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = Integer.toString(enCampMa.getCoid());
		MappingCenter mappingData = new MappingCenter();
		coid = mappingData.getCentercodeById(coid);
		
		if(business.equals("CALLBOT")) {
			
			enCampRtJson.setTopcDataIsueDtm(topcDataIsueDtm);
			enCampRtJson.setCpid(campid);
			enCampRtJson.setCamp_seq(cpSeq);
			enCampRtJson.setLastAttempt(didt);
			enCampRtJson.setLastResult(dirt);
			enCampRtJson.setTotAttempt(dict);
		}else {
			enCampRtJson.setTopcDataIsueDtm(topcDataIsueDtm);
			enCampRtJson.setIbmHubId(hubId);
			enCampRtJson.setCenterCd(coid);
			enCampRtJson.setLastAttempt(didt);
			enCampRtJson.setLastResult(dirt);
			enCampRtJson.setTotAttempt(dict);
			
		}
		return enCampRtJson;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String msg, String crudtype) { // cpid::coid::cpna::division

		log.info(" ");
		log.info("====== ClassName : ServicePostgre & Method : createCampMaMsg ======");

		Entity_CampMa enCampMa = new Entity_CampMa();
		String parts[] = msg.split("::");
		String cpid = "";
		int coid = 0;
		String cpna = "";

		switch (crudtype) {// cpid::cpna::division::coid
		case "insert":
			log.info("action type : {}", crudtype);
			cpid = parts[0];// 캠페인 아이디
			coid = Integer.parseInt(parts[1]); // 센터구분 코드
			cpna = parts[3]; // 캠페인 명
			break;

		case "update": // cpid::coid::cpna::divisionid::action
			log.info("action type : {}", crudtype);
			cpid = "";
			coid = 0;
			cpna = parts[2]; // 캠페인 명
			break;

		default: // cpid::coid::cpna::divisionid::action
			log.info("action type : {}", crudtype);
			cpid = parts[0];// 캠페인 아이디
			coid = Integer.parseInt(parts[1]); // 센터구분 코드
			cpna = "";
			break;
		}

		enCampMa.setCpid(cpid);
		enCampMa.setCoid(coid);
		enCampMa.setCpna(cpna);

		log.info("cpid : {}", cpid);
		log.info("coid : {}", coid);
		log.info("cpna : {}", cpna);

		return enCampMa;
	}

	@Override
	public Entity_CampMaJson createCampMaJson(Entity_CampMa enCampMa, String datachgcd) { // cpid::cpna::division::coid

		log.info(" ");
		log.info("====== ClassName : ServicePostgre & Method : createCampMaJson ======");
		Entity_CampMaJson enCampMaJson = new Entity_CampMaJson();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSSSS");
		String topcDataIsueDtm = "";

		switch (datachgcd) {

		case "insert":

			enCampMaJson.setTenantId(Integer.toString(enCampMa.getCoid()));
			enCampMaJson.setCmpnId(enCampMa.getCpid());
			enCampMaJson.setCmpnNm(enCampMa.getCpna());

			topcDataIsueDtm = now.format(formatter);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("N");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);

			break;

		case "update":

			enCampMaJson.setTenantId("");
			enCampMaJson.setCmpnId("");
			enCampMaJson.setCmpnNm(enCampMa.getCpna());

			topcDataIsueDtm = now.format(formatter);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("N");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);

			break;

		default:

			enCampMaJson.setTenantId(Integer.toString(enCampMa.getCoid()));
			enCampMaJson.setCmpnId(enCampMa.getCpid());
			enCampMaJson.setCmpnNm("");

			topcDataIsueDtm = now.format(formatter);

			enCampMaJson.setDataChgCd(datachgcd);
			enCampMaJson.setDataDelYn("Y");
			enCampMaJson.setTopcDataIsueDtm(topcDataIsueDtm);
			break;
		}
		log.info("====== End createCampMaJson ======");
		return enCampMaJson;

	}

	@Override
	public Entity_ContactLt createContactLtMsg(String msg) {// (콜봇에서 뽑아온거)cpid::cpsq::cske::csna::tkda::flag

		log.info(" ");
		log.info("====== ClassName : ServicePostgre & Method : createContactLtMsg ======");

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		String ContactLvalues[] = msg.split("::");

		log.info("msg : {}", msg);
		// 임시로 데이터 적재
		id.setCpid(ContactLvalues[0]);
		id.setCpsq(Integer.parseInt(ContactLvalues[1]));
		enContactLt.setId(id);
		enContactLt.setCske(ContactLvalues[2]);// "customerkey"
		enContactLt.setCsna(ContactLvalues[3]);// "카리나"
		enContactLt.setFlag(ContactLvalues[5]);// "HO2"
		enContactLt.setTkda(ContactLvalues[4]);// "custid,111"

		log.info("cpid : {}", ContactLvalues[0]);
		log.info("cpsq : {}", Integer.parseInt(ContactLvalues[1]));
		log.info("cske : {}", ContactLvalues[2]);
		log.info("csna : {}", ContactLvalues[3]);
		log.info("flag : {}", ContactLvalues[5]);
		log.info("tkda : {}", ContactLvalues[4]);

		return enContactLt;
	}

	@Override
	public String createContactLtGC(String msg) {
		//뽑아온다(콜봇).cpid::cpsq::cske::csna::tkda::flag::contactltId::queid
		log.info(" ");
		log.info("===== ClassName : ServicePostgre & Method : createContactLtGC =====");
		log.info("msg : {}",msg);
		
			String values[] = msg.split("::");
			
			JSONObject data = new JSONObject();
			JSONObject mainObj = new JSONObject();
			data.put("CPID", values[0]);
			data.put("CPSQ", values[1]);
			data.put("CSKE", values[2]);
			data.put("CSNA", values[3]);
			data.put("TKDA", values[4]);
			data.put("TNO1", "");
			data.put("TNO2", "");
			data.put("TNO3", "");
			data.put("TNO4", "");
			data.put("TNO5", "");
			data.put("TLNO", "");
			data.put("TMZO", "Asia/Seoul (+09:00)");
			data.put("QUEUEID", values[7]);
			data.put("TRYCNT", "0");
			
			mainObj.put("data", data);
			mainObj.put("id", values[2]);
			mainObj.put("contactListId", values[6]);
			log.info("CPID :{}", values[0]);
			log.info("CPSQ :{}", values[1]);
			log.info("CSKE :{}", values[2]);
			log.info("CSNA :{}", values[3]);
			log.info("TKDA :{}", values[4]);
			log.info("TNO1 :{}", "");
			log.info("TNO2 :{}", "");
			log.info("TNO3 :{}", "");
			log.info("TNO4 :{}", "");
			log.info("TNO5 :{}", "");
			log.info("TLNO :{}", "");
			log.info("QUEUEID :{}", values[7]);
			log.info("trycnt :{}", "0");
			log.info("TMZO :{}", "Asia/Seoul (+09:00)");
			
			return mainObj.toString();
	}

	// **Insert
	@Override
	public Entity_CampRt InsertCampRt(Entity_CampRt entity_CampRt) {

		Optional<Entity_CampRt> existingEntity = repositoryCampRt.findById(entity_CampRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("Record with the given composite key already exists.");
		}

		return repositoryCampRt.save(entity_CampRt);

	}

	@Override
	public Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa) {

		Optional<Entity_CampMa> existingEntity = repositoryCampMa.findByCpid(entityCampMa.getCpid());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("Record with 'cpid' already exists.");
		}

		return repositoryCampMa.save(entityCampMa);
	}

	@Override
	public Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt) {

		Optional<Entity_ContactLt> existingEntity = repositoryContactLt.findById(entityContactLt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("Record with the given composite key already exists.");
		}

		return repositoryContactLt.save(entityContactLt);

	}

	@Override
	public Entity_CampMa findCampMaByCpid(String cpid) {

		try {
			Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampMa by cpid: {}", cpid, ex);

			return null;
		}
	}

	@Override
	public Entity_CampRt findCampRtByCpid(String cpid) {

		try {
			Optional<Entity_CampRt> optionalEntity = repositoryCampRt.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampRt by cpid: {}", cpid, ex);

			return null;
		}
	}

	@Override
	public Integer findCampRtMaxRlsq() {

		try {
			Optional<Integer> optionalEntity = repositoryCampRt.findMaxRlsq();
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampRt which has hightest value of 'rlsq' column: {}", ex);

			return null;
		}
	}

	@Override
	public List<Entity_ContactLt> findContactLtByCpid(String id) {

		List<Entity_ContactLt> resultList = repositoryContactLt.findByCpid(id);
		if (!resultList.isEmpty()) {
			return resultList;
		} else {
			log.error("records can't be found with {}", id);
			return null;
		}
	}

	@Override
	public Entity_ContactLt findContactLtByCske(String cske) {

		try {
			Optional<Entity_ContactLt> optionalEntity = repositoryContactLt.findByCske(cske);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving contact by cske: {}", cske, ex);

			return null;
		}
	}

	@Override
	public int getRecordCount() {
		log.info("Campma 테이블 레코드 수 : {}", repositoryCampMa.countBy());
		return repositoryCampMa.countBy();
	}

	@Override
	public void DelCampMaById(String cpid) {
		repositoryCampMa.deleteById(cpid);
	}

	@Override
	@Transactional
	public void UpdateCampMa(String cpid, String cpna) {
		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findById(cpid);

		if (optionalEntity.isPresent()) {
			Entity_CampMa entity = optionalEntity.get();
			entity.setCpna(cpna);
			repositoryCampMa.save(entity);
		} else {
			throw new EntityNotFoundException("Entity not found with CPID: " + cpid);
		}
	}

}
