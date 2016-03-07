/**
 * Created by Andrea on 06/03/2016.
 */
//TODO do better OOP than extending mandelbrot
public class BurningShip extends Mandelbrot
{
    @Override
    public double functionOfZ(Complex c) {
        return burningShip(c);
    }

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
