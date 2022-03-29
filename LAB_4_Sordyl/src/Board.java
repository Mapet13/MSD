import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private int size = 25;
    public int editType = 1;

    int height;

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        this.height = height;

        Point.MAX_POS = length;

        forEachPoint((x, y) -> points[x][y] = new Point(isRoad(y, height) ? Point.Type.EMPTY : Point.Type.GRASS));
        forEachPoint((x, y) -> points[x][y].next = points[x == points.length - 1 ? 0 : x + 1][y]);
    }

    public void iteration() {
        forEachPoint((x, y) -> points[x][y].reset());
        
        forEachPoint((x, y) -> points[x][y].set_next(x, y, points));
        forEachPoint((x, y) -> points[x][y].accelerate());
        forEachPoint((x, y) -> points[x][y].move());

        this.repaint();
    }

    public void clear() {
        forEachPoint((x, y) -> { if (isRoad(y, height)) points[x][y].clear(); });
        this.repaint();
    }


    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
        }

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
                float a = 1.0F;


                g.setColor(
                        switch(points[x][y].type) {
                            case EMPTY -> new Color(1.0f, 1.0f, 1.0f, 0.7f);
                            case CAR_FAST -> new Color(0.5f, 0.0f, 0.0f, 0.7f);
                            case CAR_SLOW -> new Color(0.6f, 0.4f, 0.0f, 0.7f);
                            case CAR_REGULAR -> new Color(0.0f, 0.0f, 0.5f, 0.7f);
                            case GRASS -> new Color(0.0f, 0.5f, 0.0f, 0.7f);
                        }
                );

                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0) && isRoad(y, height)) {
            points[x][y].clicked(Point.Type.fromInt(editType));
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0) && isRoad(y, height)) {
            points[x][y].clicked(Point.Type.fromInt(editType));
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

    public void forEachPoint(BiConsumer<Integer,Integer> function) {
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                function.accept(x, y);
            }
        }
    }

    private boolean isRoad(int y, int height) {
        return (height / 2) - 1 <= y && y <= (height / 2) ;
    }

}
