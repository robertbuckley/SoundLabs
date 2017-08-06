import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SoundLab extends Canvas implements Runnable, MouseListener, MouseMotionListener, MouseWheelListener {
	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	public static final String NAME = "SoundLab: Robert Buckley";
	public static final double FPS = 60.0;
	private boolean running = false;
	private Thread thread;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private Image plus;
	private Image play;
	private Image refresh;
	private int yShift;
	private static JFrame frame;
	private ArrayList<GraphSliderPair> graphs;
	public static void main(String[] args){
		SoundLab s = new SoundLab();
		s.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		s.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		s.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame = new JFrame(NAME);
		frame.add(s);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		s.start();
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		while(running){
			paint();
			long currentTime = System.currentTimeMillis();
			if(currentTime - lastTime > 1000.0 / FPS){
				update();
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	private synchronized void start(){
		if(running) return;
		initialize();
		running  = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void update(){
		for(int i = 0; i < graphs.size(); ++i){
			graphs.get(i).update();
		}
	}
	
	private synchronized void initialize(){
		try {
			loadResources(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		yShift = 0;
		graphs = new ArrayList<GraphSliderPair>();
		graphs.add(new GraphSliderPair(0, 0, getWidth() / 4));
	}
	
	
	private synchronized void resetGraph(){
		yShift = 0;
		graphs = new ArrayList<GraphSliderPair>();
		graphs.add(new GraphSliderPair(0, 0, getWidth() / 4));
	}
	private synchronized void paint(){
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null){
			createBufferStrategy(2);
			return;
		}
		Graphics g = strat.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		for(int i = 0; i < graphs.size(); ++i){
			graphs.get(i).paint(g, yShift);
		}
		double max = 0;
		for(double i = 0.0; i < 900; i += 1){
			g.setColor(new Color(252, 75, 90));
			double y = 0;
			for(int j = 0; j < graphs.size(); ++j){
				y += graphs.get(j).getSinusoidal().getValue(i);
			}
			if(y > max) max = y;
		}
		for(double i = 0.0; i < 900; i += 1){
			g.setColor(new Color(252, 75, 90));
			double y = 0;
			for(int j = 0; j < graphs.size(); ++j){
				y += graphs.get(j).getSinusoidal().getValue(i);
			}
			g.fillOval((int)(i) , (int)(graphs.size() * graphs.get(0).getHeight() + max + y + yShift), 4, (int)(4*Math.log(Math.abs(max) + 4)));
		}
		g.drawImage(plus, getWidth() - getWidth() / 23 - 10, getHeight() - getWidth() / 23 - 10, getWidth() / 23, getWidth() / 23, this);
		g.drawImage(play, getWidth() - getWidth() / 23 - 10, getHeight() - getWidth() / 23 - getWidth() / 23 - 20, getWidth() / 23, getWidth() / 23, this);
		g.drawImage(refresh, getWidth() - getWidth() / 23 - 10, getHeight() - getWidth() / 23 - getWidth() / 23 - 30 - getWidth() / 23, getWidth() / 23, getWidth() / 23, this);
		g.dispose();
		strat.show();
	}
	
	private synchronized void stop() throws InterruptedException{
		if(!running) return;
		running = false;
		thread.join();
		System.exit(1);
	}

	
	private synchronized void loadResources(JFrame frame) throws Exception{
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/icon.png")));
		plus = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/plus.png"));
		play = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/play.png"));
		refresh = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/refresh.png"));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(int i = 0; i < graphs.size(); ++i){
			graphs.get(i).isClicked(e.getX(), e.getY(), yShift);	
		}
		if(e.getX() > getWidth() - getWidth() / 23 - 10 && e.getX() < getWidth() - 10){
			if(e.getY() > getHeight() - getWidth() / 23 - 10 && e.getY() < getHeight() - 10){
				graphs.add(new GraphSliderPair(0, (int)(graphs.size() * graphs.get(0).getHeight()), getWidth() / 4));
			}
		}
		if(e.getX() > getWidth() - getWidth() / 23 - 10 && e.getX() < getWidth() - 10){
			if(e.getY() > getHeight() - 2 * (getWidth() / 23) - 24 && e.getY() < getHeight() - getWidth() / 23 - 20){
				Beep toPlay;
				ArrayList<Double> freq = new ArrayList<Double>();
				ArrayList<Double> xShift = new ArrayList<Double>();
				ArrayList<Double> volume = new ArrayList<Double>();
				for(int i = 0; i < graphs.size(); ++i){
					 freq.add(graphs.get(i).getFrequency() * 220.0);
					 xShift.add(graphs.get(i).getXShift());
					 volume.add((graphs.get(i).getAmplitude() / 100) * 1);
				}
				toPlay = new Beep(freq, xShift, volume, 1000);
			}
		}
		if(e.getX() > getWidth() - getWidth() / 23 - 10 && e.getX() < getWidth() - 10){
			if(e.getY() > getHeight() - 3 * (getWidth() / 23) - 34 && e.getY() < getHeight() - getWidth() / 23 - 30 - getWidth() / 23){
				resetGraph();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for(int i = 0; i < graphs.size(); ++i){
			graphs.get(i).isClicked(e.getX(), e.getY(), yShift);	
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(yShift == 0){
			if(e.getWheelRotation() > 0) yShift += -25 * e.getWheelRotation();
		}
		else if(yShift <= -25){
			yShift += -25 * e.getWheelRotation();
		}
	}
}
