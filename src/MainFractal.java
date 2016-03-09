import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * abstract Fractal that has privileges over the Julia sets.
 */
public abstract class MainFractal extends Fractal
{
    private static boolean isHovering = false;
    private String title;

    //complex bound constructor
    public MainFractal(double realLower, double realUpper, double imagLower, double imagUpper){
        super(realLower,realUpper,imagLower,imagUpper);
        this.title = this.getClass().getName();
        setPalette(new int[][]{ {0, 0, 0}, {255,140,0}, {255,255,255}, {0, 255, 255}, {0, 0, 255} });
        this.addMouseListener(new FractMouseLis());
        this.addMouseMotionListener(new FractMouseLis());
    }

    //default constructor
    public MainFractal(){
        this(Fractal.REAL_LOW,Fractal.REAL_UP,Fractal.IMAG_LOW,Fractal.IMAG_UP);
    }

    /*
     * loop the function of z until it escapes or reaches max iterations
     * then get the smooth color constant
     */
    @Override
    public double compute(Complex c)
    {
        Complex z = new Complex(0,0);

        int iterations=0;
        while(iterations<getMaxIterations() && z.modulusSquare()<4.0) {
            z = functionOfZ(z,c);
            iterations++;
        }

        return getColorConstant(iterations,z);
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

    public boolean isHovering() {return isHovering;}

    public String getTitle() {
        return title;
    }

    /**
     * Listener class
     * click on set and create a new JuliaFrame
     */
    private class FractMouseLis extends MouseAdapter
    {
        //show julia
        @Override
        public void mouseClicked(MouseEvent e) {
            JuliaFrame.getInstance().updateJulia(getComplex(e.getX(), e.getY()));
        }

        //if hovering live update julia
        @Override
        public void mouseMoved(MouseEvent e) {
            if (isHovering())
                JuliaFrame.getInstance().updateJulia(getComplex(e.getX(), e.getY()));
        }
    }
}
