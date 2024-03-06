package gc.apiClient.entity;

public class Entity_CampRtJson {
	
	private int rlsq;
	private int cpsq;
	private int hubid;
	private int dirt;
	private int dict;
	private String tkda;
	private String coid;
	private String cpid;
	private String contactLtId;
 	private String contactid;
 	private String didt;
	

	public Entity_CampRtJson() {
	}

	public Entity_CampRtJson(String cpid,int cpsq, String contactLtId, String contactid, int hubid, String didt,
			int dirt, int dict,String coid,int rlsq,String tkda) {
		this.cpid = cpid;
		this.cpsq = cpsq;
		this.contactLtId = contactLtId;
		this.contactid = contactid;
		this.hubid = hubid;
		this.didt = didt;
		this.dirt = dirt;
		this.dict = dict;
		this.coid = coid;
		this.tkda = tkda;
	}

	public String getCpid() { return cpid; }
	
	public int getCpsq() {return cpsq;}
	public int getHubId() {return hubid;}
	public int getDict() {return dict;}
	public int getRlsq() { return rlsq; }
	public int getDirt() {return dirt;}
	public String getContactLtId() {return contactLtId;}
	public String getContactId() {return contactid;}
	public String getDidt() {return didt;}
	public String getCoid() { return coid; }
	public String getTkda() { return tkda; }
	
	
	public void setCpid(String cpid) {this.cpid = cpid;}
	public void setTkda(String tkda) {this.tkda = tkda;}
	public void setCpsq(int cpsq) {this.cpsq = cpsq;}
	public void setContactLtId(String contactLtId) {this.contactLtId = contactLtId;}
	public void setContactId(String contactid) {this.contactid = contactid;}
	public void setHubId(int hubid) {this.hubid = hubid;}
	public void setDidt(String didt) {this.didt = didt;}
	public void setDirt(int dirt) {this.dirt = dirt;}
	public void setDict(int dict) {this.dict = dict;}
	public void setCoid(String coid) {this.coid = coid;}
	public void setRlsq(int rlsq) {this.rlsq = rlsq;}
}
	
