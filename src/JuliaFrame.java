import javax.swing.*;
import java.awt.*;

/**
 * GUI that shows the Julia Set
 */
public class JuliaFrame extends FractalGUI
{
    private JuliaSet julia;
    private FavButton fav;
    public static final Dimension DEFAULT_SIZE = new Dimension(400,400);

    public JuliaFrame(Complex c){
        super("Filled Julia Set", new JuliaSet(c),DEFAULT_SIZE.width,DEFAULT_SIZE.height);
        setTitle("Filled Julia Set "+c.toString());
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
        pane.add(favPanel,BorderLayout.SOUTH);
        pane.add(julia,BorderLayout.CENTER);
        //pane.add(getSettings(),BorderLayout.SOUTH);
        //add(getLastPoint(),BorderLayout.NORTH);
        setVisible(true);
    }

    public void liveJulia(Complex c){
        this.setTitle("Filled Julia Set "+c.toString());
        setFractal(new JuliaSet(c));
        julia = (JuliaSet) getFractal();
        fav.setJuliaSet(julia);
        init();
    }
}