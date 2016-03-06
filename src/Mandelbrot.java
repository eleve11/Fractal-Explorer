import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays the Mandelbrot set on the ComplexPlane.
 */
public class Mandelbrot extends Fractal
{
    private JuliaFrame juliaFrame;

    //complex bound constructor
    public Mandelbrot(double realLower, double realUpper, double imagLower, double imagUpper){
        super(realLower,realUpper,imagLower,imagUpper);
        setPalette(new int[][]{ {0, 0, 0}, {0, 0, 255}, {0, 255, 255}, {255,255,255}, {255,140,0} });
        this.addMouseListener(new MandelListener());
        this.addMouseMotionListener(new MandelListener());
    }

    //default constructor
    public Mandelbrot(){
        this(Fractal.REAL_LOW,Fractal.REAL_UP,Fractal.IMAG_LOW,Fractal.IMAG_UP);
    }

    /*
     * draw the mandelbrot set
     */
    @Override
    public double functionOfZ(Complex c){
        return mandelbrot(c);
    }

    /*
     * run mandelbrot function on c
     * return the smooth color constant value for C
     */
    private double mandelbrot(Complex c)
    {
        Complex z = new Complex(0,0);

        int iterations=0;
        while(iterations<getMaxIterations() && z.modulusSquare()<4.0) {
            z = z.square().add(c);
            iterations++;
        }

        return getColorConstant(iterations,z);
    }

    /**
     * Listener class
     * click on set and create a new JuliaFrame
     */
    private class MandelListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Complex c = getComplex(e.getX(), e.getY());
            startJulia(c);
            setHovering(false);
        }

        private void startJulia(Complex c) {
            Point location = new Point(getWidth(), 0);
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

        private void showJulia(Complex c) {
            if (juliaFrame == null) startJulia(c);
            juliaFrame.liveJulia(c);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (isHovering())
                showJulia(getComplex(e.getX(), e.getY()));
        }
    }
}

