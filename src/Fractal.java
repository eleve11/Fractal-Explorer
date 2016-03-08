import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * represent a fractal on the complex plane.
 */
public abstract class Fractal extends JPanel
{
    private Double realLow, realUp, imagLow, imagUp;
    private int maxIterations = 100;
    private int[][] palette;

    //default values
    public static final double REAL_LOW = -2.0;
    public static final double REAL_UP = 2.0;
    public static final double IMAG_LOW = -1.6;
    public static final double IMAG_UP = 1.6;

    //construct using complex plane constraints
    public Fractal(double realLower, double realUpper, double imagLower, double imagUpper)
    {
        this.realLow = realLower;
        this.realUp = realUpper;
        this.imagLow = imagLower;
        this.imagUp = imagUpper;


        FractMouseListener fl = new FractMouseListener();
        this.addMouseListener(fl);
        this.addMouseMotionListener(fl);
        this.addKeyListener(new FractKeyLis(this));
        this.setFocusable(true);
    }

    //default constructor
    public Fractal(){
        this(REAL_LOW,REAL_UP,IMAG_LOW,IMAG_UP);
    }

    /*
     * draw the Fractal set on the complex plane
     * according to the function called by FunctionOfZ
     */
    @Override
    public void paint(Graphics g)
    {
        //loop through each pixel
        for(int y=0; y<getHeight(); y++){
            for(int x=0; x<getWidth(); x++)
            {
                int[][] palette = getPalette();
                double it = compute(getComplex(x,y));
                //interpolate between 2 adiacent color in the palette
                int itfloor = (int)Math.floor(it) + palette.length;
                int[] color1 = palette[itfloor % palette.length];
                int[] color2 = palette[(itfloor +1) % palette.length];
                Color col = RgbLinearInterpolate(color1,color2,it);
           /* other possible colouring
           float hue = (float)mandelbrot(getComplex(x,y))/10;
           Color col = new Color(Color.HSBtoRGB(hue,1,1)); //why the center is not black?
           */
                //draw pixel
                g.setColor(col);
                g.drawLine(x,y,x,y);
            }
        }
    }

    /**
     * Check if the function of Z escapes the limit
     * computes how close the point is to the fractal
     * should call functionOfZ and getColorConstant
     * @param c the point on the complex plane we are checking
     * @return colour constant value
     */
    public abstract double compute(Complex c);

    /**
     * this method calls the function of Z that graphically represents
     * the fractal on the complex plane when computed
     * @param c is the complex variable
     * @param z is the starting point
     * @return the resulting complex
     */
    public abstract Complex functionOfZ(Complex z, Complex c);

    /*
    * return the point in the complex plane that
    * corresponds to those pixels
    */
    public Complex getComplex(int x, int y)
    {
        double cx = (realUp-realLow)*x/getWidth() + realLow;
        //this is inverted so that the y axis goes in the right way
        double cy = (imagLow-imagUp)*y/getHeight() +  imagUp;
        return new Complex(cx,cy);
    }

    //method overload
    public Complex getComplex(Point p){
        return getComplex(p.x,p.y);
    }

    /**
     * Smooth colouring algorithm:
     * guarantees that the colors are continuous and don't create bands
     */
    public double getColorConstant(double iterations, Complex z)
    {
        if(iterations<getMaxIterations())
        {
            double log_zn = Math.log(z.modulusSquare())/2;
            double nu = Math.log(log_zn/Math.log(2)) / Math.log(2);
            iterations = (iterations + 1 - nu)/(palette.length *3); //divide by a factor to make it smoother
        }

        return iterations;
    }

    /**
     * interpolate to RGB colors
     * @param count will be the iterations converted into color constant
     * @return resulting color
     */
    public static Color RgbLinearInterpolate(int[] start, int[] end, double count)
    {
        // linear interpolation lerp (r,a,b) = (1-r)*a + r*b = (1-r)*(ax,ay,az) + r*(bx,by,bz)
        double r =  count - Math.floor(count); //get just decimal part
        double nr = 1.0 - r;
        double R = ((nr * start[0]) + (r * end[0])) % 256;
        double G = ((nr * start[1]) + (r * end[1])) % 256;
        double B = ((nr * start[2]) + (r * end[2])) % 256;

        return new Color((int)R,(int)G,(int)B);
    }

    /**
     * getters and setters
     */
    public Double getRealLow() {
        return realLow;
    }

    public Double getRealUp() {
        return realUp;
    }

    public Double getImagUp() {
        return imagUp;
    }

