import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZoomPane extends JPanel
{
    private Rectangle rect;
    private Fractal fractal;
    private JPanel overFractal;

    public ZoomPane(Fractal fractal){
        this.fractal = fractal;
        setFocusable(false);
        setOpaque(false);

        init();
        setVisible(true);
    }

    private void init()
    {
        overFractal = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
                g2.setStroke(new BasicStroke(2));
                g2.setPaint(Color.WHITE);

                if(rect!=null) {
                    g2.draw(rect);
                    g2.fill(rect);
                }
            }
        };
        add(overFractal);
        overFractal.setOpaque(false);
        overFractal.setVisible(true);
        MouseAdapter ml =new ZoomListener();
        overFractal.addMouseListener(ml);
        overFractal.addMouseMotionListener(ml);
    }

    public void doLayout()
    {
        Point p = fractal.getLocation();
        SwingUtilities.convertPoint(fractal, p, this);
        overFractal.setLocation(p);
        overFractal.setSize(fractal.getSize());
    }

    private class ZoomListener extends MouseAdapter
    {
        private Point startDrag, endDrag;

        /*
         * on mouse click show clicked coordinates
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            Complex c = fractal.getComplex(e.getX(), e.getY());
            fractal.getGUI().getSettings().updatePointLabel(c);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startDrag = new Point(e.getX(), e.getY());
            endDrag = startDrag;
            setVisible(true);
        }

        /*
         * when mouse is released, zoom if it was dragged
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (endDrag != startDrag) {
                alignValues();
                //new Thread(new Fractal.ZoomRun(startDrag,endDrag).start();
                fractal.getGUI().getSettings().updateSet();
            }
            setVisible(false);
            startDrag = null;
            endDrag = null;
            rect = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            setVisible(true);
            endDrag = new Point(e.getX(), e.getY());

            //set rectangle constants
            int width = Math.abs(startDrag.x - endDrag.x);
            int height = Math.abs(startDrag.y - endDrag.y);
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);

            rect = new Rectangle(x, y, width, height);
            overFractal.repaint();
        }

        //make sure the rectangle startDrag is top left
        private void alignValues() {
            Point tmp = new Point();

            if (startDrag.x > endDrag.x) {
                tmp.x = endDrag.x;
                endDrag.x = startDrag.x;
                startDrag.x = tmp.x;
            }

            if (startDrag.y > endDrag.y) {
                tmp.y = endDrag.y;
                endDrag.y = startDrag.y;
                startDrag.y = tmp.y;
            }
        }
    }
}
