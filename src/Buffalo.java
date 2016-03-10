/**
 * Buffalo Fractal.
 */
public class Buffalo extends MainFractal
{
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        z = z.firstQuadrant();
        return z.square().subtract(z).add(c);
    }
}
