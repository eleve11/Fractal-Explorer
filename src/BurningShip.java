/**
 * Representation of the BurningShip Fractal.
 */
public class BurningShip extends MainFractal
{
    /*
     * draw the burning ship set
     */
    @Override
    public double functionOfZ(Complex c) {
        return burningShip(c);
    }

    /*
     * run the burning ship function and
     * @return the color constant
     */
    private double burningShip(Complex c)
    {
        Complex z = new Complex(0,0);

        int iterations=0;
        while(iterations<getMaxIterations() && z.modulusSquare()<4.0) {
            z = z.firstQuadrant().square().add(c);
            iterations++;
        }

        return getColorConstant(iterations,z);
    }
}
