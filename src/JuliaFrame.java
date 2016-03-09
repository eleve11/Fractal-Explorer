import javax.swing.*;
import java.awt.*;

/**
 * GUI that shows the Julia Set
 */
public class JuliaFrame extends FractalGUI
{
    private JuliaSet julia;
    private JPanel toolBar;

    public static final Dimension DEFAULT_SIZE = new Dimension(400,400);

    //let the frame be a singleton
    private static JuliaFrame instance = new JuliaFrame();

    //private constructor because is a singleton
    private JuliaFrame(){
        super("Filled Julia Set", new JuliaSet(new Complex(0,0)) ,DEFAULT_SIZE.width,DEFAULT_SIZE.height);
        setTitle("Filled Julia Set");
        julia  = (JuliaSet) getFractal();
        this.setLocation(MainFrame.DEFAULT_SIZE.width, 0);
        init();
    }

    //initialise the JFrame
    public void init()
    {
        this.requestFocus();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container pane = this.getContentPane();
        setToolBar(julia);
        pane.add(toolBar,BorderLayout.SOUTH);
        pane.add(julia,BorderLayout.CENTER);
        //pane.add(getSettings(),BorderLayout.WEST);
    }

    private void setToolBar(JuliaSet juliaSet){
        toolBar = new JPanel();
        FavButton fav = new FavButton(juliaSet);
        SaveButton save = new SaveButton(juliaSet);
        toolBar.add(fav);
        toolBar.add(save);
    }

    /*
     * updates the julia frame with a new JuliaSet and toolbar
     * basically a second initialisation  that updates the screen
     * for when the frame gets activated
     */
    public void updateJulia(Complex c)
    {
        this.setTitle("Filled Julia Set "+c.toString());
        //hide what was before
        getFractal().setVisible(false);
        toolBar.setVisible(false);
        //replace with what is new
        setFractal(new JuliaSet(c));
        julia = (JuliaSet) getFractal();
        init();
        setVisible(true);
        julia.requestFocus();
    }

    //return the instance of the singleton
    public static JuliaFrame getInstance() {
        return instance;
    }
}