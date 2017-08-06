import java.awt.Color;
import java.awt.Graphics;

public class Slider implements CanvasPart{
	private double x;
	private double y;
	private int width;
	private int height;
	private double place;
	private double start;
	private double end;
	final static int RATIO = 12;
	
	/**
	 * @param x the x location of the left side of the line
	 * @param y the y location of the left side of the line
	 * @param width the width of the line
	 * @param height the height of the box
	 * @param start the value the slider returns when place = 0.0
	 * @param end the value the slider returns when place = 1.0
	 */
	public Slider(double x, double y, int width, double start, double end){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = width / RATIO;
		place = .5;
		this.start = start;
		this.end = end;
	}
	
	public Slider(double x, double y, int width, double place, double start, double end){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = width / RATIO;
		this.place = place;
		this.start = start;
		this.end = end;
	}
	
	public double getValue(){
		return start + place * (end - start);
	}

	@Override
	public void update() {

	}

	@Override
	public void paint(Graphics g, int yShift) {
		g.setColor(Color.BLACK);
		g.drawLine((int)x, (int)y + yShift, (int)(x + width), (int)y + yShift);
		g.setColor(Color.GRAY);
		g.fillRect((int)(x  + width * place - height / 2), (int)(y - height / 2 + yShift), (int)height, (int)height);
	}

	@Override
	public boolean isClicked(double x, double y, int yShift) {
		if(x > this.x && x < this.x + width){
			if(y > this.y - height / 2 + yShift && y < this.y + height / 2 + yShift){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onClick(double x, double y, int yShift) {
		place = (x - this.x) / width;
	}
}
