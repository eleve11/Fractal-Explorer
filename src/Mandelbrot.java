/**
 * Displays the Mandelbrot set on the ComplexPlane.
 */
public class Mandelbrot extends MainFractal
{
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
}

