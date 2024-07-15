package gc.apiClient.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.data.domain.Page;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.embeddable.ApimCampRt;
import gc.apiClient.embeddable.CallBotCampRt;
import gc.apiClient.embeddable.CampRt;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.embeddable.Ucrm;
import gc.apiClient.embeddable.UcrmCampRt;
import gc.apiClient.entity.postgresql.Entity_ApimRt;
import gc.apiClient.entity.postgresql.Entity_CallbotRt;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.repository.postgresql.Repository_ApimRt;
import gc.apiClient.repository.postgresql.Repository_CallbotRt;
import gc.apiClient.repository.postgresql.Repository_CampMa;
import gc.apiClient.repository.postgresql.Repository_CampRt;
import gc.apiClient.repository.postgresql.Repository_ContactLt;
import gc.apiClient.repository.postgresql.Repository_Ucrm;
import gc.apiClient.repository.postgresql.Repository_UcrmRt;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicePostgre implements InterfaceDBPostgreSQL {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_Ucrm repositoryUcrm;
	private final Repository_CallbotRt repositoryCallbotRt;
	private final Repository_UcrmRt repositoryUcrmRt;
	private final Repository_ApimRt repositoryApimRt;
	private final Repository_ContactLt repositoryContactLt;
	private final CustomProperties customProperties;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa, Repository_ContactLt repositoryContactLt, CustomProperties customProperties,
			Repository_Ucrm repositoryUcrm, Repository_CallbotRt repositoryCallbotRt, Repository_UcrmRt repositoryUcrmRt, Repository_ApimRt repositoryApimRt) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryUcrm = repositoryUcrm;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryContactLt = repositoryContactLt;
		this.customProperties = customProperties;
		this.repositoryCallbotRt = repositoryCallbotRt;
		this.repositoryUcrmRt = repositoryUcrmRt;
		this.repositoryApimRt = repositoryApimRt;
	}


	@Override
	public Entity_CampRt createCampRtMsg(String cpid, Entity_CampMa enCampMa,int rlsq) {
		// contactid::contactListId::cpid::CPSQ::dirt::tkda::dateCreated

		log.info("====== Method : createCampRtMsg ======");

		log.info("들어온 rs : {}", cpid);
		Entity_CampRt enCampRt = new Entity_CampRt();
		CampRt id = new CampRt();
		String parts[] = cpid.split("::");

		int coid = 0;
		int cpsq = Integer.parseInt(parts[3]);
		long hubId = 0;
		int dirt = 0;
		int dict = 0;
		String campid = parts[2];
		String contactLtId = parts[1];
		String contactId = parts[0];
		String tkda = parts[5];
		Date didt = null;

		try {

			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 ------");
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
			log.info("------ 들어온 rs를 분배해여 필요한 변수들 초기화 끝 ------");

			if (tkda.charAt(0) == 'C') {
				hubId = Long.parseLong(tkda.split(",")[1]);
			} else if (tkda.charAt(0) == 'A') {
				cpsq = Integer.parseInt(tkda.split("\\|\\|")[5]);
			} else {
			}

			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
			log.info("didt(포맷 변경 전) : {}", parts[6]);
			Date parsedDate = inputFormat.parse(parts[6]);

			// Formatting the parsed date to the desired format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String formattedDateString = outputFormat.format(parsedDate);
			Date formattedDate = outputFormat.parse(formattedDateString);
			didt = formattedDate;
			log.info("didt(포맷 변경 후) : {}", didt);

			log.info("dirt(맵핑 전) : {}", parts[4]);
			Map<String, String> properties = customProperties.getProperties();
			dirt = Integer.parseInt(properties.getOrDefault(parts[4], "1").trim());
			log.info("dirt(맵핑 후) : {}", dirt);

			ServiceWebClient crmapi = new ServiceWebClient();
			String result = crmapi.getStatusApiReq("campaign_stats", campid);
			dict = ServiceJson.extractIntVal("ExtractDict", result);

			coid = enCampMa.getCoid();
			log.info("campid({})로 조회한 레코드의 coid : {}", campid, coid);

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

		} catch (Exception e) {
			log.error("Error Message : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enCampRt;
	}

	@Override
	public Entity_CampMa createEnCampMa(JSONObject jsonObject) { // 매개변수로 받는 String msg = > cpid::coid::cpna::division

		log.info("====== Method : createEnCampMa ======");

		Entity_CampMa enCampMa = new Entity_CampMa();
		String cpid = "";
		int coid = 0;
		String cpna = "";
		String contactListid = "";
		String contactListnm = "";
		String queueid = "";
		String divisionid = "";
		String divisionnm = "";
		String insdate = "";
		String moddate = "";

		try {
			
			coid = Integer.parseInt(jsonObject.optString("coid", "")); // 센터구분 코드
			
		} catch (Exception e) {
			log.info("잘못된 coid(센터구분 코드)입니다 coid(센터구분 코드)는 두 자리 숫자여야 합니다 : {}", jsonObject.getString("coid"));
			coid = 99;
			log.info("coid(센터구분 코드)임의로 숫자 '99'로 변경 : {}", coid);
		}
		
		cpid = jsonObject.optString("cpid", ""); //캠페인아이디
		cpna = jsonObject.optString("cpna", ""); //캠페인명
		contactListid = jsonObject.optString("contactListid", ""); //컨텍리스트아이디
		contactListnm = jsonObject.optString("contactListnm", ""); //컨텍리스트명
		queueid = jsonObject.optString("queueid", ""); //큐아이디
		divisionid = jsonObject.optString("divisionid", ""); //디비전아이디
		divisionnm = jsonObject.optString("divisionnm", ""); //디비전명
		insdate = jsonObject.optString("insdate", "");//최초생성일
		moddate = jsonObject.optString("moddate", ""); //마지막수정일

		enCampMa.setCpid(cpid);
		enCampMa.setCoid(coid);
		enCampMa.setCpna(cpna);
		enCampMa.setContactltid(contactListid);
		enCampMa.setContactltnm(contactListnm);
		enCampMa.setQueueid(queueid);
		enCampMa.setDivisionid(divisionid);
		enCampMa.setDivisionnm(divisionnm);
		enCampMa.setInsdate(insdate);
		enCampMa.setModdate(moddate);

		return enCampMa;
	}

	@Override
	public Entity_ContactLt createContactLtMsg(String msg) {// (콜봇에서 뽑아온거)cpid::cpsq::cske::csno::tkda::flag

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		String ContactLvalues[] = msg.split("::");

		try {
			id.setCpid(ContactLvalues[0]);
			id.setCpsq(Integer.parseInt(ContactLvalues[1]));
			enContactLt.setId(id);
			enContactLt.setCske(ContactLvalues[2]);// "customerkey"
			enContactLt.setFlag(ContactLvalues[5]);// "HO2"
			enContactLt.setTkda(ContactLvalues[4]);// "custid,111"

		} catch (Exception e) {
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}

	@Override
	public Entity_ContactLt createContactUcrm(Entity_Ucrm entityUcrm) {

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		try {
			id.setCpid(entityUcrm.getId().getCpid());
			id.setCpsq(Integer.parseInt(entityUcrm.getId().getCpsq()));
			enContactLt.setId(id);
			enContactLt.setCske(entityUcrm.getHldrCustId());
			enContactLt.setFlag(entityUcrm.getWorkDivsCd());
			enContactLt.setTkda(entityUcrm.getTrdtCntn());
			enContactLt.setTno1(entityUcrm.getTlno());

		} catch (Exception e) {
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}

	@Override
	public Entity_Ucrm createUcrm(String msg) throws Exception {

		Entity_Ucrm enUcrm = new Entity_Ucrm();
		Ucrm id = new Ucrm();
		JSONObject jsonObj = new JSONObject(msg);
		String payload = jsonObj.getString("payload");
		JSONObject payloadObject = new JSONObject(payload);

		String ctiCmpnId = payloadObject.optString("ctiCmpnId", "");
		String ctiCmpnSno = payloadObject.optString("ctiCmpnSno", "");
		String cablTlno = payloadObject.optString("cablTlno", "");
		String custNm = payloadObject.optString("custNm", "");
		String custTlno = payloadObject.optString("custTlno", "");

		id.setCpid(ctiCmpnId);
		id.setCpsq(ctiCmpnSno);
		enUcrm.setId(id);
		enUcrm.setCablTlno(cablTlno);
		enUcrm.setCustNm(custNm);
		enUcrm.setCustTlno(custTlno);
		enUcrm.setHldrCustId(payloadObject.optString("hldrCustId", ""));
		enUcrm.setSubssDataChgCd(payloadObject.optString("subssDataChgCd", ""));
		enUcrm.setSubssDataDelYn(payloadObject.optString("subssDataDelYn", ""));
		enUcrm.setTlno(payloadObject.optString("tlno", ""));
		enUcrm.setTopcDataIsueDtm(payloadObject.optString("topcDataIsueDtm", ""));
		enUcrm.setTopcDataIsueSno(payloadObject.optString("topcDataIsueSno", ""));
		enUcrm.setTrdtCntn(payloadObject.optString("trdtCntn", ""));
		enUcrm.setWorkDivsCd(payloadObject.optString("workDivsCd", ""));

		return enUcrm;
	}

	@Override
	public String createContactLtGC(String msg) {
		// 뽑아온다(콜봇).cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

		String values[] = msg.split("::");

		JSONObject data = new JSONObject();
		JSONObject mainObj = new JSONObject();
		try {
			data.put("CPID", values[0]);
			data.put("CPSQ", values[1]);
			data.put("CSKE", values[2]);
			data.put("CSNA", "");
			data.put("CBDN", "");
			data.put("TKDA", values[4]);
			data.put("TNO1", values[3]);
			data.put("TNO2", "");
			data.put("TNO3", "");
			data.put("TNO4", "");
			data.put("TNO5", "");
			data.put("TLNO", "");
			data.put("TMZO", "Asia/Seoul"); // <-- (+09:00) 삭제
			data.put("QUEUEID", values[7]);
			data.put("TRYCNT", "0");

			mainObj.put("data", data);
			mainObj.put("id", values[1]);
			mainObj.put("contactListId", values[6]);

		} catch (Exception e) {
			log.error("Error Message :{}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return mainObj.toString();
	}

	@Override
	@Transactional
	public Entity_CampRt insertCampRt(Entity_CampRt entity_CampRt) {

		Optional<Entity_CampRt> existingEntity = repositoryCampRt.findById(entity_CampRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryCampRt.save(entity_CampRt);

	}

	@Override
	@Transactional
	public Entity_CampMa insertCampMa(Entity_CampMa entityCampMa) {

		Optional<Entity_CampMa> existingEntity = repositoryCampMa.findByCpid(entityCampMa.getCpid()); // db에 인서트 하기 전. 키 값인 캠페인 아이디로 먼저 조회를 한다.

		if (existingEntity.isPresent()) {// 조회 해본 결과 레코드가 이미 있는 상황이라면 에러는 발생시킨다.
			throw new DataIntegrityViolationException("주어진 'cpid'를 가진 레코드가 테이블에 이미 존재합니다.");
		}

		return repositoryCampMa.save(entityCampMa);// 없으면 인서트
	}

	@Override
	@Transactional
	public Entity_Ucrm insertUcrm(Entity_Ucrm entityUcrm) {

		Optional<Entity_Ucrm> existingEntity = repositoryUcrm.findById(entityUcrm.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrm.save(entityUcrm);
	}

	@Override
	@Transactional
	public Entity_ContactLt insertContactLt(Entity_ContactLt entityContactLt) {

		Optional<Entity_ContactLt> existingEntity = repositoryContactLt.findById(entityContactLt.getId());

		if (existingEntity.isPresent()) {
		}
		return repositoryContactLt.save(entityContactLt);
	}

	@Override
	@Transactional
	public Entity_CampMa findCampMaByCpid(String cpid) {

		try {
			Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampMa by cpid: {}", cpid);
			errorLogger.error("Error retrieving Entity_CampMa by cpid: {}", cpid, ex);
			return null;
		}
	}
	
	
	@Override
	@Transactional
	public List<Entity_CampMa> getAllRecords() throws Exception {
		 return repositoryCampMa.findAll();
	}
	

	@Override
	@Transactional
	public Integer findCampRtMaxRlsq() {

		try {
			Optional<Integer> optionalEntity = repositoryCampRt.findMaxRlsq();
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_CampRt which has hightest value of 'rlsq' column: {}", ex.getMessage());
			errorLogger.error("Error retrieving Entity_CampRt which has hightest value of 'rlsq' column: {}", ex.getMessage(), ex);
			return null;
		}
	}

	@Override
	public int getRecordCount() {
		log.info("Campma 테이블 레코드 수 : {}", repositoryCampMa.countBy());
		return repositoryCampMa.countBy();
	}

	@Override
	@Transactional
	public Page<Entity_Ucrm> getAll() throws Exception {
		return repositoryUcrm.findAllWithLock(PageRequest.of(0, 750));
	}

	@Override
	public Page<Entity_UcrmRt> getAllUcrmRt() throws Exception {
		return repositoryUcrmRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	public Page<Entity_CallbotRt> getAllCallBotRt() throws Exception {
		return repositoryCallbotRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	public Page<Entity_ApimRt> getAllApimRt() throws Exception {
		return repositoryApimRt.findAll(PageRequest.of(0, 1000));
	}

	@Override
	@Transactional
	public void delCampMaById(String cpid) throws Exception {

		Optional<Entity_CampMa> entityOpt = repositoryCampMa.findByCpid(cpid);
		if (entityOpt.isPresent()) {
			repositoryCampMa.deleteById(cpid);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + cpid);
		}
	}

	@Override
	@Transactional
	public void delCallBotRtById(CallBotCampRt id) throws Exception {

		Optional<Entity_CallbotRt> entityOpt = repositoryCallbotRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryCallbotRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delUcrmRtById(UcrmCampRt id) throws Exception {

		Optional<Entity_UcrmRt> entityOpt = repositoryUcrmRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryUcrmRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delApimRtById(ApimCampRt id) throws Exception {

		Optional<Entity_ApimRt> entityOpt = repositoryApimRt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryApimRt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void delUcrmLtById(String topcDataIsueSno) throws Exception {
		Optional<Entity_Ucrm> entityOptional = repositoryUcrm.lockByTopcDataIsueSno(topcDataIsueSno);
		if (entityOptional.isPresent()) {
			repositoryUcrm.deleteByTopcDataIsueSno(topcDataIsueSno);
		} else {
			throw new Exception("해당 topcDataIsueSno 값을 가진 엔티티가 DB테이블에서 조회되지 않습니다. : " + topcDataIsueSno);
		}
	}

	@Override
	@Transactional
	public void delContactltById(ContactLtId id) throws Exception {

		Optional<Entity_ContactLt> entityOpt = repositoryContactLt.findById(id);
		if (entityOpt.isPresent()) {
			repositoryContactLt.deleteById(id);
		} else {
			throw new Exception("삭제하려는 id를 가진 엔티티가 DB테이블에서 조회되지 않습니다.: " + id);
		}
	}

	@Override
	@Transactional
	public void updateCampMa(String cpid, String cpna) throws Exception {
		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findById(cpid);// 캠페인 아이디로 레코드 조회.

		if (optionalEntity.isPresent()) {// 조회 후 있다면 해당 레코드의 캠페인명 업데이트
			Entity_CampMa entity = optionalEntity.get();
			entity.setCpna(cpna);
			repositoryCampMa.save(entity);
		} else {
			throw new EntityNotFoundException("해당 cpid (" + cpid + ")로 조회 된 레코드가 DB에 없습니다.");
		}
	}

	@Override
	@Transactional
	public Entity_CallbotRt insertCallbotRt(Entity_CallbotRt enCallbotRt) throws Exception {

		Optional<Entity_CallbotRt> existingEntity = repositoryCallbotRt.findById(enCallbotRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryCallbotRt.save(enCallbotRt);
	}

	@Override
	@Transactional
	public Entity_UcrmRt insertUcrmRt(Entity_UcrmRt enUcrmRt) throws Exception {
		Optional<Entity_UcrmRt> existingEntity = repositoryUcrmRt.findById(enUcrmRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryUcrmRt.save(enUcrmRt);
	}

	@Override
	@Transactional
	public Entity_ApimRt insertApimRt(Entity_ApimRt enApimRt) throws Exception {
		Optional<Entity_ApimRt> existingEntity = repositoryApimRt.findById(enApimRt.getId());

		if (existingEntity.isPresent()) {
			throw new DataIntegrityViolationException("주어진 복합키를 가진 레코드가 이미 테이블에 존재합니다.");
		}

		return repositoryApimRt.save(enApimRt);
	}

	@Override
	public Entity_UcrmRt createUcrmRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_UcrmRt enUcrmRt = new Entity_UcrmRt();
		UcrmCampRt ucrmCampRt = new UcrmCampRt();
		ucrmCampRt.setCpid(cpid);
		ucrmCampRt.setCpsq(cpsq);
		enUcrmRt.setId(ucrmCampRt);
		enUcrmRt.setDivisionid(divisionid);

		return enUcrmRt;
	}

	@Override
	public Entity_CallbotRt createCallbotRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_CallbotRt enCallbotRt = new Entity_CallbotRt();
		CallBotCampRt callbotCampRt = new CallBotCampRt();
		callbotCampRt.setCpid(cpid);
		callbotCampRt.setCpsq(cpsq);
		enCallbotRt.setId(callbotCampRt);
		enCallbotRt.setDivisionid(divisionid);

		return enCallbotRt;
	}

	@Override
	public Entity_ApimRt createApimRt(String msg) throws Exception {

		String cpid = msg.split("::")[0];
		String cpsq = msg.split("::")[1];
		String divisionid = msg.split("::")[2];

		Entity_ApimRt apimRt = new Entity_ApimRt();
		ApimCampRt apimCampRt = new ApimCampRt();
		apimCampRt.setCpid(cpid);
		apimCampRt.setCpsq(cpsq);
		apimRt.setId(apimCampRt);
		apimRt.setDivisionid(divisionid);

		return apimRt;
	}

	@Override
	public Entity_ContactLt createContactUcrm(JSONObject jsonObject) throws Exception {
		Entity_ContactLt enContactLt = new Entity_ContactLt();
		ContactLtId id = new ContactLtId();
		try {
			JSONObject dataObject = jsonObject.getJSONObject("data");
			String cpid = dataObject.getString("CPID");
			String cpsq = dataObject.getString("CPSQ");
			String cske = dataObject.getString("CSKE");
			String tkda = dataObject.getString("TKDA");
			String tno1 = dataObject.getString("TNO1");

			id.setCpid(cpid);
			id.setCpsq(Integer.parseInt(cpsq));
			enContactLt.setId(id);
			enContactLt.setCske(cske);
			enContactLt.setFlag("A");
			enContactLt.setTkda(tkda);
			enContactLt.setTno1(tno1);

		} catch (Exception e) {
			log.error("Error Messge : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return enContactLt;
	}

	@Override
	@Transactional
	public void delUcrmltRecord(String cpid, String cpsq) throws Exception {
		Optional<Entity_Ucrm> entityOptional = repositoryUcrm.lockByCpidAndCpsq(cpid, cpsq);
		if (entityOptional.isPresent()) {
			repositoryUcrm.deleteByCpidAndCpsq(cpid, cpsq);
		} else {
		}

	}

	@Override
	@Transactional
	public void delContactltRecord(String cpid, String cpsq) throws Exception {
		Optional<Entity_ContactLt> entityOptional = repositoryContactLt.lockByCpidAndCpsq(cpid, cpsq);
		if (entityOptional.isPresent()) {
			repositoryContactLt.deleteByCpidAndCpsq(cpid, cpsq);
		} else {
		}

	}

	@Override
	public List<Entity_ContactLt> getRecordsByCpid(String cpid) throws Exception {
		return repositoryContactLt.findByCpId(cpid);
	}

}
