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
    private JScrollPane scroll;

    public static final Dimension DEFAULT_SIZE = new Dimension(800,520);

    //constructor
    public FractalGUI(String title, Fractal fractal,int width,int height)
    {
        super(title);

        this.fractal = fractal;

        //set the settings panel scrollable only vertically
        this.settings = new SettingsPanel(fractal);
        this.scroll = new JScrollPane(settings);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // these make the keylistener work at launch
        this.setFocusable(true);
        this.addKeyListener(new FractKeyLis(fractal));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(width, height);
    }

    //default constructor
    public FractalGUI(String title, Fractal fractal){
        this(title, fractal, DEFAULT_SIZE.width
                ,DEFAULT_SIZE.height);
    }

    /**
     * subclasses need to initialise the frame and add components
     */
    public abstract void init();

    /*
     * accessor methods
     */
    public Fractal getFractal() {
        return fractal;
    }

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
        getSettings().setFractal(fractal); //update settings as well
    }

    public SettingsPanel getSettings() {
        return settings;
    }

    public JScrollPane getScrollSets() {
        return scroll;
    }
}
