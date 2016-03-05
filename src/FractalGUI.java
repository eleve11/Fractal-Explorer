import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * has a JPanel (Image) and 2 textfield to set iterations position
 * also label to show what point was clicked
 */
//TODO get the keyListener to work
public abstract class FractalGUI extends JFrame
{
    private Fractal fractal;
    private JPanel settings;
    private JLabel lastPoint;

    public FractalGUI(String title, Fractal fractal,int width,int height){
        super(title);

        this.fractal = fractal;
        this.settings = new SettingsPanel(fractal);
        this.lastPoint = fractal.getCLabel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width, height);
    }

    public FractalGUI(String title, Fractal fractal){
        this(title, fractal, 600,500);
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

    public JLabel getLastPoint() {
        return lastPoint;
    }
}
