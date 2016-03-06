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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
