/**
 * Buffalo Fractal.
 */
public class Buffalo extends MainFractal
{
    /*
     * draw the buffalo fractal
     */
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        z = z.firstQuadrant();
        return z.square().subtract(z).add(c);
    }
}