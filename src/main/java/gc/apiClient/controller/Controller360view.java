package gc.apiClient.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import gc.apiClient.customproperties.CustomProperties;
import gc.apiClient.entity.oracleH.Entity_DataCall;
import gc.apiClient.entity.oracleH.Entity_DataCallCustomer;
import gc.apiClient.entity.oracleH.Entity_DataCallService;
import gc.apiClient.entity.oracleH.Entity_MasterServiceCode;
import gc.apiClient.entity.oracleH.Entity_WaDataCall;
import gc.apiClient.entity.oracleH.Entity_WaDataCallOptional;
import gc.apiClient.entity.oracleH.Entity_WaDataCallTrace;
import gc.apiClient.entity.oracleH.Entity_WaMTracecode;
import gc.apiClient.entity.oracleM.Entity_MDataCall;
import gc.apiClient.entity.oracleM.Entity_MDataCallCustomer;
import gc.apiClient.entity.oracleM.Entity_MDataCallService;
import gc.apiClient.entity.oracleM.Entity_MMasterServiceCode;
import gc.apiClient.entity.oracleM.Entity_MWaDataCall;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallOptional;
import gc.apiClient.entity.oracleM.Entity_MWaDataCallTrace;
import gc.apiClient.entity.oracleM.Entity_MWaMTracecode;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.interfaceCollection.InterfaceDBPostgreSQL;
import gc.apiClient.interfaceCollection.InterfaceMsgObjOrcl;
import gc.apiClient.interfaceCollection.InterfaceWebClient;
import gc.apiClient.messages.MessageTo360View;
import gc.apiClient.service.ServiceJson;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@Profile("oracleH")
public class Controller360view extends ServiceJson {

	private final InterfaceDBOracle serviceOracle;
	private final InterfaceMsgObjOrcl serviceMsgObjOrcl;

	public Controller360view(InterfaceDBPostgreSQL serviceDb, InterfaceDBOracle serviceOracle,
			InterfaceWebClient serviceWeb, CustomProperties customProperties, InterfaceMsgObjOrcl serviceMsgObjOrcl) {
		this.serviceOracle = serviceOracle;
		this.serviceMsgObjOrcl = serviceMsgObjOrcl;
	}


