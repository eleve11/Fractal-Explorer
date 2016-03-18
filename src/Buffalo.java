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
        //the image is flipped by .conjugate method to make the buffalo identifiable ;)
        return z.square().subtract(z).conjugate().add(c);
    }
}