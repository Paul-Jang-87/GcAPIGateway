package gc.apiClient.entity.postgresql;

import java.time.LocalDateTime;

import gc.apiClient.embeddable.ContactLtId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CONTACTLT")

/**
 * UCRM,CallBot,APIM 아웃바운드 발신 대상자들의 정보들이 쌓인다.
 * UCRM과 CallBot 로직은 카프카로부터 발신대상자 정보 관련 메시지를 받아 DB에 쌓는다. 
 * 하지만 UCRM과 CallBot로직이 살짝 다른 부분이 있다. CallBot로직은 메시지를 받고 'CONTACTLT' 직접적으로 쌓지만
 * UCRM로직은 'UCRMLT' 테이블은 한 번 거친다. 프로세스로 설명하자면 카프카 -> UCRMLT 테이블 -> CONTACTLT 테이블
 * 카프카에서 'UCRMLT'쉐도우 테이블에 발신 대상자 리스트를 건건이 쌓으면 1분마다 'ControllerUCRM'에서 쌓이 데이터들을 가져와
 * 제네시스로 보낸 후 여기 'CONTACTLT'테이블에 쌓는다.
 */


public class Entity_ContactLt {
	
	@EmbeddedId
    private ContactLtId id;
	
	@Column(name = "CSKE")
 	private String cske;

	@Column(name = "TNO1")
	private String tno1;

	@Column(name = "TNO2")
	private String tno2;
	
	@Column(name = "TNO3")
	private String tno3;
	
	@Column(name = "CSNA")
	private String csna;
	
	@Column(name = "TKDA")
	private String tkda;
	
	@Column(name = "FLAG")
	private String flag;
	
	@Column(name = "DATE")
	private LocalDateTime date;
	
	public Entity_ContactLt(){
		
	}
	
}
	
