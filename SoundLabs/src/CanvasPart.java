import java.awt.Graphics;

public interface CanvasPart {
	void update();
	void paint(Graphics g, int yShift);
	boolean isClicked(double x, double y, int yShift);
	void onClick(double x, double y, int yShift);
}
