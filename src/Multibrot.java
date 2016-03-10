/**
 * Create a multibrot with n tails.
 */
public class Multibrot extends MainFractal
{
    private int n;
    //construct multibrot using number of tails
    public Multibrot(int n){
        this.setN(n);

        //little trick to handle big Ns
        if(n>20)
            setMaxIterations(getMaxIterations()- n/2);
    }

    /*
     * the multibrot runs the mandelbrot formula with z to the number of tails+1
     */
    @Override
    public Complex functionOfZ(Complex z, Complex c)
    {
        Complex x = z;
        for(int i = 0; i<n; i++)
            x = x.times(z);
        return x.add(c);
    }

    //make sure the argument is acceptable
    private void setN(int n){
        if (n<1)
            throw new IllegalArgumentException();
        this.n = n;
    }

    public Integer getN(){return n;}
}
