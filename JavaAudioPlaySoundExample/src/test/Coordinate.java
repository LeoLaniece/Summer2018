package test;

public class Coordinate {
	public double x;
	public double y;
	public long time;
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
		this.time = 0;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double x) {
		this.y = x;
	}
	public void setTime(long t) {
		time = t;
	}
}
