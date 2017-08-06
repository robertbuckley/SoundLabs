import java.awt.Color;
import java.awt.Graphics;

public class Sinusoidal implements CanvasPart{
	private double x;
	private double y;
	final static int HEIGHT = 265;
	final static int WIDTH = 900;
	private double frequency;
	private double amplitude;
	private double xShift;
	private double waveLength;
	/**
	 * @param x the x location of the graph's center
	 * @param y the y location of the graph's center
	 * @param frequency the number of cycles per min
	 * @param height the height of the box
	 */
	public Sinusoidal(double x, double y, double frequency, double amplitude, double xShift){
		this.x = x;
		this.y = y;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.xShift= xShift;
		this.waveLength = 343.0 / frequency; 
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void paint(Graphics g, int yShift) {
		int span = WIDTH;
		for(double i = 0.0; i < span; i += 1){
			g.setColor(Color.WHITE);
			//System.out.println(amplitude);
			g.fillOval((int)(x - WIDTH / 2 + i) , (int)(y + yShift + amplitude * Math.sin(2 * Math.PI * frequency * i / 343.0 - xShift)), 4, (int)(4 * Math.log(Math.abs(amplitude) + 4)));
		}
	}

	@Override
	public boolean isClicked(double x, double y, int yShift) {
		return false;
	}

	@Override
	public void onClick(double x, double y, int yShift) {
		
	}

	public void setFrequency(double frequency){
		this.frequency = frequency;
		this.waveLength = 343.0 / frequency; 
	}
	
	public void setAmplitude(double amplitude){
		this.amplitude = amplitude;
	}
	
	public void setXShift(double xShift){
		this.xShift = xShift;
	}
	
	public double getValue(double x){
		return amplitude * Math.sin(2 * Math.PI * frequency * x / 343.0 - xShift);
	}
	
}
