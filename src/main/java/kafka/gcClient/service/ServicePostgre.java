package kafka.gcClient.service;

import java.sql.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kafka.gcClient.datamapping.MappingHomeCenter;
import kafka.gcClient.entity.Entity_AppConfig;
import kafka.gcClient.entity.Entity_CampMa;
import kafka.gcClient.entity.Entity_CampRt;
import kafka.gcClient.entity.Entity_ContactLt;
import kafka.gcClient.entity.Entity_MapCoid;
import kafka.gcClient.interfaceCollection.InterfaceDB;
import kafka.gcClient.repository.Repository_AppConfig;
import kafka.gcClient.repository.Repository_CampMa;
import kafka.gcClient.repository.Repository_CampRt;
import kafka.gcClient.repository.Repository_ContactLt;
import kafka.gcClient.repository.Repository_MapCoId;
import reactor.core.publisher.Mono;

@Service
public class ServicePostgre extends ServiceJson implements InterfaceDB {
	// 검색 **Create **Insert **Select
	private final Repository_CampRt repositoryCampRt;
	private final Repository_CampMa repositoryCampMa;
	private final Repository_AppConfig repositoryAppConfig;
	private final Repository_ContactLt repositoryContactLt;
	private final Repository_MapCoId repositoryMapCoId;

	public ServicePostgre(Repository_CampRt repositoryCampRt, Repository_CampMa repositoryCampMa,
			Repository_AppConfig repositoryAppConfig, Repository_MapCoId repositoryMapCoId,
			Repository_ContactLt repositoryContactLt) {

		this.repositoryCampRt = repositoryCampRt;
		this.repositoryCampMa = repositoryCampMa;
		this.repositoryAppConfig = repositoryAppConfig;
		this.repositoryContactLt = repositoryContactLt;
		this.repositoryMapCoId = repositoryMapCoId;
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
	public Entity_CampRt createCampRtMsg(String cpid) {

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("\\|");

		String campid = parts[0];
		String contactLtId = parts[1];
		String contactId = "";
		int hubId = 0;
		Date didt = Date.valueOf("2022-02-01");
		int dirt = 880382;
		int dict = 100;
		String coid = "";

		System.out.println("campid: " + campid);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByid(campid);

		contactId = enContactLt.getCske();
		hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);

		// api 호출 후. didt,dirt 가져오는 부분.
//		CrmSv01 crmapi1 = new CrmSv01(this);
//		String result = crmapi1.GetApiRequet("campaignId");
//		String cpid_3 = ExtractCpidfromThird(result);

		CrmSv01 crmapi1 = new CrmSv01(this);
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = enCampMa.getCoid();

		enCampRt.setCpid(campid);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		System.out.println("campid: " + campid);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);

		return enCampRt;
	}

	@Override
	public Entity_CampRt createCampRtToJson(String cpid) {

		Entity_CampRt enCampRt = new Entity_CampRt();

		String parts[] = cpid.split("\\|");

		String campid = parts[0];
		String contactLtId = parts[1];
		String contactId = "";
		int hubId = 0;
		Date didt = Date.valueOf("2022-02-01");
		int dirt = 880382;
		int dict = 100;
		String coid = "";

		System.out.println("campid: " + campid);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);

		Entity_ContactLt enContactLt = new Entity_ContactLt();
		enContactLt = findContactLtByid(campid);

		contactId = enContactLt.getCske();
		hubId = Integer.parseInt(enContactLt.getTkda().split(",")[1]);

		// api 호출 후. didt,dirt 가져오는 부분.
//		CrmSv01 crmapi1 = new CrmSv01(this);
//		String result = crmapi1.GetApiRequet("campaignId");
//		String cpid_3 = ExtractCpidfromThird(result);

		CrmSv01 crmapi1 = new CrmSv01(this);
		String result = crmapi1.GetStatusApiRequet("campaign_stats", campid);
		dict = ExtractDict(result);

		Entity_CampMa enCampMa = new Entity_CampMa();

		enCampMa = findCampMaByCpid(campid);
		coid = enCampMa.getCoid();
		MappingHomeCenter mappingData = new MappingHomeCenter();
		coid = mappingData.getCentercodeById(coid);

		enCampRt.setCpid(campid);
		enCampRt.setContactLtId(contactLtId);
		enCampRt.setContactId(contactId);
		enCampRt.setHubId(hubId);
		enCampRt.setDidt(didt);
		enCampRt.setDirt(dirt);
		enCampRt.setDict(dict);
		enCampRt.setCoid(coid);

		System.out.println("campid: " + campid);
		System.out.println("contactLtId: " + contactLtId);
		System.out.println("contactId: " + contactId);
		System.out.println("hubid: " + hubId);
		System.out.println("didt: " + didt);
		System.out.println("dirt: " + dirt);
		System.out.println("dict: " + dict);
		System.out.println("coid: " + coid);

		return enCampRt;
	}

	@Override
	public Entity_CampMa createCampMaMsg(String cpid) {

		Entity_CampMa enCampMa = new Entity_CampMa();
		Entity_MapCoid enMapcoid = new Entity_MapCoid();

		enMapcoid = findMapCoidByCpid(cpid);
		String coid = enMapcoid.getCoid();

		enCampMa.setCoid(coid);
		enCampMa.setCpid(cpid);
		enCampMa.setCpna("cpna_6");

		return enCampMa;
	}

	@Override
	public Entity_ContactLt createContactLtMsg() {

		Entity_ContactLt enContactLt = new Entity_ContactLt();

		enContactLt.setCpid("97e6b32d-c266-4d33-92b4-01ddf33898cd");
		enContactLt.setCpsq(109284);
		enContactLt.setCske("customerkey");
		enContactLt.setCsna("카리나");
		enContactLt.setFlag("HO2");
		enContactLt.setTkda("custid,111");
		enContactLt.setTn01("tn01");
		enContactLt.setTn02("tn02");
		enContactLt.setTn03("tn03");
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
	public Entity_ContactLt findContactLtByid(String cpid) {
		Optional<Entity_ContactLt> optionalEntity = repositoryContactLt.findByCpid(cpid);
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
