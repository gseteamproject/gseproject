package task3;

public class Palette {

	private int status;
	private int id;
	
	public Palette(int id,int status) {
		this.id=id;
		this.status=status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
}
