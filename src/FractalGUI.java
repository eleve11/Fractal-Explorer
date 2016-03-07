import javax.swing.*;
import java.awt.*;
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

        //TODO not sure i want these 2 here
        this.setFocusable(true);
        this.addKeyListener(new FractKeyLis(fractal));

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

    public void setSettings(JPanel settings) {
        this.settings = settings;
    }
}
