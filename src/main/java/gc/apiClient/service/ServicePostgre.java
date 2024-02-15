package gc.apiClient.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.stereotype.Service;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.datamapping.MappingHomeCenter;
import gc.apiClient.entity.Entity_AppConfig;
import gc.apiClient.entity.Entity_CampMa;
import gc.apiClient.entity.Entity_CampRt;
import gc.apiClient.entity.Entity_CampRtJson;
import gc.apiClient.entity.Entity_ContactLt;
import gc.apiClient.entity.Entity_MapCoid;
import gc.apiClient.interfaceCollection.InterfaceDB;
import gc.apiClient.repository.Repository_AppConfig;
import gc.apiClient.repository.Repository_CampMa;
import gc.apiClient.repository.Repository_CampRt;
import gc.apiClient.repository.Repository_ContactLt;
import gc.apiClient.repository.Repository_MapCoId;

@Service
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
		enCampMa.setCoid("coid_3");
		enCampMa.setCpid("cpid_3");

		return enCampMa;
	}

	@Override
	public Entity_CampRt createCampRtMsg(String cpid) {// contactid(고객키)|contactListId|didt|dirt|cpid

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("\\|");

		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		Date didt = null;
		int dirt = 0;
		int dict = 0;
		String coid = "";

		System.out.println("=====createCampRtMsg=====");
		System.out.println("campid: " + campid);
		System.out.println("cpsq: " + cpsq);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);

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

		enCampRt.setCpid(campid);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		System.out.println("campid: " + campid);
		System.out.println("cpsq: " + cpsq);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);
		System.out.println("===== End =====");

		return enCampRt;
	}

	@Override
	public Entity_CampRtJson createCampRtJson(String cpid) {// contactid(고객키)|contactListId|didt|dirt|cpid

		Entity_CampRtJson enCampRt = new Entity_CampRtJson();

		String parts[] = cpid.split("\\|");

		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		String didt = "";
		int dirt = 0;
		int dict = 0;
		String coid = "";
		

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
			System.out.println("포맷 변경 (String) : " + formattedDateString);
			didt = formattedDateString;

			System.out.println("Formatted Date: " + didt);
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
		coid = mappingData.getCentercodeById(coid);

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
	public Entity_CampRt createCampRtMsgCallbot(String cpid) {// contactid(고객키)|contactListId|didt|dirt|cpid

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("\\|");

		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		Date didt = null;
		int dirt = 0;
		int dict = 0;

		System.out.println("=====createCampRtMsgCallbot=====");
		System.out.println("campid: " + campid);
		System.out.println("cpsq: " + cpsq);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);

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

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		enCampRt.setCpid(campid);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);

		System.out.println("campid: " + campid);
		System.out.println("cpsq: " + cpsq);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("===== createCampRtMsgCallbot End =====");

		return enCampRt;
	}

	@Override
	public Entity_CampRtJson createCampRtJsonCallbot(String cpid) { // contactid(고객키)|contactListId|didt|dirt|cpid
		
		System.out.println("===== createCampRtJsonCallbot =====");

		Entity_CampRtJson enCampRt = new Entity_CampRtJson();

		String parts[] = cpid.split("\\|");

		String campid = parts[4];
		int cpsq = 0;
		String contactLtId = parts[1];
		String contactId = parts[0];
		int hubId = 0;
		String didt = "";
		int dirt = 0;
		int dict = 0;
		String coid = "";

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
			System.out.println("포맷 변경 (String) : " + formattedDateString);
			didt = formattedDateString;

			System.out.println("Formatted Date: " + didt);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> properties = customProperties.getProperties();
		dirt = Integer.parseInt(properties.getOrDefault(parts[3], "6"));

		ServiceWebClient crmapi1 = new ServiceWebClient();
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		enCampRt.setCpid(campid);
		enCampRt.setCpsq(cpsq);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		System.out.println("===== createCampRtJsonCallbot End =====");
		return enCampRt;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String cpid) {

		System.out.println("Class : ServicePostgre\n Method : createCampMaMsg");

		Entity_CampMa enCampMa = new Entity_CampMa();
		Entity_MapCoid enMapcoid = new Entity_MapCoid();

		enMapcoid = findMapCoidByCpid(cpid);
		String coid = enMapcoid.getCoid();

		enCampMa.setCoid(coid);
		enCampMa.setCpid(cpid);
		enCampMa.setCpna("cpna_6");

		System.out.println("===== END =====");

		return enCampMa;
	}

	@Override
	public Entity_ContactLt createContactLtMsg(String msg) {

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		String ContactLvalues[] = msg.split("\\|");

		System.out.println(msg);
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

		return enContactLt;
	}

	@Override
	public Entity_ContactLt createContactLtMsgCallbot(String msg) {

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		String ContactLvalues[] = msg.split("\\|");

		System.out.println(msg);
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

		return enContactLt;
	}

	@Override
	public Entity_AppConfig createAppConfigMsg(String temp) {

		Entity_AppConfig enAppConfig = new Entity_AppConfig();
		String parts[] = temp.split("\\|");

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
		return repositoryCampRt.save(entity_CampRt);
	}

	@Override
	public Entity_CampMa InsertCampMa(Entity_CampMa entityCampMa) {
		return repositoryCampMa.save(entityCampMa);
	}

	@Override
	public Entity_ContactLt InsertContactLt(Entity_ContactLt entityContactLt) {
		return repositoryContactLt.save(entityContactLt);
	}

	@Override
	public Entity_AppConfig InsertAppConfig(Entity_AppConfig entityAppConfig) {
		return repositoryAppConfig.save(entityAppConfig);
	}

	@Override
	public Entity_MapCoid InsertMapCoId(Entity_MapCoid entityMapCoid) {
		return repositoryMapCoId.save(entityMapCoid);
	}

	@Override
	public Entity_MapCoid findMapCoidByCpid(String cpid) {

		Optional<Entity_MapCoid> optionalEntity = repositoryMapCoId.findByCpid(cpid);
		return optionalEntity.orElse(null);
	}

	@Override
	public Entity_CampMa findCampMaByCpid(String cpid) {

		Optional<Entity_CampMa> optionalEntity = repositoryCampMa.findByCpid(cpid);
		return optionalEntity.orElse(null);
	}

	@Override
	public Entity_CampRt findCampRtByCpid(String cpid) {

		Optional<Entity_CampRt> optionalEntity = repositoryCampRt.findByCpid(cpid);
		return optionalEntity.orElse(null);
	}

	@Override
	public List<Entity_ContactLt> findContactLtByCpid(String cpid) {
		
		List<Entity_ContactLt> resultList = repositoryContactLt.findByCpid(cpid);

		if (!resultList.isEmpty()) {
			return resultList;
		} else {
			return null; 
		}
	}
	
	@Override
	public Entity_ContactLt findContactLtByCske(String cske) {
		
		Optional<Entity_ContactLt> optionalEntity = repositoryContactLt.findByCske(cske);
		return optionalEntity.orElse(null);
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

}