    public Double getImagLow() {
        return imagLow;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public int[][] getPalette() {
        return palette;
    }

    public FractalGUI getGUI(){return (FractalGUI) SwingUtilities.getWindowAncestor(this);}

    /*
     * these setters have constraints
     */
    public void setRealLow(double realLow) {
        if(realLow >= getRealUp())
            throw  new IllegalArgumentException();
        this.realLow = realLow;
    }

    public void setRealUp(double realUp) {
        if(realUp <= getRealLow())
            throw new IllegalArgumentException();
        this.realUp = realUp;
    }

    public void setImagLow(double imagLow) {
        if(imagLow >= getImagUp())
            throw new IllegalArgumentException();
        this.imagLow = imagLow;
    }

    public void setImagUp(double imagUp) {
        if(imagUp <= getImagLow())
            throw new IllegalArgumentException();
        this.imagUp = imagUp;
    }

    public void setMaxIterations(int maxIterations) {
        if(maxIterations<1)
            throw new IllegalArgumentException();
        this.maxIterations = maxIterations;
    }

    public void setPalette(int[][] palette) {
        this.palette = palette;
    }


    /**
     * mouse adapter that handles the zoom feature
     */
    private class FractMouseListener extends MouseAdapter
    {
        private Point startDrag, endDrag;

        /*
         * on mouse click show clicked coordinates
         */
        @Override
        public void mouseClicked(MouseEvent e) {
                Complex c = getComplex(e.getX(), e.getY());
                getGUI().getSettings().updatePointLabel(c);
        }

        /*
         * on mouse pressed start prepare for drag
         */
        @Override
        public void mousePressed(MouseEvent e) {
            startDrag = new Point(e.getX(),e.getY());
            endDrag = startDrag;
        }

        /*
         * when mouse is released, zoom if it was dragged
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if(endDrag!=startDrag){
                alignValues();
                zoom(startDrag,endDrag);
                getGUI().getSettings().updateSet();
            }

            startDrag = null;
            endDrag = null;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            endDrag = new Point(e.getX(),e.getY());

            //set rectangle constants
            int width = Math.abs(startDrag.x - endDrag.x);
            int height = Math.abs(startDrag.y - endDrag.y);
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);

            //draw the rectangle for the zooming area
            Graphics2D g = (Graphics2D) getGraphics();
            g.setColor(Color.WHITE);
            Rectangle r = new Rectangle(x,y,width, height);
            g.setStroke(new BasicStroke(2));
            g.draw(r);
            repaint(r);
        }

        //ZOOM ANIMATION WORKING BUT NOT THREAD SAFE CODE !
        private void zoom(Point a, Point b){
            Complex start = getComplex(a);
            Complex end = getComplex(b);

            //set the bounds
            Complex botBound = new Complex(getRealLow(),getImagLow());
            Complex topBound = new Complex(getRealUp(),getImagUp());

            //zoom by 15% of the total zoom each iteration
            double horShiftStart = (start.getX() - botBound.getX()) * 0.15;
            double verShiftStart = (topBound.getY() - start.getY()) * 0.15;
            double horShiftEnd = (topBound.getX() - end.getX()) * 0.15;
            double verShiftEnd = (end.getY() - botBound.getY()) * 0.15;

            while ((topBound.getX()>end.getX() && topBound.getY()>start.getY())
                || (botBound.getX()<start.getX() && botBound.getY()<end.getY()))
            {
                topBound = new Complex(topBound.getX()-horShiftEnd, topBound.getY()-verShiftStart);
                botBound = new Complex(botBound.getX()+horShiftStart, botBound.getY()+verShiftEnd);

                setRealLow(botBound.getX());
                setRealUp(topBound.getX());
                setImagLow(botBound.getY());
                setImagUp(topBound.getY());

                paint(getGraphics()); //HORRIBLE CODE (never call directly the paint method)
            }

//            //not animated (comment out the animated) zoom
//            setRealLow(start.getX());
//            setRealUp(end.getX());
//            setImagUp(start.getY());
//            setImagLow(end.getY());
//
//            repaint();
        }

        //make sure the rectangle startDrag is top left
        private void alignValues()
        {
            Point tmp = new Point();

            if(startDrag.x>endDrag.x) {
                tmp.x = endDrag.x;
                endDrag.x = startDrag.x;
                startDrag.x = tmp.x;
            }
            
            if(startDrag.y>endDrag.y){
                tmp.y = endDrag.y;
                endDrag.y = startDrag.y;
                startDrag.y = tmp.y;
            }
        }
    }
}