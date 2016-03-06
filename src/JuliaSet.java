/**
 * Filled Julia Set
 */
public class JuliaSet extends Fractal
{
    private Complex c; //constant value

    //complex plane bounds constructor
    public JuliaSet(double realLower, double realUpper, double imagLower, double imagUpper, Complex c){
        super(realLower,realUpper,imagLower,imagUpper);
        setPalette(new int[][]{{70, 0, 20}, {100, 0, 100}, {255, 0, 0}, {255, 200, 0} });
        this.c=c;
    }

    //default constructor
    public JuliaSet(Complex c){
        this(-2,2,-1.6,1.6,c);
    }

    /*
     * draw the Julia Set on the Complex plane
     */
    @Override
    public double functionOfZ(Complex z) {
        return julia(z);
    }

    /*
     * run julia function on Z and
     * return the smooth color constant for that point
     */
    private double julia(Complex z){
        int iterations = 0;

        while(iterations<getMaxIterations() && z.modulusSquare()<4.0){
            z = z.square().add(c);
            iterations++;
        }

        return getColorConstant(iterations,z);
    }

    //Getter method for the constant
    public Complex getC() {
        return c;
    }
}
