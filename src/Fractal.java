import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

/**
 * represent a fractal on the complex plane.
 */
public abstract class Fractal extends JPanel
{
    private Double realLow, realUp, imagLow, imagUp;
    private int maxIterations = 100;
    private int[][] palette;
    private JLabel cLabel; //TODO: shouldn't have the label in this class?
    private boolean isHovering = false;

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
        cLabel = new JLabel();


        FractMouseListener fl = new FractMouseListener();
        this.addMouseListener(fl);
        this.addMouseMotionListener(fl);
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
    //TODO: document how i calculate the colours -- see wikipedia for mandelbrot
    @Override
    public void paint(Graphics g)
    {
        //loop through each pixel
        for(int y=0; y<getHeight(); y++){
            for(int x=0; x<getWidth(); x++)
            {
                int[][] palette = getPalette();
                double it = functionOfZ(getComplex(x,y));
                int itfloor = (int)Math.floor(it);
                int[] color1 = palette[itfloor % palette.length];
                int[] color2 = palette[(itfloor +1) % palette.length];
                Color col = RgbLinearInterpolate(color1,color2,it);
           /*
           float hue = (float)mandelbrot(getComplex(x,y))/10;
           Color col = new Color(Color.HSBtoRGB(hue,1,1)); //why the center is not black?
           */
                g.setColor(col);
                g.drawLine(x,y,x,y);
            }
        }
    }

    public abstract double functionOfZ(Complex c);

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

    public JLabel getCLabel(){
        return cLabel;
    } //TODO: see top

    //z should be the value of it escapes the mandelbrot
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

    //interpolate formula taken from wikipedia
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

    public boolean isHovering() {return isHovering;}

    public FractalGUI getGUI(){return (FractalGUI) SwingUtilities.getWindowAncestor(Fractal.this);}

    //TODO: throw exceptions for edge cases in these settings
    public void setRealLow(double realLow) {
        this.realLow = realLow;
    }

    public void setRealUp(double realUp) {
        this.realUp = realUp;
    }

    public void setImagLow(double imagLow) {
        this.imagLow = imagLow;
    }

    public void setImagUp(double imagUp) {
        this.imagUp = imagUp;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setPalette(int[][] palette) {
        this.palette = palette;
    }

    public void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    //TODO: inner classes are huge, do something!
    /**
     * mouse adapter that zooms
     */
    private class FractMouseListener extends MouseAdapter
    {
        private Point startDrag, endDrag;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                Complex c = getComplex(e.getX(), e.getY());
                DecimalFormat df = new DecimalFormat("#.##");
                getCLabel().setText("Last selected point: (" + df.format(c.getX()) + ", " + df.format(c.getY()) + ")");
                setHovering(false);
            }else
                setHovering(!isHovering);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startDrag = new Point(e.getX(),e.getY());
            endDrag = startDrag;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(endDrag!=startDrag)
                zoom(startDrag,endDrag);
            startDrag = null;
            endDrag = null;
            getGUI().getSettings().updateSet();
        }

        //TODO zoom rectangle sucks
        @Override
        public void mouseDragged(MouseEvent e) {
            endDrag = new Point(e.getX(),e.getY());

            //draw the transparent  rectangle
            int width = Math.abs(startDrag.x - endDrag.x);
            int height = Math.abs(startDrag.y - endDrag.y);
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);
            Graphics2D g = (Graphics2D) getGraphics();
            g.setColor(Color.WHITE);
            Rectangle r = new Rectangle(x,y,width, height);
            g.setStroke(new BasicStroke(2));
            g.draw(r);
            repaint(r);
        }

        //zoom
        private void zoom(Point a, Point b){
            alignValues();
            Complex start = getComplex(a);
            Complex end = getComplex(b);

            /* ZOOM ANIMATION NOT WORKING
            Complex topBound = new Complex(getRealLow(),getImagLow());
            Complex botBound = new Complex(getRealUp(),getImagUp());
            double horShiftStart = Math.abs(start.getX() - topBound.getX())/10;
            double verShiftStart = Math.abs(start.getY() - topBound.getY())/10;
            double horShiftEnd = Math.abs(end.getX() - botBound.getX())/10;
            double verShiftEnd = Math.abs(end.getY() - botBound.getY())/10;

            while ((topBound.getX()<start.getX() && topBound.getY()<start.getY())
                || (botBound.getX()>end.getX() && botBound.getY()>end.getY()))
            {
                topBound.setX(topBound.getX()+horShiftStart);
                topBound.setY(topBound.getY()+verShiftStart);
                botBound.setX(botBound.getX()-horShiftEnd);
                botBound.setY(botBound.getY()-verShiftEnd);

                setRealLow(topBound.getX());
                setRealUp(botBound.getX());
                setImagUp(topBound.getY());
                setImagLow(botBound.getY());
                System.out.println("zoom");
                repaint();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            //finalise zoom
            setRealLow(start.getX());
            setRealUp(end.getX());
            setImagUp(start.getY());
            setImagLow(end.getY());

            repaint();
        }

        //make sure the rectangle starts at startDrag point
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