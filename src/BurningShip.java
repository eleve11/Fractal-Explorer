/**
 * Representation of the BurningShip Fractal.
 */
public class BurningShip extends MainFractal
{
    /*
     * draw the burning ship set
     */
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        return z.firstQuadrant().square().add(c);
    }
}
