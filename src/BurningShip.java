/**
 * Representation of the BurningShip Fractal.
 */
public class BurningShip extends MainFractal
{
    /*
     * draw the burning ship set flipped
     */
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        //.conjugate flips the i-value so the ship doesn't "look upside down"
        return z.firstQuadrant().square().conjugate().add(c);
    }
}
