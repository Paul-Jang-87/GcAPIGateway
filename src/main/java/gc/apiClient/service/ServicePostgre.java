package gc.apiClient.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.datamapping.MappingHomeCenter;
import gc.apiClient.embeddable.ContactLtId;
import gc.apiClient.entity.Entity_AppConfig;
import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_ContactltMapper;
import gc.apiClient.entity.Entity_MapCoid;
import gc.apiClient.interfaceCollection.InterfaceDB;
import gc.apiClient.repository.Repository_AppConfig;
import gc.apiClient.repository.Repository_CampMa;
import gc.apiClient.repository.Repository_CampRt;
import gc.apiClient.repository.Repository_ContactLt;
import gc.apiClient.repository.Repository_MapCoId;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicePostgre extends ServiceJson implements InterfaceDB {
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_AppConfig repositoryAppConfig;
	private final Repository_ContactLt repositoryContactLt;
	private final Repository_MapCoId repositoryMapCoId;
	private final CustomProperties customProperties;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa,
			Repository_AppConfig repositoryAppConfig, Repository_MapCoId repositoryMapCoId,
			Repository_ContactLt repositoryContactLt, CustomProperties customProperties) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryAppConfig = repositoryAppConfig;
		this.repositoryContactLt = repositoryContactLt;
		this.repositoryMapCoId = repositoryMapCoId;
		this.customProperties = customProperties;
	}

	// **Create
	@Override
	public Entity_MapCoid createMapCoIdMsg() {
		Entity_MapCoid enCampMa = new Entity_MapCoid();
		enCampMa.setCoid(22);
		enCampMa.setCpid("cpid_3");

		return enCampMa;
	}

	@Override
	public Entity_CampRt createCampRtMsg(String cpid) {// contactid(고객키)|contactListId|didt|dirt|cpid

		log.info("===== createCampRtMsg =====");

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("::");

		int rlsq = 0;
		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		Date didt = null;
		int dirt = 0;
		int dict = 0;
		int coid = 0;

		log.info("rlsq: {}", rlsq);
		log.info("campid: {}", campid);
		log.info("cpsq: {}", cpsq);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("hubid: {}", hubId);
		log.info("didt: {}", didt);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);
		log.info("coid: {}", coid);

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByCske(contactId);

		String tokendata = enContactLt.getTkda();

		if (tokendata.charAt(0) == 'C') {
			hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);
		} else {
			cpsq = Integer.parseInt(enContactLt.getTkda().split("\\|\\|")[5]);
		}

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			Date parsedDate = inputFormat.parse(parts[2]);

			// Formatting the parsed date to the desired format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String formattedDateString = outputFormat.format(parsedDate);
			Date formattedDate = outputFormat.parse(formattedDateString);
			didt = formattedDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[3], "6"));

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = enCampMa.getCoid();

		enCampRt.setRlsq(rlsq);
		enCampRt.setCpid(campid);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		log.info("rlsq: {}", rlsq);
		log.info("campid: {}", campid);
		log.info("cpsq: {}", cpsq);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("hubid: {}", hubId);
		log.info("didt: {}", didt);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);
		log.info("coid: {}", coid);

		return enCampRt;
	}

	@Override
	public Entity_CampRtJson createCampRtJson(String cpid) {// contactid(고객키)|contactListId|didt|dirt|cpid

		log.info("===== createCampRtJson =====");

		Entity_CampRtJson enCampRt = new Entity_CampRtJson();

		String parts[] = cpid.split("::");

		int rlsq = 0;
		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		String didt = "";
		int dirt = 0;
		int dict = 0;
		int coid = 0;

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByCske(contactId);

		String tokendata = enContactLt.getTkda();

		if (tokendata.charAt(0) == 'C') {
			hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);
		} else {
			cpsq = Integer.parseInt(enContactLt.getTkda().split("\\|\\|")[5]);
		}

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			Date parsedDate = inputFormat.parse(parts[2]);

			// Formatting the parsed date to the desired format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String formattedDateString = outputFormat.format(parsedDate);
			didt = formattedDateString;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[3], "6"));

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = enCampMa.getCoid();
		MappingHomeCenter mappingData = new MappingHomeCenter();
		coid = Integer.parseInt(mappingData.getCentercodeById(Integer.toString(coid))); 

		enCampRt.setRlsq(rlsq);
		enCampRt.setCpid(campid);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		return enCampRt;
	}

	@Override
	public Entity_CampRt createCampRtMsgCallbot(String cpid) {// contactid(고객키)::contactListId::didt::dirt::cpid

		log.info("===== createCampRtMsgCallbot =====");

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("::");

		int rlsq = 0;
		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		Date didt = null;
		int dirt = 0;
		int dict = 0;

		log.info("rlsq: {}", rlsq);
		log.info("campid: {}", campid);
		log.info("cpsq: {}", cpsq);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("hubid: {}", hubId);
		log.info("didt: {}", didt);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByCske(contactId);

		String tokendata = enContactLt.getTkda();
		if (tokendata.charAt(0) == 'C') {
			hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);
		} else {
			cpsq = Integer.parseInt(enContactLt.getTkda().split("\\|\\|")[5]);
		}

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			Date parsedDate = inputFormat.parse(parts[2]);

			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String formattedDateString = outputFormat.format(parsedDate);
			Date formattedDate = outputFormat.parse(formattedDateString);
			didt = formattedDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[3], "6"));

		ServiceWebClient callbotapi = new ServiceWebClient();
		String result = callbotapi.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		enCampRt.setCpid(campid);
		enCampRt.setRlsq(rlsq);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);

		log.info("rlsq: {}", rlsq);
		log.info("campid: {}", campid);
		log.info("cpsq: {}", cpsq);
		log.info("contactLtId: {}", contactLtId);
		log.info("contactId: {}", contactId);
		log.info("hubid: {}", hubId);
		log.info("didt: {}", didt);
		log.info("dirt: {}", dirt);
		log.info("dict: {}", dict);

		return enCampRt;
	}

	@Override
	public Entity_CampRtJson createCampRtJsonCallbot(String cpid) { // contactid(고객키)::contactListId::didt::dirt::cpid

		Entity_CampRtJson enCampRt = new Entity_CampRtJson();

		String parts[] = cpid.split("::");

		int rlsq = 0;
		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		String didt = "";
		int dirt = 0;
		int dict = 0;
		int coid = 0;

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByCske(contactId);

		String tokendata = enContactLt.getTkda();

		if (tokendata.charAt(0) == 'C') {
			hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);
		} else {
			cpsq = Integer.parseInt(enContactLt.getTkda().split("\\|\\|")[5]);
		}

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		try {
			Date parsedDate = inputFormat.parse(parts[2]);

			// Formatting the parsed date to the desired format
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String formattedDateString = outputFormat.format(parsedDate);
			didt = formattedDateString;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[3], "6"));

		ServiceWebClient callbotapi = new ServiceWebClient();
		String result = callbotapi.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		enCampRt.setCpid(campid);
		enCampRt.setRlsq(rlsq);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		return enCampRt;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String cpid) {

		System.out.println("Class : ServicePostgre\n Method : createCampMaMsg");

		Entity_CampMa enCampMa = new Entity_CampMa();
		Entity_MapCoid enMapcoid = new Entity_MapCoid();

		enMapcoid = findMapCoidByCpid(cpid);
		int coid = enMapcoid.getCoid();

		enCampMa.setCoid(coid);
		enCampMa.setCpid(cpid);
		enCampMa.setCpna("cpna_6");

		System.out.println("===== END =====");

		return enCampMa;
	}

	@Override
	public Entity_ContactLt createContactLtMsg(String msg) {

		log.info("===== createContactLtMsg ===== ");

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		String ContactLvalues[] = msg.split("::");

		log.info("msg : {}", msg);
		// 임시로 데이터 적재
		enContactLt.setCpid(ContactLvalues[0]);
		enContactLt.setCpsq(Integer.parseInt(ContactLvalues[1]));
		enContactLt.setCske(ContactLvalues[2]);// "customerkey"
		enContactLt.setCsna(ContactLvalues[3]);// "카리나"
		enContactLt.setFlag(ContactLvalues[4]);// "HO2"
		enContactLt.setTkda(ContactLvalues[5]);// "custid,111"
		enContactLt.setTn01(ContactLvalues[6]);// "tn01"
		enContactLt.setTn02(ContactLvalues[7]);// "tn02"
		enContactLt.setTn03(ContactLvalues[8]);// "tn03"

		log.info("cpid : {}", ContactLvalues[0]);
		log.info("cpsq : {}", Integer.parseInt(ContactLvalues[1]));
		log.info("cske : {}", ContactLvalues[2]);
		log.info("csna : {}", ContactLvalues[3]);
		log.info("flag : {}", ContactLvalues[4]);
		log.info("tkda : {}", ContactLvalues[5]);
		log.info("tno1 : {}", ContactLvalues[6]);
		log.info("tno2 : {}", ContactLvalues[7]);
		log.info("tno3 : {}", ContactLvalues[8]);

		return enContactLt;
	}

	@Override
	public Entity_ContactltMapper createContactLtGC(String msg) {// cpid|cpsq|cske|csna|flag|tkda|tno1|tno2|tno3

		log.info("===== createContactLtGC =====");

		Entity_ContactltMapper contactltMapper = new Entity_ContactltMapper();
		String values[] = msg.split("::");

		log.info("msg : {}", msg);

		// 임시로 데이터 적재
		contactltMapper.setCpsq(values[1]); // CPSQ
		contactltMapper.setCske(values[2]); // CSKE
		contactltMapper.setCsna(values[3]); // CSNA
		contactltMapper.setTkda(values[5]); // TKDA
		contactltMapper.setCpid(values[0]); // CPID
		contactltMapper.setTno1(values[6]); // tno1
		contactltMapper.setTno2(values[7]); // tno2
		contactltMapper.setTno3(values[8]); // tno3
		contactltMapper.setTmzo("Asia/Seoul (+09:00)"); // tmzo

		log.info("cpsq :{}", values[1]);
		log.info("cske :{}", values[2]);
		log.info("csna :{}", values[3]);
		log.info("tkda :{}", values[5]);
		log.info("cpid :{}", values[0]);
		log.info("tno1 :{}", values[6]);
		log.info("tno2 :{}", values[7]);
		log.info("tno3 :{}", values[8]);
		log.info("tmzo :{}", "Asia/Seoul (+09:00)");

		return contactltMapper;
	}

	@Override
	public Entity_ContactltMapper createContactLCallbottGC(String msg) {// cpid|cpsq|cske|tno1|tkda|flag

		log.info("===== createContactLCallbottGC =====");

		Entity_ContactltMapper contactltMapper = new Entity_ContactltMapper();
		String values[] = msg.split("::");

		log.info("msg : {}", msg);

		// 임시로 데이터 적재
		contactltMapper.setCpsq(values[1]); // CPSQ
		contactltMapper.setCske(values[2]); // CSKE
		contactltMapper.setCsna(""); // CSNA
		contactltMapper.setTkda(values[4]); // TKDA
		contactltMapper.setCpid(values[0]); // CPID
		contactltMapper.setTno1(values[3]); // tno1
		contactltMapper.setTno2(""); // tno2
		contactltMapper.setTno3(""); // tno3
		contactltMapper.setTmzo("Asia/Seoul (+09:00)"); // tmzo

		log.info("cpsq : {}", values[1]);
		log.info("cske : {}", values[2]);
		log.info("tkda : {}", values[4]);
		log.info("cpid : {}", values[0]);
		log.info("tno1 : {}", values[3]);

		return contactltMapper;
	}

	@Override
	public Entity_ContactLt createContactLtMsgCallbot(String msg) {// cpid::cpsq::cske::tno1::tkda::flag

		log.info("===== createContactLtMsgCallbot =====");

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		String ContactLvalues[] = msg.split("::");

		// 임시로 데이터 적재
		enContactLt.setCpid(ContactLvalues[0]);
		enContactLt.setCpsq(Integer.parseInt(ContactLvalues[1]));
		enContactLt.setCske(ContactLvalues[2]);// "customerkey"
		enContactLt.setTn01(ContactLvalues[3]);// "tn01"
		enContactLt.setTkda(ContactLvalues[4]);// "custid,111"
		enContactLt.setFlag(ContactLvalues[5]);// "HO2"
		enContactLt.setCsna("");// "카리나"
		enContactLt.setTn02("");// "tn02"
		enContactLt.setTn03("");// "tn03"

		log.info("cpid : {}", ContactLvalues[0]);
		log.info("cpsq : {}", ContactLvalues[1]);
		log.info("cske : {}", ContactLvalues[2]);
		log.info("tno1 : {}", ContactLvalues[3]);
		log.info("tkda : {}", ContactLvalues[4]);
		log.info("flag : {}", ContactLvalues[5]);

		return enContactLt;
	}

	@Override
	public Entity_AppConfig createAppConfigMsg(String temp) {

		Entity_AppConfig enAppConfig = new Entity_AppConfig();
		String parts[] = temp.split("::");

		enAppConfig.setApimClId("");
		enAppConfig.setApimClSecret("");
		enAppConfig.setGcClientId(parts[0]);
		enAppConfig.setGcClientSecret(parts[1]);
		enAppConfig.setSaslId("");
		enAppConfig.setSaslPwd("");

		return enAppConfig;
	}

	// **Insert
	@Override
	public Entity_CampRt InsertCampRt(Entity_CampRt entity_CampRt) {

		try {
			return repositoryCampRt.save(entity_CampRt);
		} catch (DataIntegrityViolationException ex) {
			log.error("Data integrity violation while inserting entity_CampRt: {}", entity_CampRt, ex);
		} catch (DataAccessException ex) {
			log.error("Data access error while inserting entity_CampRt: {}", entity_CampRt, ex);
		}

		return null;
	}

	@Override
	public Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa) {
		
		try {
			return repositoryCampMa.save(entityCampMa);
		} catch (DataIntegrityViolationException ex) {
			log.error("Data integrity violation while inserting entityCampMa: {}", entityCampMa, ex);
		} catch (DataAccessException ex) {
			log.error("Data access error while inserting entityCampMa: {}", entityCampMa, ex);
		}

		return null;
		
	}

	@Override
	public Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt) {
		
		Optional<Entity_ContactLt> existingEntity = repositoryContactLt.findByCske(entityContactLt.getCske());

        if (existingEntity.isPresent()) {
            throw new DataIntegrityViolationException("Record with 'cpid' already exists.");
        }

        return repositoryContactLt.save(entityContactLt);
	}

	@Override
	public Entity_AppConfig InsertAppConfig(Entity_AppConfig entityAppConfig) {
		return repositoryAppConfig.save(entityAppConfig);
	}

	@Override
	public Entity_MapCoid InsertMapCoId(Entity_MapCoid entityMapCoid) {
		
		try {
			return repositoryMapCoId.save(entityMapCoid);
		} catch (DataIntegrityViolationException ex) {
			log.error("Data integrity violation while inserting entityMapCoid: {}", entityMapCoid, ex);
		} catch (DataAccessException ex) {
			log.error("Data access error while inserting entityMapCoid: {}", entityMapCoid, ex);
		}
		return null;
	}

	@Override
	public Entity_MapCoid findMapCoidByCpid(String cpid) {

		try {
			Optional<Entity_MapCoid> optionalEntity = repositoryMapCoId.findByCpid(cpid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_MapCoid by cpid: {}", cpid, ex);

			return null;
		}
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
	public List<Entity_ContactLt> findContactLtByCpid(String id) {

		List<Entity_ContactLt> resultList = repositoryContactLt.findByCpid(id);
		if (!resultList.isEmpty()) {
			return resultList;
		} else {
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

			// 예외처리 커스터마이징 부분.
//	        String tableName = "your_table_name";
//	        String fieldName = "your_field_name";
//
//	        throw new MyCustomDataIntegrityException("Data integrity violation while inserting contact", ex, tableName, fieldName);
			return null;
		}
	}

	@Override
	public Entity_AppConfig findAppConfigByid(Long id) {
		Optional<Entity_AppConfig> optionalEntity = repositoryAppConfig.findByid(id);
		return optionalEntity.orElse(null);
	}

	@Override
	public Entity_AppConfig getEntityById(Long id) {
		Optional<Entity_AppConfig> optionalEntity = repositoryAppConfig.findByid(id);
		return optionalEntity.orElse(null);
	}

	// 예외처리 커스터 마이징 부분.
	@SuppressWarnings("serial")
	public class MyCustomDataIntegrityException extends RuntimeException {
		
		public MyCustomDataIntegrityException(String message, Throwable cause) {
	        super(message, cause);
	    }
		
		
		

//	    private String tableName;
//	    private String fieldName;
//
//	    public MyCustomDataIntegrityException(String message, Throwable cause, String tableName, String fieldName) {
//	        super(message, cause);
//	        this.tableName = tableName;
//	        this.fieldName = fieldName;
//	    }
//
//	    public String getTableName() {
//	        return tableName;
//	    }
//
//	    public String getFieldName() {
//	        return fieldName;
//	    }
	}
	
	@SuppressWarnings("serial")
	public class MyCustomDataAccessException extends RuntimeException {
	    public MyCustomDataAccessException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}


}
