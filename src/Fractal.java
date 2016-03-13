import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * represent a fractal on the complex plane.
 */
//TODO: fix drag rectangle
public abstract class Fractal extends JPanel
{
    private Double realLow, realUp, imagLow, imagUp;
    private int maxIterations = 100;
    private int[][] palette;
    private double colorOffset = 0;
    private FractMouseListener fl;

    //default values
    public static final double REAL_LOW = -2.0;
    public static final double REAL_UP = 2.0;
    public static final double IMAG_LOW = -1.6;
    public static final double IMAG_UP = 1.6;

    //construct using complex plane constraints
    public Fractal(double realLower, double realUpper, double imagLower, double imagUpper) {
        this.realLow = realLower;
        this.realUp = realUpper;
        this.imagLow = imagLower;
        this.imagUp = imagUpper;

        fl = new FractMouseListener();
        this.addMouseListener(fl);
        this.addMouseMotionListener(fl);
        this.addKeyListener(new FractKeyLis(this));
        this.setFocusable(true);
    }

    //default constructor
    public Fractal() {
        this(REAL_LOW, REAL_UP, IMAG_LOW, IMAG_UP);
    }

    /*
     * draw the Fractal set on the complex plane
     * according to the function called by FunctionOfZ
     */
    @Override
    public void paint(Graphics g) {
        //loop through each pixel
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int[][] palette = getPalette();
                double it = getMaxIterations() - compute(getComplex(x, y));
                //interpolate between 2 adiacent color in the palette
                int itfloor = (int) Math.floor(it);
                int[] color1 = palette[itfloor % palette.length];
                int[] color2 = palette[(itfloor+1) % palette.length];
                Color col = RgbLinearInterpolate(color1, color2, it);
           /* other possible colouring
           float hue = (float)mandelbrot(getComplex(x,y))/10;
           Color col = new Color(Color.HSBtoRGB(hue,1,1)); //why the center is not black?
           */
                //draw pixel
                g.setColor(col);
                g.drawLine(x, y, x, y);
            }
        }

        // draw the zoom rectangle if dragging
        if (fl.startDrag != null && fl.endDrag != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(Color.WHITE);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
            g2.draw(fl.r);
            g2.setPaint(Color.LIGHT_GRAY);
            g2.fill(fl.r);
        }
    }

    /**
     * Check if the function of Z escapes the limit
     * computes how close the point is to the fractal
     * should call functionOfZ and getColorConstant
     *
     * @param c the point on the complex plane we are checking
     * @return colour constant value
     */
    public abstract double compute(Complex c);

    /**
     * this method calls the function of Z that graphically represents
     * the fractal on the complex plane when computed
     *
     * @param c is the complex variable
     * @param z is the starting point
     * @return the resulting complex
     */
    public abstract Complex functionOfZ(Complex z, Complex c);

    /*
    * return the point in the complex plane that
    * corresponds to those pixels
    */
    public Complex getComplex(int x, int y) {
        double cx = (realUp - realLow) * x / getWidth() + realLow;
        //this is inverted so that the y axis goes in the right way
        double cy = (imagLow - imagUp) * y / getHeight() + imagUp;
        return new Complex(cx, cy);
    }

    //method overload
    public Complex getComplex(Point p) {
        return getComplex(p.x, p.y);
    }

    /**
     * Smooth colouring algorithm:
     * guarantees that the colors are continuous and don't create bands
     */
    public double getColorConstant(double iterations, Complex z)
    {
        if (iterations < getMaxIterations()) {
            double log_zn = Math.log(z.modulusSquare()) / 2;
            double nu = Math.log(log_zn / Math.log(2)) / Math.log(2);
            iterations = (iterations + 1 - nu) / (palette.length*3); //divide by a factor to make it smoother
        }

        return iterations;
    }

    /**
     * interpolate two RGB colors
     *
     * @param count will be the iterations converted into color constant
     * @return resulting color
     */
    public static Color RgbLinearInterpolate(int[] start, int[] end, double count) {
        // linear interpolation lerp (r,a,b) = (1-r)*a + r*b = (1-r)*(ax,ay,az) + r*(bx,by,bz)
        double r = count - Math.floor(count); //get just decimal part
        double nr = 1.0 - r;
        double R = ((nr * start[0]) + (r * end[0])) % 256;
        double G = ((nr * start[1]) + (r * end[1])) % 256;
        double B = ((nr * start[2]) + (r * end[2])) % 256;

        return new Color((int) R, (int) G, (int) B);
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

    public double getColorOffset(){return colorOffset;}

    public FractalGUI getGUI() {
        return (FractalGUI) SwingUtilities.getWindowAncestor(this);
    }

    /*
     * these setters have constraints
     */
    public void setRealLow(double realLow) {
        if (realLow > getRealUp())
            throw new IllegalArgumentException();
        this.realLow = realLow;
    }

    public void setRealUp(double realUp) {
        if (realUp < getRealLow())
            throw new IllegalArgumentException();
        this.realUp = realUp;
    }

    public void setImagLow(double imagLow) {
        if (imagLow > getImagUp())
            throw new IllegalArgumentException();
        this.imagLow = imagLow;
    }

    public void setImagUp(double imagUp) {
        if (imagUp < getImagLow())
            throw new IllegalArgumentException();
        this.imagUp = imagUp;
    }

    public void setMaxIterations(int maxIterations) {
        if (maxIterations < 1)
            throw new IllegalArgumentException();
        this.maxIterations = maxIterations;
    }

    public void setColorOffset(double offset){
        if(offset<0||offset>palette.length)
            throw new IllegalArgumentException();
        this.colorOffset = offset;
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
        private Rectangle r;

        /*
         * on mouse click update clicked coordinates
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
            startDrag = new Point(e.getX(), e.getY());
            endDrag = startDrag;
        }

        /*
         * when mouse is released, zoom if it was dragged
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (endDrag != startDrag) {
                alignValues();
                //run on a separate thread because EDT would skip to the final repaint call
                new Thread(new ZoomRun(startDrag,endDrag)).start();
            }

            startDrag = null;
            endDrag = null;
        }

        /*
         * when the mouse is dragged draw the zooming area rectangle
         */
        @Override
        public void mouseDragged(MouseEvent e)
        {
            endDrag = new Point(e.getX(), e.getY());

            //set rectangle constants
            int width = Math.abs(startDrag.x - endDrag.x);
            int height = Math.abs(startDrag.y - endDrag.y);
            int x = Math.min(startDrag.x, endDrag.x);
            int y = Math.min(startDrag.y, endDrag.y);
            r = new Rectangle(x, y, width, height);

            repaint();
        }

        //make sure the rectangle startDrag is top left
        private void alignValues()
        {
            Point tmp = new Point();

            if (startDrag.x > endDrag.x) {
                tmp.x = endDrag.x;
                endDrag.x = startDrag.x;
                startDrag.x = tmp.x;
            }

            if (startDrag.y > endDrag.y) {
                tmp.y = endDrag.y;
                endDrag.y = startDrag.y;
                startDrag.y = tmp.y;
            }
        }
    }

    /**
     * Runnable that runs the zoom animation
     */
    class ZoomRun implements Runnable
    {
        private Complex start,end, topBound, botBound;
        //it will run 50 iterations to finish the zoom
        //the higher the smoother yet slower zoom animation
        private final double ITERATIONS = 50; //must be greater than 0

        //the constructor sets the bounds and destination points
        public ZoomRun(Point start, Point end){
            this.start = getComplex(start);
            this.end = getComplex(end);
            this.botBound = new Complex(getRealLow(),getImagLow());
            this.topBound = new Complex(getRealUp(),getImagUp());
        }

        //zoom by 2% of the total zoom each iteration
        @Override
        public void run()
        {
            //set the shift constants so that it look like it speeds up in the  end
            double horShiftStart = (start.getX() - botBound.getX()) / ITERATIONS;
            double verShiftStart = (topBound.getY() - start.getY()) / ITERATIONS;
            double horShiftEnd = (topBound.getX() - end.getX()) / ITERATIONS;
            double verShiftEnd = (end.getY() - botBound.getY()) / ITERATIONS;

            while ((topBound.getX()>end.getX() && topBound.getY()>start.getY())
                || (botBound.getX()<start.getX() && botBound.getY()<end.getY()))
            {
                topBound = new Complex(topBound.getX() - horShiftEnd, topBound.getY() - verShiftStart);
                botBound = new Complex(botBound.getX() + horShiftStart, botBound.getY() + verShiftEnd);

                setRealLow(botBound.getX());
                setRealUp(topBound.getX());
                setImagLow(botBound.getY());
                setImagUp(topBound.getY());

                repaint();
                getGUI().getSettings().updateSet();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}