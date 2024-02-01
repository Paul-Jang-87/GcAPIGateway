package kafka.gcClient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "APPCONFIG")
public class Entity_AppConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msg_id")
	private Long id;

	@Column(name = "GC_CLIENT_ID")
	private String gc_client_id;

	@Column(name = "GC_CLIENT_SECRET")
	private String gc_client_secret;
	
	@Column(name = "SASL_ID")
 	private String sasl_id;
    
	@Column(name = "SASL_PWD")
	private String sasl_pwd;

	@Column(name = "APIM_CLIENT_ID")
	private String apim_client_id;
	
	@Column(name = "APIM_CLENT_SECRET")
	private String apim_clent_secret;
	

	public Entity_AppConfig() {
	}

	public Entity_AppConfig(
			Long id, String gc_client_id,     
			String gc_client_secret, 
			String sasl_id,          
			String sasl_pwd,         
			String apim_client_id,   
			String apim_clent_secret) {
		
		this.id = id;
		this.gc_client_id = gc_client_id;
		this.gc_client_secret = gc_client_secret;
		this.sasl_id = sasl_id;       
		this.sasl_pwd =  sasl_pwd;     
		this.apim_client_id =  apim_client_id;
		this.apim_clent_secret = apim_clent_secret;
		
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	
	public String getGcClientId() { return gc_client_id; }
	public void setGcClientId(String gc_client_id) {this.gc_client_id = gc_client_id;}
	
	
	public String getGcClientSecret() { return gc_client_secret; }
	public void setGcClientSecret(String gc_client_secret) {this.gc_client_secret = gc_client_secret;}

	
	public String getSaslId() {return sasl_id;}
	public void setSaslId(String sasl_id) {this.sasl_id = sasl_id;}
	
	
	public String getSaslPwd() {return sasl_pwd;}
	public void setSaslPwd(String sasl_pwd) {this.sasl_pwd = sasl_pwd;}
	
	
	public String getApimClId() {return apim_client_id;}
	public void setApimClId(String apim_client_id) {this.apim_client_id = apim_client_id;}
	
	
	public String getApimClSecret() {return apim_clent_secret;}
	public void setApimClSecret(String apim_clent_secret) {this.apim_clent_secret = apim_clent_secret;}
	
	
}
	
