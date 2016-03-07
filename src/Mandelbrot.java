/**
 * Displays the Mandelbrot set on the ComplexPlane.
 */
public class Mandelbrot extends MainFractal
{
    /*
     * formula to compute the mandelbrot set
     */
    @Override
    public Complex functionOfZ(Complex z, Complex c){
        return z.square().add(c);
    }
}

