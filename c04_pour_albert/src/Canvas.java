import fill.SeedFiller;
import model.Point;
import model.Line;
import rasterize.*;
import model.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * trida pro kresleni na platno: vyuzita tridy RasterBufferedImage
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class Canvas {

    private JPanel panel;
    private JTextField clickModeText;
    private JTextField dotModeText;
    private RasterBufferedImage raster;
    private FilledLineRasterizer filledLineRasterizer;
    private ShiftFilledLineRasterizer shiftFilledLineRasterizer;
    private Point p1, p2, lastP;
    private Polygon polygon;
    private PolygonRasterizer polygonRasterizer;
    private boolean clickMode = true;
    private boolean dotMode = false;
    private boolean firstPoint = true;
    private boolean shiftPressed = false;


    public Canvas(int width, int height) {


        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Inicializace textového pole
        clickModeText = new JTextField("Click mode Activated", 20);
        clickModeText.setEditable(false);
        clickModeText.setBackground(Color.black);
        clickModeText.setForeground(Color.white);

        dotModeText = new JTextField("Dot mode Deactivated", 20);
        dotModeText.setEditable(false);
        dotModeText.setBackground(Color.black);
        dotModeText.setForeground(Color.white);

        raster = new RasterBufferedImage(width, height);

        filledLineRasterizer = new FilledLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(filledLineRasterizer);

        polygon = new Polygon();

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.add(clickModeText);
        panel.add(dotModeText);
        panel.setFocusable(true);

        //Zachytávání události pro myš
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                raster.clear();
                if (e.getButton() == MouseEvent.BUTTON3){
                    SeedFiller seedFiller= new SeedFiller(raster, new Color(raster.getColor()).getRGB(), e.getX(), e.getY());
                    seedFiller.fill();
                }
                if (e.getButton() == MouseEvent.BUTTON1){
                    //Zjišujeme zda je aktivovanı klikací mód
                    if (clickMode) {
                        raster.clear();
                        p1 = new Point(e.getX(), e.getY());
                        polygon.addPoint(p1);
                        polygonRasterizer.rasterize(polygon);
                        if (!firstPoint){
                            Line line = new Line(lastP, p1, 0xffff00);
                            filledLineRasterizer.rasterize(line);
                        }
                        firstPoint = false;
                        lastP = p1;
                        panel.repaint();
                    }
                    if (!clickMode) {
                        if (firstPoint) {
                            p1 = new Point(e.getX(), e.getY());
                            polygon.addPoint(p1);
                            firstPoint = !firstPoint;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!clickMode) {
                    lastP = p2;
                    polygon.addPoint(lastP);
                    polygonRasterizer.rasterize(polygon);
                    panel.repaint();
                }
            }
        });
        //Zachytávání události pro pohyb myši
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!clickMode) {
                    raster.clear();
                    p2 = new Point(e.getX(), e.getY());

                    if (lastP != null) {
                        if (!dotMode){
                            Line line = new Line(lastP, p2, 0xffff00);
                            filledLineRasterizer.rasterize(line);
                        } else{
                            Line line = new Line(lastP, p2, 0xffff00);
                            filledLineRasterizer.dotRasterize(line);
                        }
                    } else {
                        if (!dotMode){
                            Line line = new Line(p1, p2, 0xffff00);
                            filledLineRasterizer.rasterize(line);
                        } else {
                            Line line = new Line(p1, p2, 0xffff00);
                            filledLineRasterizer.dotRasterize(line);
                        }
                    }
                    panel.repaint();
                }
            }
        });
        //Zachytávání události klávesnici
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearAllObjects();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    clickMode = !clickMode;
                    if (!clickMode) {
                        clickModeText.setText("Drag mode Activated");
                    } else {
                        clickModeText.setText("Click mode Activated");
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    dotMode = !dotMode;
                    if (!dotMode) {
                        dotModeText.setText("Dot mode deactivated");
                    } else {
                        dotModeText.setText("Dot mode activated");
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftPressed = false;
                }
            }
        });
    }

    //Metoda pro vymazání všech objektù
    public void clearAllObjects() {
        clear(0xaaaaaa);
        polygon.clearPolygon();
        firstPoint = true;
        lastP = null;
        p1 = null;
        p2 = null;
        panel.repaint();
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xffffff);
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
    }

}
