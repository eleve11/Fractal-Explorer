import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays the Mandelbrot set on the ComplexPlane.
 */
public class Mandelbrot extends Fractal
{
    //complex bound constructor
    public Mandelbrot(double realLower, double realUpper, double imagLower, double imagUpper){
        super(realLower,realUpper,imagLower,imagUpper);
        setPalette(new int[][]{ {0, 0, 0}, {0, 0, 255}, {0, 255, 255}, {255,255,255}, {255,140,0} });
        this.addMouseListener(new MandelListener());
        this.addMouseMotionListener(new MandelListener());
    }

    //default constructor
    public Mandelbrot(){
        this(-2,2,-1.6,1.6);
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
    private class MandelListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            Complex c = getComplex(e.getX(), e.getY());
            showJulia(c);
        }

        private void showJulia(Complex c){
            JuliaFrame juliaFrame = JuliaFrame.getInstance();
            juliaFrame.setLocation(getWidth(),0);
            juliaFrame.updateJulia(c);
            juliaFrame.setVisible(true);
        }

        //maybe use a thread
        @Override
        public void mouseMoved(MouseEvent e) {
            if(isHovering())
                showJulia(getComplex(e.getX(),e.getY()));
        }

        //HOW DO YOU SHARE INFO?? USE SYNCHRONIZATION
        private class Hover implements Runnable{
            private MouseEvent e;

            @Override
            public void run() {
                while (isHovering())
                    if(e!=null)
                        showJulia(getComplex(e.getX(),e.getY()));
            }

            public void setE(MouseEvent e) {
                this.e = e;
            }
        }
    }
}

