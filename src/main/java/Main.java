import java.awt.*;
import java.util.TimerTask;
import javax.swing.*;

public class Main {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {

        final UI ui = new UI();
        ui.setLiam(new Vector(100, 100));

        new java.util.Timer().scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    ui.repaint();
                }
            },
            0,
            500
        );
        ui.repaint();

        final JFrame frame = new JFrame("canvas");
        frame.setSize(WIDTH, HEIGHT);
        frame.setContentPane(ui);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

class UI extends JPanel {
    private Vector liam;

    public void setLiam(final Vector liam) {
        this.liam = liam;
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        final Vector dimensions = new Vector(getWidth(), getHeight());
        final Vector origin = new Vector(dimensions.x / 2, dimensions.y / 2);

        // x axis
        g2.drawLine(0, origin.y, dimensions.x, origin.y);

        // y axis
        g2.drawLine(origin.x, 0, origin.x, dimensions.y);

        // liam
        if (liam == null) {
            return;
        }

        g.setColor(Color.RED);
        final Vector liamDestination = origin.add(liam);
        g.drawLine(origin.x, origin.y, liamDestination.x, liamDestination.y);
    }
}

class Vector {
    public final int x;
    public final int y;

    public Vector(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(final Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    @Override
    public String toString() {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
