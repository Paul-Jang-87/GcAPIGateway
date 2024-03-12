package gc.apiClient.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import gc.apiClient.entity.oracle.Entity_DataCall;
import gc.apiClient.entity.oracle.Entity_WaDataCallOptional;
import gc.apiClient.entity.postgresql.Entity_CampMa;
import gc.apiClient.interfaceCollection.InterfaceDBOracle;
import gc.apiClient.repository.oracle.Repository_DataCall;
import gc.apiClient.repository.oracle.Repository_DataCallCustomer;
import gc.apiClient.repository.oracle.Repository_DataCallService;
import gc.apiClient.repository.oracle.Repository_MDataCall;
import gc.apiClient.repository.oracle.Repository_MDataCallCustomer;
import gc.apiClient.repository.oracle.Repository_MDataCallService;
import gc.apiClient.repository.oracle.Repository_MMasterServiceCode;
import gc.apiClient.repository.oracle.Repository_MWaDataCall;
import gc.apiClient.repository.oracle.Repository_MWaDataCallOptional;
import gc.apiClient.repository.oracle.Repository_MWaDataCallTrace;
import gc.apiClient.repository.oracle.Repository_MWaMTraceCode;
import gc.apiClient.repository.oracle.Repository_MasterServiceCode;
import gc.apiClient.repository.oracle.Repository_WaDataCall;
import gc.apiClient.repository.oracle.Repository_WaDataCallOptional;
import gc.apiClient.repository.oracle.Repository_WaDataCallTrace;
import gc.apiClient.repository.oracle.Repository_WaMTraceCode;
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
	public Entity_DataCall findDataCallByCpid(Entity_DataCall enDataCall) {

		try {
			Optional<Entity_DataCall> optionalEntity = repositoryDataCall.findById(enDataCall.getId());
			return optionalEntity.orElse(null);
		} catch (IncorrectResultSizeDataAccessException ex) {
			log.error("Error retrieving Entity_DataCall by id: {}", enDataCall.getId(), ex);

			return null;
		}
	}
	

}
