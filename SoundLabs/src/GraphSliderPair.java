import java.awt.Graphics;

public class GraphSliderPair implements CanvasPart{
	private Sinusoidal s;
	private Slider amplitude;
	private Slider frequency;
	private Slider xShift;
	private int x;
	private int y;
	private int widthSlider;
	final double SPACE = 5;
	public GraphSliderPair(int x, int y, int widthSlider){
		this.x = x;
		this.y = y;
		this.widthSlider = widthSlider;
		s = new Sinusoidal(x + Sinusoidal.WIDTH / 2, y + Sinusoidal.HEIGHT / 2, 1, 50, 0);
		amplitude = new Slider(x + (Sinusoidal.WIDTH - widthSlider) / 2, y + Sinusoidal.HEIGHT, widthSlider, .75, -100, 100);
		//220 to 830
		frequency = new Slider(x + (Sinusoidal.WIDTH - widthSlider) / 2, y + Sinusoidal.HEIGHT + (double)widthSlider / Slider.RATIO + SPACE, widthSlider, 1, 3.77272);
		xShift = new Slider(x + (Sinusoidal.WIDTH - widthSlider) / 2, y + Sinusoidal.HEIGHT + (double) 2 * (widthSlider / Slider.RATIO + SPACE), widthSlider, 0, 100);
	}

	@Override
	public void update() {
		s.setAmplitude(amplitude.getValue());
		s.setFrequency(frequency.getValue());
		s.setXShift(xShift.getValue());
	}

	@Override
	public void paint(Graphics g, int yShift) {
		s.paint(g, yShift);
		amplitude.paint(g, yShift);
		frequency.paint(g, yShift);
		xShift.paint(g, yShift);
	}

	@Override
	public boolean isClicked(double x, double y, int yShift) {
		if(amplitude.isClicked(x, y, yShift)) amplitude.onClick(x, y, yShift);
		if(frequency.isClicked(x, y, yShift)) frequency.onClick(x, y, yShift);
		if(xShift.isClicked(x, y, yShift)) xShift.onClick(x, y, yShift);
		return true;
	}

	@Override
	public void onClick(double x, double y, int yShift) {
		// TODO Auto-generated method stub
		
	}
	
	public double getHeight(){
		return Sinusoidal.HEIGHT + 3 * (widthSlider / Slider.RATIO + SPACE);
	}
	
	public Sinusoidal getSinusoidal(){
		return s;
	}
	
	public double getFrequency(){
		return frequency.getValue();
	}
	
	public double getAmplitude(){
		return amplitude.getValue();
	}
	
	public double getXShift(){
		return xShift.getValue();
	}
}
