import javax.swing.*;
import java.awt.*;
/**
 * has a JPanel (Image) and 2 textfield to set iterations position
 * also label to show what point was clicked
 */
public abstract class FractalGUI extends JFrame
{
    private Fractal fractal;
    private SettingsPanel settings;
    public static final Dimension DEFAULT_SIZE = new Dimension(800,520);

    //constructor
    public FractalGUI(String title, Fractal fractal,int width,int height)
    {
        super(title);

        this.fractal = fractal;
        this.settings = new SettingsPanel(fractal);

        // not sure i want these 2 lines here as well as in the Fractal
        this.setFocusable(true);
        this.addKeyListener(new FractKeyLis(fractal));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width, height);
    }

    //short constructor
    public FractalGUI(String title, Fractal fractal){
        this(title, fractal, DEFAULT_SIZE.width
                ,DEFAULT_SIZE.height);
    }

    /**
     * subclasses need to initialise the frame
     */
    public abstract void init();

    /*
     * getters and setters
     */
    public Fractal getFractal() {
        return fractal;
    }

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }

    public SettingsPanel getSettings() {
        return settings;
    }
}
