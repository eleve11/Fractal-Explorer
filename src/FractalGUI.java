import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * has a JPanel (Image) and 2 textfield to set iterations position
 * also label to show what point was clicked
 */
public abstract class FractalGUI extends JFrame
{
    private Fractal fractal;
    private JPanel settings;
    public static final Dimension DEFAULT_SIZE = new Dimension(750,500);

    public FractalGUI(String title, Fractal fractal,int width,int height)
    {
        super(title);

        this.fractal = fractal;
        this.settings = new SettingsPanel(fractal);
        setFocusable(true);
        this.addKeyListener(new FractKeyLis());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width, height);
    }

    public FractalGUI(String title, Fractal fractal){
        this(title, fractal, DEFAULT_SIZE.width
                ,DEFAULT_SIZE.height);
    }

    public abstract void init();

    public Fractal getFractal() {
        return fractal;
    }

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }

    public SettingsPanel getSettings() {
        return (SettingsPanel) settings;
    }


    private class FractKeyLis extends KeyAdapter
    {
        @Override
        public void  keyPressed(KeyEvent e)
        {
            //shift 5% of current range
            double horizontalShift = (fractal.getRealUp()-fractal.getRealLow()) /20;
            double verticalShift = (fractal.getImagUp()-fractal.getImagLow()) /20;
            int iterationShift = 100;

            switch (e.getKeyCode())
            {
                case KeyEvent.VK_ESCAPE:
                    fractal.setRealLow(Fractal.REAL_LOW);
                    fractal.setRealUp(Fractal.REAL_UP);
                    fractal.setImagLow(Fractal.IMAG_LOW);
                    fractal.setImagUp(Fractal.IMAG_UP);
                    fractal.setMaxIterations(100);
                    getSettings().updateSet();
                    repaint();
                    break;

                case KeyEvent.VK_LEFT:
                    horizontalShift = -horizontalShift;
                case KeyEvent.VK_RIGHT:
                    fractal.setRealLow(fractal.getRealLow() + horizontalShift);
                    fractal.setRealUp(fractal.getRealUp() + horizontalShift);
                    getSettings().updateSet();
                    repaint();
                    break;

                case KeyEvent.VK_DOWN:
                    verticalShift = -verticalShift;
                case KeyEvent.VK_UP:
                    fractal.setImagLow(fractal.getImagLow() + verticalShift);
                    fractal.setImagUp(fractal.getImagUp() + verticalShift);
                    getSettings().updateSet();
                    repaint();
                    break;

                case KeyEvent.VK_SHIFT:
                    fractal.setHovering(true);
                    break;

                case KeyEvent.VK_MINUS:
                    iterationShift = -iterationShift;
                    if(fractal.getMaxIterations()==100) //prevent user from going under 100 iterations
                        break;
                case KeyEvent.VK_EQUALS: //using equals instead of plus
                    fractal.setMaxIterations(fractal.getMaxIterations() + iterationShift);
                    getSettings().updateSet();
                    repaint();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                fractal.setHovering(false);
            }
        }
    }
}
