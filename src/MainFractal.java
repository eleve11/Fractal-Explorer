import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * abstract Fractal that has more features than the Julia sets.
 */
public abstract class MainFractal extends Fractal
{
    private static boolean isHovering = false;
    private String title;

    //complex bound constructor
    public MainFractal(double realLower, double realUpper, double imagLower, double imagUpper){
        super(realLower,realUpper,imagLower,imagUpper);
        this.title = this.getClass().getName();

        //set colors
        setPalette(new int[][]{ {0, 0, 0},{255,140,0}, {255,255,255}, {0, 255, 255}, {0, 0, 255} });

        //attach listeners
        this.addMouseListener(new FractMouseLis());
        this.addMouseMotionListener(new FractMouseLis());
    }

    //default constructor
    public MainFractal(){
        this(Fractal.REAL_LOW,Fractal.REAL_UP,Fractal.IMAG_LOW,Fractal.IMAG_UP);
    }

    /*
     * loop the function of z until it escapes or reaches max iterations
     * then return the smooth color constant
     */
    @Override
    public double compute(Complex c)
    {
        //z starts at 0
        Complex z = new Complex(0,0);

        int iterations=0;
        while(iterations<getMaxIterations() && z.modulusSquare()<BAILOUT*BAILOUT) {
            z = functionOfZ(z,c);
            iterations++;
        }

        return getSmoothIterations(iterations,z);
    }

    //function that represents the formula for the fractal
    @Override
    public abstract Complex functionOfZ(Complex z, Complex c);

    /*
     * getters and setters
     */
    public static void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    public static boolean isHovering() {return isHovering;}

    public String getTitle() {
        return title;
    }

    /**
     * Listener class
     * click on set and create a new JuliaFrame
     * hover to update the current JuliaSet
     */
    private class FractMouseLis extends MouseAdapter
    {
        //show julia, run the julia on EDT
        @Override
        public void mouseClicked(MouseEvent e)
        {
            Complex c = getComplex(e.getX(), e.getY());
            JuliaFrame.getInstance().updateJulia(c,MainFractal.this);
        }

        //if hovering live update julia on EDT
        @Override
        public void mouseMoved(MouseEvent e)
        {
            if(isHovering()) {
                Complex c = getComplex(e.getX(), e.getY());
                JuliaFrame.getInstance().updateJulia(c,MainFractal.this);
            }
        }
    }
}