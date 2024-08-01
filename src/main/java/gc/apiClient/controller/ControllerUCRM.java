package gc.apiClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gc.apiClient.BusinessLogic;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.entity.postgresql.Entity_CampMa_D;
import gc.apiClient.entity.postgresql.Entity_CampRt;
import gc.apiClient.entity.postgresql.Entity_ContactLt;
import gc.apiClient.entity.postgresql.Entity_Ucrm;
import gc.apiClient.entity.postgresql.Entity_UcrmRt;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.kafMsges.MsgUcrm;
import gc.apiClient.messages.MessageToProducer;
import gc.apiClient.service.CreateEntity;
import gc.apiClient.service.ServiceInstCmpRt;
import gc.apiClient.service.ServiceJson;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ControllerUCRM {
	private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	private final InterfaceDBPostgreSQL serviceDb;
	private final InterfaceWebClient serviceWeb;
	private final CreateEntity createEntity;
	private final ServiceInstCmpRt serviceInstCmpRt;
	private final  MsgUcrm msgUcrm;

	public ControllerUCRM(InterfaceDBPostgreSQL serviceDb
			, InterfaceWebClient serviceWeb
			, CreateEntity createEntity
			, ServiceInstCmpRt serviceInstCmpRt
			, MsgUcrm msgUcrm) {
		this.serviceDb = serviceDb;
		this.createEntity = createEntity;
		this.serviceWeb = serviceWeb;
		this.serviceInstCmpRt = serviceInstCmpRt;
		this.msgUcrm = msgUcrm;
	}

	// 이것은 카프카 컨슈머에서 호출하는 api. 컨슈머 앱에서 특정 토픽을 구독하면서 메시지를 받는다. 메시지를 받은 컨슈머는 이 api를 호출하여 받은 메시지를 전달해준다.
	@PostMapping("/saveucrmdata")
	public Mono<ResponseEntity<String>> saveUcrmData(@RequestBody String msg) {

		try {

			Entity_Ucrm enUcrm = createEntity.createUcrm(msg); // 전달 받은 String 형태의 메시지를 쉐도우테이블('UCRMLT')에 인서트 하기 위해서 Entity 형태로 제가공해준다.
			if(enUcrm == null) {
				return Mono.empty();
			}
			serviceDb.insertUcrm(enUcrm);
			log.info("(saveUcrmData) - 저장된 메시지 : {}", msg);

		} catch (Exception e) {
			log.error("(saveUcrmData) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : %s", e.getMessage())));
		}

		return Mono.empty();
	}
	

	@Transactional
	public Mono<Void> addUcrmMsgFrmConsumer() {// 이 함수는 스케줄러에 의해 5초마다 실행되면서 쉐도우 테이블('UCRMLT')에 있는 데이터들을 처리해주는 작업을 수행한다.
		try {

			Page<Entity_Ucrm> entitylist = serviceDb.getAll("A");// 바로 위의 함수 'SaveUcrmData'에 의해 테이블에 적재된 데이터들은 최대 1000개씩 불러온다.

			if (entitylist.isEmpty()) {
			} else {
				int reps = entitylist.getNumberOfElements();
				log.info("(addUcrmMsgFrmConsumer) - UCRMLT 테이블에서 조회된 레코드 개수 : {}", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();// 키 : cpid, 값 : contactLtId
				Map<String, String> mapquetId = new HashMap<String, String>();// 키 : cpid, 값 : queid
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();// 키 : contactLtId, 값

				List<String> invalid_camp = new ArrayList<String>();
				Entity_CampMa_D enCpma_D = null;
				String contactLtId = "";
				String flag = "";
				String cpid = "";
				String queid = "";

				for (int i = 0; i < reps; i++) {// 가져온 레코드 갯수만큼 반복.

					flag = entitylist.getContent().get(i).getWorkDivsCd();

					if (flag.equals("D")) {
						continue;
					} else {

						cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
						contactLtId = mapcontactltId.get(cpid);
						queid = mapquetId.get(cpid);

						if (contactLtId == null || contactLtId.equals("")) {// cpid로 Map(mapcontactltId)을 조회했는데 Map 안에 그것(cpid)에 대응하는 contactltId가 없다면,

							if (invalid_camp.contains(cpid)) {// 지금 레코드에 있는 캠페인 아이디가 유효하지 않은 캠페인 아이디라면 api호출 없이 DB에서 해당 레코드 삭제 후 그냥 다음 레코드로 넘어감.
								serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
								continue;
							}

							String result = serviceWeb.getCampaignsApiReq("campaignId", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

							if (result.equals("")) {// cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아내려고 했는데 결과 값이 없다면, 혹시 campma_d테이블에 존재하는지 조회.

								try {
									log.info("(addUcrmMsgFrmConsumer) - 캠페인 아이디 ({})로 api호출 결과 결과가 없습니다. 마스터D 테이블을 조회합니다.", cpid);
									enCpma_D = serviceDb.findCampMa_DByCpid(cpid);
									contactLtId = enCpma_D.getContactltid();
									queid = enCpma_D.getQueueid();

									mapcontactltId.put(cpid, contactLtId);
									mapquetId.put(cpid, queid);
									serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());

								} catch (Exception e) {// campma_d테이블을 조회했는데도 없다면 유효하지 않은 캠페인으로 처리하고 해당레코드 삭제
									log.info("(addUcrmMsgFrmConsumer) - 마스터D 테이블 조회 결과 유효한 캠페인 아이디 ({})가 아닙니다", cpid);
									invalid_camp.add(cpid); // 유효하지 않은 캠페인 저장.
									serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
									// 밑의 로직을 수행하지 않고 다음 i번째로 넘어간다.
									continue;
								}

							} else {

								JSONObject res = ServiceJson.extractObjVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예 )contactlistid::queueid
								contactLtId = res.getString("contactltid");
								queid = res.getString("queueid");

								mapcontactltId.put(cpid, contactLtId);
								mapquetId.put(cpid, queid);
							}

						}

						// UCRM에서 보내 준 데이터를 가지고 제네시스에 PUSH하기 위해서 필요한 데이터들을 추출한다.
						JSONObject contactltObj = ServiceJson.extractObjVal("ExtractRawUcrm", entitylist.getContent().get(i));
						contactltObj.put("contactltid", contactLtId);
						contactltObj.put("queueid", queid);
						String contactltMapper = createEntity.createContactLtGC(contactltObj); // row_result = cpid::cpsq::cske::csno::tkda::flag::contactltid::queid

						// 제네시스에 인서트 하기 위한 배열 준비
						if (!contactlists.containsKey(contactLtId)) {
							contactlists.put(contactLtId, new ArrayList<>());
						}
						
						if( !contactltMapper.equals("") ) {
							contactlists.get(contactLtId).add(contactltMapper);
						}
					}

				}

				invalid_camp.clear(); // 유효하지 않았던 cpid들을 저장해 둔 배열 안의 요소들 삭제

				// 캠페인 컨택리스트 적재를 위한 Genesys API 호출 (add contact)
				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
					int originlistSize = entry.getValue().size();
					// entry.getValue().size()=0 일때 API 호출을 할 필요가 없음.
					if (originlistSize > 0) {

						List<String> contacts = new ArrayList<String>();
						int unit = originlistSize / 250;
						reps = unit + 1;

						for (int i = 0; i < reps; i++) {
							int startIdx = i * 250;
							int endIdx = Math.min(startIdx + 250, originlistSize);
							for (int j = startIdx; j < endIdx; j++) {
								contacts.add(entry.getValue().get(j));
							}
							if (contacts.size() != 0) {

								JSONObject jsonObject = new JSONObject(contacts.get(0));
								JSONObject dataObject = jsonObject.getJSONObject("data");
								cpid = dataObject.getString("CPID");

								log.info("(addUcrmMsgFrmConsumer) - (Add) cpid(contactLtId) {}({})의 현재 사이즈 {} 중, {}번째 유닛인 contacts의 사이즈 {}", cpid, entry.getKey(), originlistSize, i, contacts.size());

								serviceWeb.postContactLtApiReq("contact", entry.getKey(), contacts);

								for (int k = 0; k < contacts.size(); k++) {

									jsonObject = new JSONObject(contacts.get(k));
									dataObject = jsonObject.getJSONObject("data");

									// CPID와 CPSQ 값을 추출
									cpid = dataObject.getString("CPID");
									String cpsq = dataObject.getString("CPSQ");
									Entity_ContactLt enContactLt = createEntity.createContactUcrm(jsonObject);
									if(enContactLt == null) {
										continue;
									}
									serviceDb.delUcrmltRecord(cpid, cpsq);// ucrmlt (쉐도우테이블에서 삭제)
									serviceDb.insertContactLt(enContactLt); // contactlt테이블에 인서트
								}
								contacts.clear();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("(addUcrmMsgFrmConsumer) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.empty();
	}

	@Transactional
	public Mono<Void> delUcrmMsgFrmCnsmer() {// 이 함수는 스케줄러에 의해 5초마다 실행되면서 쉐도우 테이블('UCRMLT')에 있는 데이터들을 처리해주는 작업을 수행한다.
		try {

			Page<Entity_Ucrm> entitylist = serviceDb.getAll("D");// 바로 위의 함수 'SaveUcrmData'에 의해 테이블에 적재된 데이터들은 최대 1000개씩 불러온다.

			if (entitylist.isEmpty()) {
			} else {
				int reps = entitylist.getNumberOfElements();
				log.info("(delUcrmMsgFrmCnsmer) - UCRMLT 테이블에서 조회된 레코드 개수 : {}", reps);

				Map<String, String> mapcontactltId = new HashMap<String, String>();// 키 : cpid, 값 : contactLtId
				Map<String, String> mapquetId = new HashMap<String, String>();// 키 : cpid, 값 : queid
				Map<String, List<String>> delcontactlists = new HashMap<String, List<String>>();
				Map<String, List<String>> delcontactltTb = new HashMap<String, List<String>>();

				List<String> invalid_camp = new ArrayList<String>();
				String contactLtId = "";
				String flag = "";
				String cpid = "";
				String queid = "";
				Entity_CampMa_D enCpma_D = null;

				for (int i = 0; i < reps; i++) {// 가져온 레코드 갯수만큼 반복.

					flag = entitylist.getContent().get(i).getWorkDivsCd();
					if (flag.equals("A")) {
						continue;
					} else {

						cpid = entitylist.getContent().get(i).getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
						contactLtId = mapcontactltId.get(cpid);
						queid = mapquetId.get(cpid);

						if (contactLtId == null || contactLtId.equals("")) {// cpid로 Map(mapcontactltId)을 조회했는데 Map 안에 그것(cpid)에 대응하는 contactltId가 없다면,

							if (invalid_camp.contains(cpid)) {// 지금 레코드에 있는 캠페인 아이디가 유효하지 않은 캠페인 아이디라면 api호출 없이 DB에서 해당 레코드 삭제 후 그냥 다음 레코드로
																// 넘어감.
								serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
								continue;
							}

							String result = serviceWeb.getCampaignsApiReq("campaignId", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

							if (result.equals("")) {// cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아내려고 했는데 결과 값이 없다면, 혹시 campma_d테이블에
													// 존재하는지 조회.

								try {
									log.info("(delUcrmMsgFrmCnsmer) - 캠페인 아이디 ({})로 api호출 결과 결과가 없습니다. 마스터D 테이블을 조회합니다.", cpid);
									enCpma_D = serviceDb.findCampMa_DByCpid(cpid);
									contactLtId = enCpma_D.getContactltid();
									queid = enCpma_D.getQueueid();

									mapcontactltId.put(cpid, contactLtId);
									mapquetId.put(cpid, queid);
									serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());

								} catch (Exception e) {// campma_d테이블을 조회했는데도 없다면 유효하지 않은 캠페인으로 처리하고 해당레코드 삭제
									log.info("(delUcrmMsgFrmCnsmer) - 마스터D 테이블 조회 결과 유효한 캠페인 아이디 ({})가 아닙니다", cpid);
									invalid_camp.add(cpid); // 유효하지 않은 캠페인 저장.
									serviceDb.delUcrmLtById(entitylist.getContent().get(i).getTopcDataIsueSno());
									// 밑의 로직을 수행하지 않고 다음 i번째로 넘어간다.
									continue;
								}

							} else {

								JSONObject res = ServiceJson.extractObjVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예 )contactlistid::queueid
								contactLtId = res.getString("contactltid");
								queid = res.getString("queueid");

								mapcontactltId.put(cpid, contactLtId);
								mapquetId.put(cpid, queid);
							}

						}
						

						// UCRM에서 보내 준 데이터를 가지고 제네시스에 PUSH하기 위해서 필요한 데이터들을 추출한다.
						JSONObject contactltObj = ServiceJson.extractObjVal("ExtractRawUcrm", entitylist.getContent().get(i));
						contactltObj.put("contactltid", contactLtId);
						contactltObj.put("queueid", queid);
						String contactltMapper = createEntity.createContactLtGC(contactltObj); // row_result = cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

						// 제네시스에서 회수 하기 위한 배열 준비
						if (!delcontactlists.containsKey(contactLtId)) {
							delcontactlists.put(contactLtId, new ArrayList<>());
						}

						// 제네시스에서 회수 후 DB테이블에서 레코드를 삭제하기 위한 배열 준비
						if (!delcontactltTb.containsKey(contactLtId)) {
							delcontactltTb.put(contactLtId, new ArrayList<>());
						}

						try {

							if( !contactltMapper.equals("") ) {//2024-07-31 발신 대상자 정보가 빈 값이 아닐 때만 수행 즉, 발신 대상자 정보가 올바른 포맷으로 왔을 때만 실행
								JSONObject jsonObject = new JSONObject(contactltMapper);
								JSONObject dataObject = jsonObject.getJSONObject("data");

								// CPID와 CPSQ 값을 추출
								String cpsq = dataObject.getString("CPSQ");
								delcontactlists.get(contactLtId).add(cpsq);
								delcontactltTb.get(contactLtId).add(contactltMapper);
							}

						} catch (Exception e) { // 인서트 하려고 했는데 이미 있는 데이터여서 에러가 발생한 경우
							log.error("(delUcrmMsgFrmCnsmer) - 에러 발생 : {}", e.getMessage());
							errorLogger.error(e.getMessage(), e);
						}
					}
				}

				invalid_camp.clear(); // 유효하지 않았던 cpid들을 저장해 둔 배열 안의 요소들 삭제

				for (Map.Entry<String, List<String>> entry : delcontactlists.entrySet()) {
					int originlistSize = entry.getValue().size();
					// entry.getValue().size()=0 일때 API 호출을 할 필요가 없음.
					if (originlistSize > 0) {

						List<String> contacts = new ArrayList<String>();
						int unit = originlistSize / 250;
						reps = unit + 1;

						for (int i = 0; i < reps; i++) {

							int startIdx = i * 250;
							int endIdx = Math.min(startIdx + 250, originlistSize);
							for (int j = startIdx; j < endIdx; j++) {
								contacts.add(entry.getValue().get(j));
							}

							if (contacts.size() != 0) {

								log.info("(delUcrmMsgFrmCnsmer) - (delete)delcontactlists (contactLtId) ({})의 현재 사이즈 {} 중, {}번째 유닛인 contacts의 사이즈{}", entry.getKey(), originlistSize, i, contacts.size());

								serviceWeb.delContacts("delcontacts", entry.getKey(), contacts);
								contacts.clear();
							}
						}

					}
				}

				// 캠페인 컨택리스트 삭제를 위한 Genesys API 호출 (delete contact)
				for (Map.Entry<String, List<String>> entry : delcontactltTb.entrySet()) {
					int originlistSize = entry.getValue().size();
					// entry.getValue().size()=0 일때 API 호출을 할 필요가 없음.
					if (originlistSize > 0) {

						List<String> contacts = new ArrayList<String>();
						int unit = originlistSize / 250;
						reps = unit + 1;

						for (int i = 0; i < reps; i++) {

							int startIdx = i * 250;
							int endIdx = Math.min(startIdx + 250, originlistSize);
							for (int j = startIdx; j < endIdx; j++) {
								contacts.add(entry.getValue().get(j));
							}
							if (contacts.size() != 0) {

								JSONObject jsonObject = new JSONObject(contacts.get(0));
								JSONObject dataObject = jsonObject.getJSONObject("data");
								cpid = dataObject.getString("CPID");

								log.info("(delUcrmMsgFrmCnsmer) - (delete)delcontactltTb cpid(contactLtId) {}({})의 현재 사이즈 {} 중, {}번째 유닛인 contacts의 사이즈 {}", cpid, entry.getKey(), originlistSize, i, contacts.size());

								for (int k = 0; k < contacts.size(); k++) {

									jsonObject = new JSONObject(contacts.get(k));
									dataObject = jsonObject.getJSONObject("data");

									// CPID와 CPSQ 값을 추출
									cpid = dataObject.getString("CPID");
									String cpsq = dataObject.getString("CPSQ");

									serviceDb.delUcrmltRecord(cpid, cpsq);// uacrmlt (쉐도우테이블에서 삭제)
									serviceDb.delContactltRecord(cpid, cpsq);// contactlt테이블에서 삭제

								}
								contacts.clear();
							}
						}
					}
				}

			}

		} catch (Exception e) {
			log.error("(delUcrmMsgFrmCnsmer) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.empty();
	}
	
	
	@PostMapping("/insertucrmlist")
	public Mono<ResponseEntity<String>> insertUcrmList(@RequestBody String msg) {
		try {

			JSONArray jsonArray = new JSONArray(msg);

			JSONObject jsonObj = new JSONObject();
			String singleDate = "";

			for (int i = 0; i < jsonArray.length(); i++) {

				jsonObj = jsonArray.getJSONObject(i);
				singleDate = jsonObj.toString();
				Entity_Ucrm enUcrm = createEntity.createUcrm(singleDate); // 전달 받은 String 형태의 메시지를 쉐도우테이블('UCRMLT')에 인서트 하기 위해서 Entity 형태로 재가공해준다.
				if(enUcrm == null) {
					return Mono.empty();
				}
				serviceDb.insertUcrm(enUcrm);
				log.info("저장된 메시지 : {}", msg);
			}

		} catch (Exception e) {
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
			return Mono.just(ResponseEntity.ok().body(String.format("You've got an error : %s", e.getMessage())));
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}
	

	@GetMapping("/pushucrm/{cpid}")
	public Mono<ResponseEntity<String>> pushucrm(@PathVariable("cpid") String cpid) { // 이 함수는 스케줄러에 의해 5초마다 실행되면서 쉐도우 테이블('UCRMLT')에 있는 데이터들을 처리해주는 작업을 수행한다.

		try {
			long startTime = System.currentTimeMillis();
			log.info("====== Method : pushucrm ======");
			log.info("cpid : {}", cpid);

			List<Entity_ContactLt> records = serviceDb.getRecordsByCpid(cpid);
			Map<String, List<String>> contactlists = new HashMap<>(); // 키 : contactLtId, 값
			log.info("records 사이즈 : {}", records.size());

			if (records.size() == 0) {
				log.info("contactlt 에서 조회된 모든 레코드 : 없음");
			} else {
				int reps = records.size();
				log.info("{}만큼 반복", reps);
				String cpsq = "";
				String cske = "";
				String csno = "";
				String tkda = "";
				String flag = "";
				String contactLtId = "";
				String queid = "";

				String result = serviceWeb.getCampaignsApiReq("campaignId", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

				// cpid::cpsq::cske::csno::tkda::flag::contactltId::queid
				for (int i = 0; i < reps; i++) { // 가져온 레코드 갯수만큼 반복.
					int temp = records.get(i).getId().getCpsq();
					cpsq = Integer.toString(temp);
					cske = records.get(i).getCske();
					csno = records.get(i).getTno1();
					tkda = records.get(i).getTkda();
					flag = records.get(i).getFlag();

					JSONObject res = ServiceJson.extractObjVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예) contactlistid::queueid
					contactLtId = res.getString("contactltid");
					queid = res.getString("queueid");
					
					JSONObject contactltObj = new JSONObject();
					contactltObj.put("cpid",cpid);
					contactltObj.put("cpsq",cpsq);
					contactltObj.put("cske",cske);
					contactltObj.put("csno",csno);
					contactltObj.put("tkda",tkda);
					contactltObj.put("flag",flag);
					contactltObj.put("contactltid",contactLtId);
					contactltObj.put("queueid",queid);

					String contactltMapper = createEntity.createContactLtGC(contactltObj); // row_result = cpid::cpsq::cske::csno::tkda::flag::contactltId::queid

					// 제네시스에 인서트 하기 위한 배열 준비
					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}

					try {
						if (flag.equals("A")) { // Add Contact
							
							if( !contactltMapper.equals("") ) {//2024-07-31 발신 대상자 정보가 빈 값이 아닐 때만 수행 즉, 발신 대상자 정보가 올바른 포맷으로 왔을 때만 실행
								contactlists.get(contactLtId).add(contactltMapper);
							}

							if (contactlists.get(contactLtId).size() >= 250) {
								for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
									log.info("contactlists 사이즈가 250이상일 때");
									log.info("(Add)Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
									serviceWeb.postContactLtApiReq("contact", entry.getKey(), entry.getValue());
								}
								for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
									entry.getValue().clear();
								}
							}

						} else {
							log.info("FLAG A or D가 아님");
						}

					} catch (Exception ex) { // 인서트 하려고 했는데 이미 있는 데이터여서 에러가 발생한 경우
						log.error("Exception 발생 : {}, {}", ex.getMessage(), ex);
					}
				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {
					log.info("contactlists 나머지");
					log.info("(Add)Arraylist '{}'의 현재 사이즈: {}", entry.getKey(), entry.getValue().size());
					serviceWeb.postContactLtApiReq("contact", entry.getKey(), entry.getValue());
				}
				long elapsedTime = System.currentTimeMillis() - startTime;
				log.info("## pushucrm 소요시간 :: " + elapsedTime + " ms");

			}
		} catch (Exception e) {
			log.error("에러 메시지 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}

	@GetMapping("/senducrmrt")
	public Mono<ResponseEntity<String>> sendUcrmRt() {//2024-08-01 @Transactional 어노테이션 삭제.

		try {

			Page<Entity_UcrmRt> entitylist = serviceDb.getAllUcrmRt();

			if (entitylist.isEmpty()) {
			} else {
				int reps = entitylist.getNumberOfElements();
				log.info("(sendUcrmRt) - CAMPRT_UCRM_W table에서 조회된 레코드 개수 : {}", reps);

				Map<String, String> mapdivision = new HashMap<String, String>();
				Map<String, String> mapcontactltId = new HashMap<String, String>();// 키 : cpid, 값 : contactLtId
				Map<String, List<String>> contactlists = new HashMap<String, List<String>>();

				List<String> invalid_camp = new ArrayList<String>();
				String contactLtId = "";
				String divisionid = "";
				String cpid = "";
				String cqsq = "";
				Entity_CampMa_D enCpma_D = null;

				for (int i = 0; i < reps; i++) {

					Entity_UcrmRt enUcrmRt = entitylist.getContent().get(i);

					cpid = enUcrmRt.getId().getCpid(); // 첫번째 레코드부터 cpid를 가지고 온다.
					cqsq = enUcrmRt.getId().getCpsq(); // 첫번째 레코드부터 cpid를 가지고 온다.
					contactLtId = mapcontactltId.get(cpid);
					divisionid = enUcrmRt.getDivisionid();

					if (contactLtId == null || contactLtId.equals("")) {

						if (invalid_camp.contains(cpid)) {// 지금 레코드에 있는 캠페인 아이디가 유효하지 않은 캠페인 아이디라면 api호출 없이 DB에서 해당 레코드 삭제 후 그냥 다음 레코드로
							// 넘어감.
							serviceDb.delUcrmRtById(enUcrmRt.getId());
							continue;
						}

						String result = serviceWeb.getCampaignsApiReq("campaignId", cpid); // cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아낸다

						if (result.equals("")) {// cpid를 가지고 직접 제네시스 api를 호출해서 contactltId를 알아내려고 했는데 결과 값이 없다면, 혹시 campma_d테이블에 존재하는지 조회.

							try {
								log.info("(sendUcrmRt) - 캠페인 아이디 ({})로 api호출 결과 결과가 없습니다. 마스터D 테이블을 조회합니다.", cpid);
								enCpma_D = serviceDb.findCampMa_DByCpid(cpid);

								contactLtId = enCpma_D.getContactltid();
								mapcontactltId.put(cpid, contactLtId);
								mapdivision.put(contactLtId, divisionid);

							} catch (Exception e) {// campma_d테이블을 조회했는데도 없다면 유효하지 않은 캠페인으로 처리하고 해당레코드 삭제
								log.info("(sendUcrmRt) - 마스터D 테이블 조회 결과 유효한 캠페인 아이디 ({})가 아닙니다", cpid);
								invalid_camp.add(cpid); // 유효하지 않은 캠페인 저장.
								serviceDb.delUcrmRtById(enUcrmRt.getId());
								// 밑의 로직을 수행하지 않고 다음 i번째로 넘어간다.
								continue;
							}

						} else {

							JSONObject res = ServiceJson.extractObjVal("ExtractContactLtId", result); // 가져온 결과에서 contactlistid,queueid만 추출. 변수 'res' 형식의 예 )contactlistid::queueid
							contactLtId = res.getString("contactltid");
							mapcontactltId.put(cpid, contactLtId);
							mapdivision.put(contactLtId, divisionid);
						}

					}

					if (!contactlists.containsKey(contactLtId)) {
						contactlists.put(contactLtId, new ArrayList<>());
					}

					contactlists.get(contactLtId).add(cqsq);
					serviceDb.delUcrmRtById(enUcrmRt.getId());

					log.info("(sendUcrmRt) - 해당 키 값이 '{}'인 Arraylist에 값 추가", contactLtId);

					for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

						divisionid = mapdivision.get(entry.getKey());

						if (entry.getValue().size() >= 50) {
							sendCampRtToCUcrm(entry.getKey(), entry.getValue(), divisionid);
						}
					}

				}

				for (Map.Entry<String, List<String>> entry : contactlists.entrySet()) {

					divisionid = mapdivision.get(entry.getKey());
					sendCampRtToCUcrm(entry.getKey(), entry.getValue(), divisionid);
				}
				
				invalid_camp.clear();
			}

		} catch (Exception e) {
			log.error("(sendUcrmRt) - 에러 발생 : {}", e.getMessage());
			errorLogger.error(e.getMessage(), e);
		}

		return Mono.just(ResponseEntity.ok("Successfully processed the message."));
	}
	

	public Mono<Void> sendCampRtToCUcrm(String contactLtId, List<String> values, String divisionid) throws Exception {

		String result = serviceWeb.postContactLtApiBulk("contactList", contactLtId, values);

		if (result.equals("")) {
			log.info("(sendCampRtToCUcrm) - 결과 없음, 다음으로 건너 뜀.");
			values.clear();
			return Mono.empty();
		}

		// 캠페인이 어느 비즈니스 로직인지 판단하기 위해서 일단 목록 중 하나만 꺼내서 확인해 보도록한다. 왜냐면 나머지는 똑같을테니.
		JSONObject contactsresult = ServiceJson.extractObjVal("ExtractContacts", result, 0);// JsonString 결과값과 조회하고 싶은 인덱스(첫번째)를 인자로 넣는다.
		String cpid = contactsresult.getString("cpid");

		Entity_CampMa enCampMa = serviceDb.findCampMaByCpid(cpid);
		Entity_CampRt entityCmRt = null;

		// 토큰데이터와 디비젼네임을 인자로 넘겨서 어떤 비지니스 로직인지, 토픽은 어떤 것으로 해야하는지를 결과 값으로 반환 받는다.
		Map<String, String> businessLogic = BusinessLogic.rtSelectedBusiness(divisionid);
		String topic_id = businessLogic.get("topic_id");

		for (int i = 0; i < values.size(); i++) {//리스트에 담긴 각 콜들에 대해, 발신결과를 메시지형태로 카프카로 보내고 디비에 저장. 리스트 예)[73558350, 73558386, 73558408]
			contactsresult = ServiceJson.extractObjVal("ExtractContacts", result, i);
			if (contactsresult == null) {
				log.info("(sendCampRtToCUcrm) - 결과 없음, 다음으로 건너 뜀.");
				continue;
			}

			entityCmRt = createEntity.createCampRtMsg(contactsresult, enCampMa); // 카프카로 메시지 전달을 위한 entity.
			if(entityCmRt == null) {
				continue;
			}
			String msg = msgUcrm.makeRtMsg(entityCmRt);

			int dirt = entityCmRt.getDirt();// 응답코드

			if (dirt == 1) {// URM이면서 정상일 때.
				log.info("(sendCampRtToCUcrm) - UCRM : dirt(응답코드)가 '1'이므로 카프카로 발신결과 메시지를 보내지 않습니다.");
			} else {
				MessageToProducer producer = new MessageToProducer();
				String endpoint = "/gcapi/post/" + topic_id;
				producer.sendMsgToProducer(endpoint, msg);
			}

			try {
				serviceInstCmpRt.insrtCmpRt(contactsresult, enCampMa);
//				serviceDb.insertCampRt(entityCmRt);
			} catch (Exception ex) {
				errorLogger.error(ex.getMessage(), ex);
			}
		}
		values.clear();
		return Mono.empty();

	}

}