	@Scheduled(fixedRate = 60000) //1분 간격으로 아래 함수들을 자동 실행. 
	public void scheduledMethod() {


		Mono.fromCallable(() -> Msg360Datacall())
		.subscribeOn(Schedulers.boundedElastic())
		.subscribe();

		Mono.fromCallable(() -> Msg360DataCallCustomer())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360DataCallService())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MDatacall())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MDataCallCustomer())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MDataCallService())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MMstrsSvcCd())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MstrsSvcCd())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MWaDataCall())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MWaDataCallOptional())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MWaDataCallTrace())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360MWaMTrCode())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360WaDataCall())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360WaDataCallOptional())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360WaDataCallTrace())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
		
		Mono.fromCallable(() -> Msg360WaMTrCode())
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();

	}


	@GetMapping("/360view1")
	public Mono<ResponseEntity<String>> Msg360Datacall() {

		try {

			String topic_id = "from_clcc_hmcepcalldt_message"; //토픽아이디
			int numberOfRecords = serviceOracle.getRecordCount(topic_id); // 해당 테이블에서 레코드 개수를 가지고 온다. 
			log.info("(DataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
				// 2. crud 구분해서 메시지 키를 정한다.
				// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCall> entitylist = serviceOracle.getAll(Entity_DataCall.class); //해당 테이블에서 최대 1000개의 레코드만 리스트로 가지고 온다. 

				for (int i = 0; i < entitylist.size(); i++) {//리스트에 담긴 레코드릐 개수 만큼 루프를 돌면서 로직 식행. 

					String crudtype = entitylist.get(i).getCmd(); // 리스트의 0번째 레코드 부터, crudtype이 무엇인지 , 예) insert, updqte 등등.
					int orderid = entitylist.get(i).getOrderid(); // 레코드 고유의 id. 레코드를 메시지 형태로 보내고 난 후 해당 레코드를 지우기 위한 키로 사용. 

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallMsg(entitylist.get(i), crudtype));//DataCallMsg함수의 매개변수로 레코드(Entity 형식의 객체 => 어떻게 속성이 구성되어 있는지는 'Entity_DataCall.class'참조)와, crudtype을 넘기고 String 값을 리턴 받는다. 
					serviceOracle.deleteAll(Entity_DataCall.class, orderid); //orderid를 사용하여 레코드를 메시지 형태로 보내고 해당 레코드를 쉐도우 테이블에서 삭제. 
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

		}
		return Mono.just(ResponseEntity.ok("'Msg360Datacall' got message successfully."));
	}
	
	//이하 동문... 

	@GetMapping("/360view2")
	public Mono<ResponseEntity<String>> Msg360MDatacall() {

		try {
			String topic_id = "from_clcc_mblcepcalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCall> entitylist = serviceOracle.getAll(Entity_MDataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCall.class, orderid);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MDatacall' got message successfully."));
	}

	@GetMapping("/360view3")

	public Mono<ResponseEntity<String>> Msg360DataCallCustomer() {

		try {
			String topic_id = "from_clcc_hmcepcalldtcust_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(DataCallCustomer) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCallCustomer> entitylist = serviceOracle.getAll(Entity_DataCallCustomer.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();

					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallCustomerMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_DataCallCustomer.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360DataCallCustomer' got message successfully."));
	}

	@GetMapping("/360view4")
	public Mono<ResponseEntity<String>> Msg360MDataCallCustomer() {

		try {

			String topic_id = "from_clcc_mblcepcalldtcust_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCallCustomer) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCallCustomer> entitylist = serviceOracle.getAll(Entity_MDataCallCustomer.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallCustomerMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCallCustomer.class, orderid);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MDataCallCustomer' got message successfully."));
	}

	@GetMapping("/360view5")
	public Mono<ResponseEntity<String>> Msg360DataCallService() {

		try {
			String topic_id = "from_clcc_hmcepcallsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(DataCallService) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_DataCallService> entitylist = serviceOracle.getAll(Entity_DataCallService.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallService(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_DataCallService.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360DataCallService' got message successfully."));
	}

	@GetMapping("/360view6")
	public Mono<ResponseEntity<String>> Msg360MDataCallService() {

		try {
			String topic_id = "from_clcc_mblcepcallsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MDataCallService) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MDataCallService> entitylist = serviceOracle.getAll(Entity_MDataCallService.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.DataCallService(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MDataCallService.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MDataCallService' got message successfully."));
	}

	@GetMapping("/360view7")
	public Mono<ResponseEntity<String>> Msg360MstrsSvcCd() {
		try {
			String topic_id = "from_clcc_hmcepcallmstrsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MasterServiceCode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MasterServiceCode> entitylist = serviceOracle.getAll(Entity_MasterServiceCode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.MstrSvcCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MasterServiceCode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MstrsSvcCd' got message successfully."));
	}

	@GetMapping("/360view8")
	public Mono<ResponseEntity<String>> Msg360MMstrsSvcCd() {

		try {
			String topic_id = "from_clcc_mblcepcallmstrsvccd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MMasterServiceCode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MMasterServiceCode> entitylist = serviceOracle.getAll(Entity_MMasterServiceCode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.MstrSvcCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MMasterServiceCode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MMstrsSvcCd' got message successfully."));
	}

	@GetMapping("/360view9")
	public Mono<ResponseEntity<String>> Msg360WaDataCall() {

		try {
			String topic_id = "from_clcc_hmcepwacalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCall> entitylist = serviceOracle.getAll(Entity_WaDataCall.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaDataCall.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaDataCall' got message successfully."));
	}

	@GetMapping("/360view10")
	public Mono<ResponseEntity<String>> Msg360MWaDataCall() {

		try {
			String topic_id = "from_clcc_mblcepwacalldt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCall) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCall> entitylist = serviceOracle.getAll(Entity_MWaDataCall.class);
				
				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCall.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCall' got message successfully."));
	}

	@GetMapping("/360view11")
	public Mono<ResponseEntity<String>> Msg360WaDataCallOptional() {

		try {
			String topic_id = "from_clcc_hmcepwacallopt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCallOptional) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCallOptional> entitylist = serviceOracle.getAll(Entity_WaDataCallOptional.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallOptionalMsg(entitylist.get(i), crudtype));

					serviceOracle.deleteAll(Entity_WaDataCallOptional.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaDataCallOptional' got message successfully."));
	}

	@GetMapping("/360view12")
	public Mono<ResponseEntity<String>> Msg360MWaDataCallOptional() {

		try {
			String topic_id = "from_clcc_mblcepwacallopt_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCallOptional) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCallOptional> entitylist = serviceOracle.getAll(Entity_MWaDataCallOptional.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallOptionalMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCallOptional.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCallOptional' got message successfully."));
	}

	@GetMapping("/360view13")
	public Mono<ResponseEntity<String>> Msg360WaDataCallTrace() {

		try {

			String topic_id = "from_clcc_hmcepwacalltr_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaDataCallTrace) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaDataCallTrace> entitylist = serviceOracle.getAll(Entity_WaDataCallTrace.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallTraceMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaDataCallTrace.class, orderid);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360WaDataCallTrace' got message successfully."));
	}

	@GetMapping("/360view14")
	public Mono<ResponseEntity<String>> Msg360MWaDataCallTrace() {

		try {
			String topic_id = "from_clcc_mblcepwacalltr_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaDataCallTrace) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaDataCallTrace> entitylist = serviceOracle.getAll(Entity_MWaDataCallTrace.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaDataCallTraceMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaDataCallTrace.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360MWaDataCallTrace' got message successfully."));
	}

	@GetMapping("/360view15")
	public Mono<ResponseEntity<String>> Msg360WaMTrCode() {

		try {
			String topic_id = "from_clcc_hmcepwatrcd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(WaMTracecode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_WaMTracecode> entitylist = serviceOracle.getAll(Entity_WaMTracecode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaMTraceCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_WaMTracecode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}
		return Mono.just(ResponseEntity.ok("'Msg360WaMTrCode' got message successfully."));
	}

	@GetMapping("/360view16")
	public Mono<ResponseEntity<String>> Msg360MWaMTrCode() {

		try {
			String topic_id = "from_clcc_mblcepwatrcd_message";
			int numberOfRecords = serviceOracle.getRecordCount(topic_id);
			log.info("(MWaMTracecode) the number of records : {}", numberOfRecords);

			if (numberOfRecords < 1) {

			} else {// 1. 쉐도우 테이블에 레코드가 1개 이상 있다면 있는 레코드들을 다 긁어 온다.
					// 2. crud 구분해서 메시지 키를 정한다.
					// 3. 프로듀서로 메시지 재가공해서 보낸다.
				List<Entity_MWaMTracecode> entitylist = serviceOracle.getAll(Entity_MWaMTracecode.class);

				for (int i = 0; i < entitylist.size(); i++) {

					String crudtype = entitylist.get(i).getCmd();
					int orderid = entitylist.get(i).getOrderid();
					MessageTo360View.SendMsgTo360View(topic_id,
							serviceMsgObjOrcl.WaMTraceCdMsg(entitylist.get(i), crudtype));
					serviceOracle.deleteAll(Entity_MWaMTracecode.class, orderid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Message : {}", e.getMessage());
		}

		return Mono.just(ResponseEntity.ok("'Msg360MWaMTrCode' got message successfully."));
	}

}
