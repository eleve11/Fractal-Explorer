import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * GUI that shows the Julia Set
 */
public class JuliaFrame extends FractalGUI
{
    private JuliaSet julia;
    //this frame is a singleton
    private static final JuliaFrame juliaFrame = new JuliaFrame();

    public JuliaFrame(Complex c){
        super("Filled Julia Set", new JuliaSet(c),400,360);
        julia  = (JuliaSet) getFractal();
    }

    //default constructor only used to have it as a singleton
    public JuliaFrame(){
        this(new Complex(0,0));
    }

    public void init(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = this.getContentPane();
        pane.add(julia,BorderLayout.CENTER);
        //pane.add(getSettings(),BorderLayout.SOUTH);
        //add(getLastPoint(),BorderLayout.NORTH);
        this.setSize(getWidth(),getHeight());
    }

    //get the instance of the singleton
    public static JuliaFrame getInstance(){return juliaFrame;}

    public void updateJulia(Complex c){
        setFractal(new JuliaSet(c));
        julia = (JuliaSet) getFractal();
        DecimalFormat df = new DecimalFormat("#.##");
        setTitle("Filled Julia Set ("+df.format(c.getX())+", "+df.format(c.getY())+")");
        init();
    }
}