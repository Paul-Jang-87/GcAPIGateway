package gc.apiClient.entity;

public class Entity_CampRtJson {
	
	private int rlsq;
	private String coid;
	private String cpid;
 	private int cpsq;
	private String contactLtId;
 	private String contactid;
 	private int hubid;
 	private String didt;
	private int dirt;
	private int dict;
	

	public Entity_CampRtJson() {
	}

	public Entity_CampRtJson(String cpid,int cpsq, String contactLtId, String contactid, int hubid, String didt,
			int dirt, int dict,String coid,int rlsq) {
		this.cpid = cpid;
		this.cpsq = cpsq;
		this.contactLtId = contactLtId;
		this.contactid = contactid;
		this.hubid = hubid;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
		this.coid = coid;
	}

	public String getCpid() { return cpid; }
	public void setCpid(String cpid) {this.cpid = cpid;}
	
	public int getCpsq() {return cpsq;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	
	public String getContactLtId() {return contactLtId;}
	public void setContactLtId(String contactLtId) {this.contactLtId = contactLtId;}
	
	public String getContactId() {return contactid;}
	public void setContactId(String contactid) {this.contactid = contactid;}
	
	public int getHubId() {return hubid;}
	public void setHubId(int hubid) {this.hubid = hubid;}
	
	public String getDidt() {return didt;}
	public void setDidt(String didt) {this.didt = didt;}
	
	public int getDirt() {return dirt;}
	public void setDirt(int dirt) {this.dirt = dirt;}
	
	public int getDict() {return dict;}
	public void setDict(int dict) {this.dict = dict;}
	
	public String getCoid() { return coid; }
	public void setCoid(String coid) {this.coid = coid;}
	
	public int getRlsq() { return rlsq; }
	public void setRlsq(int rlsq) {this.rlsq = rlsq;}
}
	
