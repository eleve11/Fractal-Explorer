import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * abstract Fractal that has privileges over the Julias.
 */
//TODO should have a title for the frame maybe?
public abstract class MainFractal extends Fractal
{
    private JuliaFrame juliaFrame;
    private static boolean isHovering = false;

    //complex bound constructor
    public MainFractal(double realLower, double realUpper, double imagLower, double imagUpper){
        super(realLower,realUpper,imagLower,imagUpper);
        setPalette(new int[][]{ {0, 0, 0}, {0, 0, 255}, {0, 255, 255}, {255,255,255}, {255,140,0} });
        this.addMouseListener(new FractMouseLis());
        this.addMouseMotionListener(new FractMouseLis());
    }

    //default constructor
    public MainFractal(){
        this(Fractal.REAL_LOW,Fractal.REAL_UP,Fractal.IMAG_LOW,Fractal.IMAG_UP);
    }

    @Override
    public abstract double functionOfZ(Complex c);

    public void startJulia(Complex c) {
        Point location = new Point(MainFrame.DEFAULT_SIZE.width, 0);
        Dimension d = JuliaFrame.DEFAULT_SIZE;

        //if a julia already exists close it
        // but save it's location on the screen and dimension
        if (juliaFrame != null) {
            juliaFrame.dispose();
            location = juliaFrame.getLocation();
            d = juliaFrame.getSize();
        }

        //create a new JuliaFrame on C with the set location
        juliaFrame = new JuliaFrame(c);
        juliaFrame.setSize(d);
        juliaFrame.setLocation(location);
    }

    public static void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    public boolean isHovering() {return isHovering;}

    /**
     * Listener class
     * click on set and create a new JuliaFrame
     */
    private class FractMouseLis extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Complex c = getComplex(e.getX(), e.getY());
            startJulia(c);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (isHovering())
                showJulia(getComplex(e.getX(), e.getY()));
        }

        private void showJulia(Complex c) {
            if (juliaFrame == null) startJulia(c);
            juliaFrame.liveJulia(c);
        }
    }

}
