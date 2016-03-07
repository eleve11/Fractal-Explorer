/**
 * A multibrot with 8 tails.
 */
public class Octabrot extends MainFractal
{
    //octabrot formula is like mandelbrot but with z^9
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        return z.square().square().square().times(z).add(c);
    }
}
