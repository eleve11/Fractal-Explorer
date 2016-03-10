import java.text.DecimalFormat;

/**
 * Complex numbers are two dimensional number
 * thus they have x,y coordinates
 * bear in mind y also represents the coefficient of i
 * where i^2=-1.
 */
public class Complex
{
    private double x,y;

    //construct complex with 2 double type coordinates
    public Complex(double x,double y){
        this.x = x; //set real part
        this.y = y; //set imaginary part
    }

    //return the squared Complex number
    public Complex square(){
        double real = x*x - y*y;
        double imaginary = 2*x*y;
        return new Complex(real,imaginary);
    }

    //return the square of the modulus of this complex number
    public double modulusSquare(){
        return x*x + y*y;
    }

    //return the sum between this complex number and the argument
    public Complex add(Complex z){
        double real = this.getX() + z.getX();
        double imaginary = this.getY() + z.getY();
        return new Complex(real,imaginary);
    }

    //return the difference between this and the argument
    public Complex subtract(Complex z){
        double real = this.getX() - z.getX();
        double imaginary = this.getY() - z.getY();
        return new Complex(real,imaginary);
    }

    //return the product between this Complex and parameter
    public Complex times(Complex z){
        //(x+yi)(u+vi) = (xu -yv) + (xv + yu)i
        double real = this.getX()*z.getX() - this.getY()*z.getY();
        double imaginary = this.getX()*z.getY() + this.getY()*z.getX();
        return new Complex(real,imaginary);
    }

    /*
     *returns the respective complex number in the first quadrant
     *the complex will have the absolute values of the coordinates
     * as coordinates
     * used to calculate the burning ship
     */
    public Complex firstQuadrant(){
        return new Complex(Math.abs(x),Math.abs(y));
    }

    //compare two complex numbers, return true if they are the same
    public boolean equals(Complex c){
        return this.getX()==c.getX() && this.getY() == c.getY();
    }

    //returns a readable string that represents the Complex Number
    @Override
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("#.###");
        return "("+df.format(getX())+", "+df.format(getY())+")";
    }

    /*
     * accessor methods for both real land imaginary part
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
