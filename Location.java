
public class Location{
	private int c; //col //x
	private int r; //row //y
	public Location(int c, int r){
		this.c = c;
		this.r = r;
	}
	public int getC(){
		return c;
	}
	public int getR(){
		return r;
	}
	public void setC(int cNew){
		c+=cNew;
	}
	public void setR(int rNew){
		r+=rNew;
	}

}