package gc.apiClient.entity;


public class Entity_ContactltMapper {
	
	private String cpid;
	private String cpsq;
 	private String cske;
	private String tno1;
	private String tno2;
	private String tno3;
	private String tmzo;
	private String csna;
	private String tkda;

	public Entity_ContactltMapper() {
	}

	public Entity_ContactltMapper(
			String cpid,
			String cpsq,
			String cske,
			String tno1,
			String tno2,
			String tno3,
			String tmzo,
			String csna,
			String tkda) {
		
		this.cpid =cpid;
		this.cpsq =cpsq;
		this.cske =cske;
		this.tno1 =tno1;
		this.tno2 =tno2;
		this.tno3 =tno3;
		this.tmzo =tmzo;
		this.csna =csna;
		this.tkda =tkda;
		
	}

	public String getCpid() {return cpid;}
	public String getCpsq() {return cpsq;}
	public String getCske() {return cske;}
	public String getTno1() {return tno1;}
	public String getTno2() {return tno2;}
	public String getTno3() {return tno3;}
	public String getTmzo() {return tmzo;}
	public String getCsna() {return csna;}
	public String getTkda() {return tkda;}
	
	public void setCpid(String cpid) {this.cpid= cpid;}
	public void setCpsq(String cpsq) {this.cpsq= cpsq;}
	public void setCske(String cske) {this.cske= cske;}
	public void setTno1(String tno1) {this.tno1= tno1;}
	public void setTno2(String tno2) {this.tno2= tno2;}
	public void setTno3(String tno3) {this.tno3= tno3;}
	public void setTmzo(String tmzo) {this.tmzo= tmzo;}
	public void setCsna(String csna) {this.csna= csna;}
	public void setTkda(String tkda) {this.tkda= tkda;}
	
	
}
	
