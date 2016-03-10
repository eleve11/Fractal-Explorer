import java.awt.*;

/**
 * Filled Julia Set
 */
public class JuliaSet extends Fractal
{
    private Complex c; //constant value
    private MainFractal generator;

    //complex plane bounds constructor
    public JuliaSet(double realLower, double realUpper, double imagLower, double imagUpper, Complex c, MainFractal generator){
        super(realLower,realUpper,imagLower,imagUpper);
        setPalette(new int[][]{{70, 0, 20}, {100, 0, 100}, {255, 0, 0}, {255, 200, 0} });
        this.c=c;
        this.generator = generator;
    }

    //default constructor
    public JuliaSet(Complex c, MainFractal generator){
        this(-2,2,-1.6,1.6,c,generator);
    }

    /*
     * run julia function on Z and
     * return the smooth color constant for that point
     */
    @Override
    public double compute(Complex z) {
        int iterations = 0;

        while(iterations<getMaxIterations() && z.modulusSquare()<4.0){
            z = functionOfZ(z,c);
            iterations++;
        }

        return getColorConstant(iterations,z);
    }

    //julia set formula
    @Override
    public Complex functionOfZ(Complex z, Complex c) {
        return generator.functionOfZ(z,c);
    }

    //Getter method for the constant
    public Complex getC() {
        return c;
    }

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }
}
