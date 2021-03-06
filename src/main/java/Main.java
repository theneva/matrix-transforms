import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.*;

public class Main {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {

        final UI ui = new UI();

        final Timer creationTimer = new Timer();
        creationTimer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    ui.liams.add(new Vector(0, 100));
                }
            },
            TimeUnit.SECONDS.toMillis(0),
            200
        );

        new Timer().schedule(
            new TimerTask() {
                @Override
                public void run() {
                    creationTimer.cancel();
                }
            },
            TimeUnit.SECONDS.toMillis(20)
        );

        new Timer().scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    ui.repaint();
                }
            },
            0,
            1000 / 60 // 60 fps
        );

        new Timer().scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    ui.liams = ui.liams.stream()
                    .map(liam -> {
                        final Vector rotated = new Transform(liam).rotate(-Math.PI / 100);
                        return rotated.add(
                            new Transform(new Vector(Math.cos(rotated.angle()), Math.sin(rotated.angle())))
                                .scale(1)
                        );
                    })
                    .collect(Collectors.toList());
                }
            },
            0,
            100
        );

        final JFrame frame = new JFrame("not a cardioid");
        frame.setSize(WIDTH, HEIGHT);
        frame.setContentPane(ui);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

class UI extends JPanel {
    public List<Vector> liams = new ArrayList<>();

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        final Vector dimensions = new Vector(getWidth(), getHeight());
        final Vector origin = new Vector(dimensions.x / 2, dimensions.y / 2);

        // x axis
        g2.drawLine(
            0,
            (int) origin.y,
            (int) dimensions.x,
            (int) origin.y
        );

        // y axis
        g2.drawLine(
            (int) origin.x,
            0,
            (int) origin.x,
            (int) dimensions.y
        );

        // liam
        if (liams == null) {
            return;
        }

        liams.forEach(liam -> {
            g.setColor(new Color(
                (int) (Math.abs(Math.cos(liam.angle()) * 255)),
                (int) Math.abs((Math.sin(liam.angle()) * 255)),
                (int) Math.abs((Math.sin(liam.angle()) * Math.cos(liam.angle()) * 255))
            ));

            final Vector liamDestination = origin.add(liam);
            g.drawLine(
                (int) origin.x,
                (int) origin.y,
                (int) liamDestination.x,
                (int) liamDestination.y
            );
        });
    }
}

class Vector {
    public final double x;
    public final double y;

    public Vector(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(final Vector other) {
        this(other.x, other.y);
    }

    public Vector add(final Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public double angle() {
        return Math.atan(y / x);
    }

    public double length() {
        return Math.pow(Math.sqrt(x + y), 2);
    }

    @Override
    public String toString() {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}

class Matrix {
    public final double x1;
    public final double x2;
    public final double y1;
    public final double y2;

    public Matrix(
        final double x1,
        final double y1,
        final double x2,
        final double y2
    ) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Vector multiply(final Vector vector) {
        return new Vector(
            x1 * vector.x + y1 * vector.y,
            x2 * vector.x + y2 * vector.y
        );
    }
}

class Transform {
    private final Vector vector;

    public Transform(final Vector vector) {
        this.vector = vector;
    }

    public Vector rotate(final double angle) {
        return new Matrix(
            Math.cos(angle),
            Math.sin(angle),
            -Math.sin(angle),
            Math.cos(angle)
        )
            .multiply(vector);
    }

    public Vector scale(final double factor) {
        return new Matrix(
            factor,
            0,
            0,
            factor
        )
            .multiply(vector);
    }
}
