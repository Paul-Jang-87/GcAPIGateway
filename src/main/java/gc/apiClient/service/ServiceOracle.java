package gc.apiClient.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

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
import gc.apiClient.repository.oracleH.Repository_DataCall;
import gc.apiClient.repository.oracleH.Repository_DataCallCustomer;
import gc.apiClient.repository.oracleH.Repository_DataCallService;
import gc.apiClient.repository.oracleH.Repository_MasterServiceCode;
import gc.apiClient.repository.oracleH.Repository_WaDataCall;
import gc.apiClient.repository.oracleH.Repository_WaDataCallOptional;
import gc.apiClient.repository.oracleH.Repository_WaDataCallTrace;
import gc.apiClient.repository.oracleH.Repository_WaMTraceCode;
import gc.apiClient.repository.oracleM.Repository_MDataCall;
import gc.apiClient.repository.oracleM.Repository_MDataCallCustomer;
import gc.apiClient.repository.oracleM.Repository_MDataCallService;
import gc.apiClient.repository.oracleM.Repository_MMasterServiceCode;
import gc.apiClient.repository.oracleM.Repository_MWaDataCall;
import gc.apiClient.repository.oracleM.Repository_MWaDataCallOptional;
import gc.apiClient.repository.oracleM.Repository_MWaDataCallTrace;
import gc.apiClient.repository.oracleM.Repository_MWaMTraceCode;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceOracle implements InterfaceDBOracle {
	// 검색 **Create **Insert **Select
	private final Repository_DataCall repositoryDataCall;
	private final Repository_DataCallCustomer repositoryDataCallCustomer;
	private final Repository_DataCallService repositoryDataCallService;
	private final Repository_MasterServiceCode repositoryMasterServiceCode;
	private final Repository_MDataCall repositoryMDataCall;
	private final Repository_MDataCallCustomer repositoryMDataCallCustomer;
	private final Repository_MDataCallService repositoryMDataCallService;
	private final Repository_MMasterServiceCode repositoryMMasterServiceCode;
	private final Repository_MWaDataCall repositoryMWaDataCall;
	private final Repository_MWaDataCallOptional repositoryMWaDataCallOptional;
	private final Repository_MWaDataCallTrace repositoryMWaDataCallTrace;
	private final Repository_MWaMTraceCode repositoryMWaMTraceCode;
	private final Repository_WaDataCall repositoryWaDataCall;
	private final Repository_WaDataCallOptional repositoryWaDataCallOptional;
	private final Repository_WaDataCallTrace repositoryWaDataCallTrace;
	private final Repository_WaMTraceCode repositoryWaMTraceCode;

	public ServiceOracle(Repository_DataCall repositoryDataCall, Repository_DataCallCustomer repositoryDataCallCustomer,
			Repository_DataCallService repositoryDataCallService,
			Repository_MasterServiceCode repositoryMasterServiceCode, Repository_MDataCall repositoryMDataCall,
			Repository_MDataCallCustomer repositoryMDataCallCustomer,
			Repository_MDataCallService repositoryMDataCallService,
			Repository_MMasterServiceCode repositoryMMasterServiceCode, Repository_MWaDataCall repositoryMWaDataCall,
			Repository_MWaDataCallOptional repositoryMWaDataCallOptional,
			Repository_MWaDataCallTrace repositoryMWaDataCallTrace, Repository_MWaMTraceCode repositoryMWaMTraceCode,
			Repository_WaDataCall repositoryWaDataCall, Repository_WaDataCallOptional repositoryWaDataCallOptional,
			Repository_WaDataCallTrace repositoryWaDataCallTrace, Repository_WaMTraceCode repositoryWaMTraceCode) {

		this.repositoryDataCall = repositoryDataCall;
		this.repositoryDataCallCustomer = repositoryDataCallCustomer;
		this.repositoryDataCallService = repositoryDataCallService;
		this.repositoryMasterServiceCode = repositoryMasterServiceCode;
		this.repositoryMDataCall = repositoryMDataCall;
		this.repositoryMDataCallCustomer = repositoryMDataCallCustomer;
		this.repositoryMDataCallService = repositoryMDataCallService;
		this.repositoryMMasterServiceCode = repositoryMMasterServiceCode;
		this.repositoryMWaDataCall = repositoryMWaDataCall;
		this.repositoryMWaDataCallOptional = repositoryMWaDataCallOptional;
		this.repositoryMWaDataCallTrace = repositoryMWaDataCallTrace;
		this.repositoryMWaMTraceCode = repositoryMWaMTraceCode;
		this.repositoryWaDataCall = repositoryWaDataCall;
		this.repositoryWaDataCallOptional = repositoryWaDataCallOptional;
		this.repositoryWaDataCallTrace = repositoryWaDataCallTrace;
		this.repositoryWaMTraceCode = repositoryWaMTraceCode;
	}

	// insert하는 부분은 필요없음.
//	@Override
//	public Entity_WaDataCallOptional InsertWaDataCallOptional(Entity_WaDataCallOptional entityWaDataCallOptional) {
//		
//		Optional<Entity_WaDataCallOptional> existingEntity = repositoryWaDataCallOptional.findById(entityWaDataCallOptional.getWcseq());
//
//        if (existingEntity.isPresent()) {
//            throw new DataIntegrityViolationException("Record with 'wcseq' already exists.");
//        }
//
//        return repositoryWaDataCallOptional.save(entityWaDataCallOptional);
//	}

	@Override
	public Entity_WaDataCallOptional findWaDataCallOptional(int wcseq) {

		try {
			Optional<Entity_WaDataCallOptional> optionalEntity = repositoryWaDataCallOptional.findById(wcseq);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_WaDataCallOptional by wcseq: {}", wcseq, ex);

			return null;
		}
	}

	@Override
	public Entity_DataCall findDataCallByCpid(int orderid) {

		try {
			Optional<Entity_DataCall> optionalEntity = repositoryDataCall.findById(orderid);
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_DataCall by id: {}", orderid, ex);

			return null;
		}
	}

	@Override
	public int getRecordCount(String topic_id) {
		
		switch (topic_id) {
		
		case "from_clcc_hcepcalldt_message": {
			return repositoryDataCall.countBy();
		}case "from_clcc_mcepcalldt_message": {
			return repositoryMDataCall.countBy();
		}case "from_clcc_hcepcalldtcust_message": {
			return repositoryDataCallCustomer.countBy();
		}case "from_clcc_mcepcalldtcust_message": {
			return repositoryMDataCallCustomer.countBy();
		}case "from_clcc_hcepcallsvccd_message": {
			return repositoryDataCallService.countBy();
		}case "from_clcc_mcepcallsvccd_message": {
			return repositoryMDataCallService.countBy();
		}case "from_clcc_hcepcallmstrsvccd_message": {
			return repositoryMasterServiceCode.countBy();
		}case "from_clcc_mcepcallmstrsvccd_message": {
			return repositoryMMasterServiceCode.countBy();
		}case "from_clcc_hcepwacalldt_message": {
			return repositoryWaDataCall.countBy();
		}case "from_clcc_mcepwacalldt_message": {
			return repositoryMWaDataCall.countBy();
		}case "from_clcc_hcepwacallopt_message": {
			return repositoryWaDataCallOptional.countBy();
		}case "from_clcc_mcepwacallopt_message": {
			return repositoryMWaDataCallOptional.countBy();
		}case "from_clcc_hcepwacalltr_message": {
			return repositoryWaDataCallTrace.countBy();
		}case "from_clcc_mcepwacalltr_message": {
			return repositoryMWaDataCallTrace.countBy();
		}case "from_clcc_hcepwatrcd_message": {
			return repositoryWaMTraceCode.countBy();
		}case "from_clcc_mcepwatrcd_message": {
			return repositoryMWaMTraceCode.countBy();
		}
		
		default:
			return 0;
		}
	}

	
	@Override
    public <T> List<T> getAll(Class<T> clazz) {
        if (clazz.isAssignableFrom(Entity_DataCall.class)) {
            return (List<T>) repositoryDataCall.findAll();
        }else if (clazz.isAssignableFrom(Entity_MDataCall.class)) {
            return (List<T>) repositoryMDataCall.findAll();
        }else if (clazz.isAssignableFrom(Entity_DataCallCustomer.class)) {
            return (List<T>) repositoryDataCallCustomer.findAll();
        }else if (clazz.isAssignableFrom(Entity_MDataCallCustomer.class)) {
            return (List<T>) repositoryMDataCallCustomer.findAll();
        }else if (clazz.isAssignableFrom(Entity_DataCallService.class)) {
            return (List<T>) repositoryDataCallService.findAll();
        }else if (clazz.isAssignableFrom(Entity_MDataCallService.class)) {
            return (List<T>) repositoryMDataCallService.findAll();
        }else if (clazz.isAssignableFrom(Entity_MasterServiceCode.class)) {
            return (List<T>) repositoryMasterServiceCode.findAll();
        }else if (clazz.isAssignableFrom(Entity_MMasterServiceCode.class)) {
            return (List<T>) repositoryMMasterServiceCode.findAll();
        }else if (clazz.isAssignableFrom(Entity_WaDataCall.class)) {
            return (List<T>) repositoryWaDataCall.findAll();
        }else if (clazz.isAssignableFrom(Entity_MWaDataCall.class)) {
            return (List<T>) repositoryMWaDataCall.findAll();
        }else if (clazz.isAssignableFrom(Entity_WaDataCallOptional.class)) {
            return (List<T>) repositoryWaDataCallOptional.findAll();
        }else if (clazz.isAssignableFrom(Entity_MWaDataCallOptional.class)) {
            return (List<T>) repositoryMWaDataCallOptional.findAll();
        }else if (clazz.isAssignableFrom(Entity_WaDataCallTrace.class)) {
            return (List<T>) repositoryWaDataCallTrace.findAll();
        }else if (clazz.isAssignableFrom(Entity_MWaDataCallTrace.class)) {
            return (List<T>) repositoryMWaDataCallTrace.findAll();
        }else if (clazz.isAssignableFrom(Entity_WaMTracecode.class)) {
            return (List<T>) repositoryWaMTraceCode.findAll();
        }else if (clazz.isAssignableFrom(Entity_MWaMTracecode.class)) {
            return (List<T>) repositoryMWaMTraceCode.findAll();
        }
        
        
        return null; 
    }
	
	
	

}
