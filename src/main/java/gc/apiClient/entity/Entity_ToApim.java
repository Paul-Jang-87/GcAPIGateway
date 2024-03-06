package gc.apiClient.entity;

public class Entity_ToApim {
	

	private int dirt;
 	private String tkda;

	public Entity_ToApim() {
	}

	public Entity_ToApim(int dirt, String tkda) {
		this.dirt = dirt;
		this.tkda = tkda;
	}


	public int getDirt() { return dirt; }
	public String getTkda() {return tkda;}

	public void setDirt(int dirt) {this.dirt = dirt;}
	public void setTkda(String tkda) {this.tkda = tkda;}
	
}
	
