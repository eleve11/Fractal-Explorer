import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * GUI that shows the Julia Set
 */
public class JuliaFrame extends FractalGUI
{
    private JuliaSet julia;
    private FavButton fav;
    private final DecimalFormat df = new DecimalFormat("#.##");
    public static final Dimension DEFAULT_SIZE = new Dimension(400,360);

    public JuliaFrame(Complex c){
        super("Filled Julia Set", new JuliaSet(c),DEFAULT_SIZE.width,DEFAULT_SIZE.height);
        setTitle("Filled Julia Set ("+df.format(c.getX())+", "+df.format(c.getY())+")");
        julia  = (JuliaSet) getFractal();
        fav = new FavButton(julia);
        init();
    }

    public void init()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = this.getContentPane();
        JPanel favPanel = new JPanel();
        favPanel.add(fav);
        pane.add(favPanel,BorderLayout.NORTH);
        pane.add(julia,BorderLayout.CENTER);
        //pane.add(getSettings(),BorderLayout.SOUTH);
        //add(getLastPoint(),BorderLayout.NORTH);
        setVisible(true);
    }

    public void liveJulia(Complex c){
        this.setTitle("Filled Julia Set ("+df.format(c.getX())+", "+df.format(c.getY())+")");
        setFractal(new JuliaSet(c));
        julia = (JuliaSet) getFractal();
        fav.setJuliaSet(julia);
        init();
    }
}