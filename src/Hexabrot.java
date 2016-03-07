/**
 * Multibrot with 6 tails.
 */
public class Hexabrot extends MainFractal {
    //like mandelbrot but with z^7
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        return z.square().square().times(z).times(z).times(z).add(c);
    }
}
