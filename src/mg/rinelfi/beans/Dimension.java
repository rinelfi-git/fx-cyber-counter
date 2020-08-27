package mg.rinelfi.beans;

public class Dimension {
	private int width;
	private int height;
	public Dimension() {
		setWidth(0);
		setHeight(0);
	}
	public Dimension(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
