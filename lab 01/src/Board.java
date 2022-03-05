import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

/**
 * Board with Points that may be expanded (with automatic change of cell
 * number) with mouse event listener
 */

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 14;
	private SimulationMode currentMode;
	private int length;
	private int height;

	public Board(int length, int height, SimulationMode currentMode) {
		this.currentMode = currentMode;
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	// single iteration
	public void iteration() {
		for (int x = 0; x < points.length; ++x) {
			points[x][0].drop();
		}

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y].calculateNewState(currentMode);

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y].changeState();
		this.repaint();
	}

	// clearing board
	public void clear() {
		initialize();
		this.repaint();
	}

	public void changeMode(SimulationMode mode) {
		currentMode = mode;
		initialize();
	}

	public void initialize() {
		points = new Point[length][height];

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y] = new Point();

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				switch (currentMode) {
					case GameOfLife:
						for (int ox = -1; ox <= 1; ++ox) {
							for (int oy = -1; oy <= 1; ++oy) {
								if (ox != 0 || oy != 0) {
									points[x][y].addNeighbor(
											points[getPeriodicNeighbour(x, ox, points.length)][getPeriodicNeighbour(y, oy, points[x].length)]
									);
								}
							}
						}
						break;
					case Rain:
						if (y != 0)
							points[x][y].addNeighbor(points[x][y - 1]);
				}



			}
		}
	}

	//paint background and separators between cells
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	// draws the background netting
	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				if (points[x][y].getState() != 0) {

					switch (currentMode) {
						case GameOfLife -> {
							switch (points[x][y].getState()) {
								case 1 -> g.setColor(new Color(0x0000ff));
								case 2 -> g.setColor(new Color(0x00ff00));
								case 3 -> g.setColor(new Color(0xff0000));
								case 4 -> g.setColor(new Color(0x000000));
								case 5 -> g.setColor(new Color(0x444444));
								case 6 -> g.setColor(new Color(0xffffff));
							}
						}
						case Rain -> {
							switch (points[x][y].getState()) {
								case 1 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.16f));
								case 2 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.33f));
								case 3 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.50f));
								case 4 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.66f));
								case 5 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.84f));
								case 6 -> g.setColor(new Color(0.0f, 0.0f, 1.0f, 1.0f));
							}
						}
					}
					g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
				}
			}
		}

	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			points[x][y].clicked();
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		length = (this.getWidth() / size) + 1;
		height = (this.getHeight() / size) + 1;
		initialize();
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			points[x][y].setState(1);
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	private int getPeriodicNeighbour(int x, int ox, int size) {
		if (x == 0 && ox == -1)
			return size - 1;
		if (x == size - 1 && ox == 1)
			return 0;
		return x + ox;
	}

}
