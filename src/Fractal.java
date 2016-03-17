import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * represent a fractal on the complex plane using a BufferedImage.
 */
public abstract class Fractal extends JPanel
{
    private Double realLow, realUp, imagLow, imagUp;
    private int maxIterations;
    private int[][] palette;
    private FractMouseListener fl;
    private BufferedImage image;
    private Thread[] threads;
    private int threadsCompleted;

    //default values
    public static final double REAL_LOW = -2.0;
    public static final double REAL_UP = 2.0;
    public static final double IMAG_LOW = -1.6;
    public static final double IMAG_UP = 1.6;
    public static final int MAX_ITERATIONS = 100;
    public static final int BAILOUT = 2;
    public final int RENDERING_THREADS = Runtime.getRuntime().availableProcessors();

    //construct using complex plane constraints
    public Fractal(double realLower, double realUpper, double imagLower, double imagUpper) {
        this.realLow = realLower;
        this.realUp = realUpper;
        this.imagLow = imagLower;
        this.imagUp = imagUpper;
        this.maxIterations = MAX_ITERATIONS;

        this.threads = new Thread[RENDERING_THREADS];

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

    /**
     * draw the Fractal image on the complex plane
     * according to the function called by FunctionOfZ
     * draw the zoomRectangle if dragging
     */
    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        //do not re-render if dragging on it
        if(!isDragging())
            render();

        //draw the image after gaining the lock
        synchronized (image){
            g2.drawImage(image,0,0,null);
        }

        // draw the zoom rectangle if dragging
        if (isDragging())
        {
            //set transparency
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
            g2.setStroke(new BasicStroke(2));
            g2.setPaint(Color.WHITE);

            g2.draw(fl.r);
            g2.fill(fl.r);
        }
    }

    /**
     * Start anumber of threads to
     * render the image splitting it
     * evenly between the threads
     */
    private void render()
    {
        //initialise image
        image = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);

        //start rendering threads
        for (int i = 0; i < RENDERING_THREADS; i++) {
            threads[i] = new Thread(new RenderThread(i));
            threads[i].start();
        }

        /*
         * motivate the garbage collector so that it puts more
         * effort in removing unused threads or images ;)
         */
        Runtime.getRuntime().gc();

        //Wait until all threads are done
        synchronized (image)
        {
            while(threadsCompleted<RENDERING_THREADS){
                try{
                    image.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                    //exit the loop if interrupted so the whole program doesn't crash
                    break;
                }
            }
            threadsCompleted = 0;
        }
    }

    /**
     * Check if the function of Z escapes the BAILOUT limit.
     * Computes how close the point c is to the set.
     * should call functionOfZ and getSmoothIteration
     *
     * @param c the point on the complex plane we are checking
     * @return smooth iteration value
     */
    public abstract double compute(Complex c);

    /**
     * this method calls the function of Z that defines the
     * fractal set on the complex plane
     *
     * @param c is the complex variable
     * @param z is the starting point
     * @return the resulting complex
     */
    public abstract Complex functionOfZ(Complex z, Complex c);

    /**
     * Smooth colouring algorithm:
     * guarantees that the colors are continuous and don't create bands
     * used the algorithm as found on the wikipedia page f the mandelbrot set
     * and tweaked by me to obtain the desired effect
     *
     * @param iterations the number of computed iterations before escaping
     * @param z the final magnitude before exiting the loop
     * @return a double value between iterations-1 and iterations
     * divided by a constant factor that is used to make the colors cycle slowly
     */
    public double getSmoothIterations(double iterations, Complex z)
    {
        //EXP is the exponent in the function of Z which represents the divergence
        int EXP = 2; //exp is 2 because most functions have .square on z
        if (this instanceof Multibrot)
            EXP = ((Multibrot)this).getN() + 1;

        /*
         * divide by this factor to make the palette cycle slowly
         * I designed this equation studying the curve of Bailout*log(EXP)
         * knowing that I would have obtained the effect i wanted multiplying
         * the full length of a palette by a fraction of it
         */
        int slow = palette.length * (int)Math.ceil(palette.length/(BAILOUT*Math.log(EXP)));

        //the algorithm suggested by wikipedia
        if (iterations < getMaxIterations()) {
            double log_zn = Math.log(z.modulusSquare()) / EXP;
            double nu = Math.log(log_zn / Math.log(BAILOUT)) / Math.log(EXP);
            iterations = (iterations - nu) / slow; //divided by slow factor to slow the color cycle
        }

        return iterations;
    }

    /**
     * interpolate two RGB colors
     *
     * @param smoothIteration the smooth value of iterations
     * @return resulting color
     */
    public Color RgbLinearInterpolate(int[] start, int[] end, double smoothIteration) {
        // linear interpolation lerp (r,a,b) = (1-r)*a + r*b = (1-r)*(ax,ay,az) + r*(bx,by,bz)
        double r = smoothIteration - Math.floor(smoothIteration); //get just decimal part
        double nr = 1.0 - r;
        //add the modulus to guarantee it's an acceptable colour
        double R = ((nr * start[0]) + (r * end[0])) % 256;
        double G = ((nr * start[1]) + (r * end[1])) % 256;
        double B = ((nr * start[2]) + (r * end[2])) % 256;

        return new Color((int) R, (int) G, (int) B);
    }

    /**
     * Runnable that calculates part of the image.
     * It only draws the lines that return the same modulo
     * value of the index of the Thread that is executing it.
     */
    private class RenderThread implements Runnable
    {
        //construct by index
        int i;
        public RenderThread(int i){this.i = i;}

        @Override
        public void run()
        {
            //loop through each row
            for (int y = 0; y < getHeight(); y++) {
                //only draw the rows that the thread has to
                if (y % RENDERING_THREADS == i)
                    for (int x = 0; x < getWidth(); x++)
                    {
                        int[][] palette = getPalette();

                        //compute running the function of Z
                        double it = getMaxIterations() - compute(getComplex(x, y));

                        //prepare interpolation
                        int itfloor = (int) Math.floor(it);
                        int[] color1 = palette[itfloor % palette.length];
                        int[] color2 = palette[(itfloor + 1) % palette.length];

                        //interpolate between 2 adiacent color in the palette
                        Color col = RgbLinearInterpolate(color1, color2, it);

                        //draw pixel on image
                        image.setRGB(x, y, col.getRGB());
                    }
            }
            //increment completed threads and wake main thread
            threadsCompleted++;
            synchronized (image){image.notify();}
        }
    }

    /**
     * do static zoom
     * @param SCALE , the higher the shortest distance zoom
     * @param in specifies the direction of the zoom
     */
    public void zoom(int SCALE,boolean in)
    {
        //set shifts
        double xShift = (getRealUp() - getRealLow())/SCALE;
        double yShift = (getImagUp() - getImagLow())/SCALE;

        //check direction
        if(in){
            xShift = -xShift;
            yShift = -yShift;
        }

        //set bounds
        setRealLow(getRealLow() - xShift);
        setRealUp(getRealUp() + xShift);
        setImagLow(getImagLow() - yShift);
        setImagUp(getImagUp() + yShift);

        //update gui
        getGUI().getSettings().updateSet();
        repaint();
    }

    /*
     * return the point in the complex plane that
     * corresponds to a given pixel
     */
    public Complex getComplex(int x, int y) {
        double cx = (realUp - realLow) * x / getWidth() + realLow;
        //this is inverted so that the y axis goes in the right way
        double cy = (imagLow - imagUp) * y / getHeight() + imagUp;
        return new Complex(cx, cy);
    }

    //overload of getComplex(x,y) with point
    public Complex getComplex(Point p) {
        return getComplex(p.x, p.y);
    }

    /*
     * accessor methods
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

    public BufferedImage getImage() {
        return image;
    }

    public FractalGUI getGUI() {
        return (FractalGUI) SwingUtilities.getWindowAncestor(this);
    }

    //return true if the user started dragging
    public boolean isDragging(){return fl.startDrag != fl.endDrag && fl.endDrag != null;}

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
         * on mouse pressed prepare for drag
         */
        @Override
        public void mousePressed(MouseEvent e) {
            startDrag = new Point(e.getX(), e.getY());
            endDrag = startDrag;
        }

        /*
         * when mouse is released, zoom if it was dragged
         * then reset drag constants
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
         * while the mouse is being dragged draw the zooming area rectangle
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

        //make sure the zoom rectangle startDrag is top left
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
    private class ZoomRun implements Runnable
    {
        private Complex start,end, topBound, botBound;
        //it will run 40 iterations to finish the zoom
        //the higher the smoother yet slower zoom animation
        private final double ITERATIONS = 40; //must be greater than 0

        //the constructor sets the bounds and destination points
        public ZoomRun(Point start, Point end){
            this.start = getComplex(start);
            this.end = getComplex(end);
            this.botBound = new Complex(getRealLow(),getImagLow());
            this.topBound = new Complex(getRealUp(),getImagUp());
        }

        //zoom by 1/ITERATIONS of the total zoom each iteration
        @Override
        public void run()
        {
            //set the shift constants
            double horShiftStart = (start.getX() - botBound.getX()) / ITERATIONS;
            double verShiftStart = (topBound.getY() - start.getY()) / ITERATIONS;
            double horShiftEnd = (topBound.getX() - end.getX()) / ITERATIONS;
            double verShiftEnd = (end.getY() - botBound.getY()) / ITERATIONS;

            //zoom until it matches the bounds
            while ((topBound.getX()>end.getX() && topBound.getY()>start.getY())
                || (botBound.getX()<start.getX() && botBound.getY()<end.getY()))
            {
                topBound = new Complex(topBound.getX() - horShiftEnd, topBound.getY() - verShiftStart);
                botBound = new Complex(botBound.getX() + horShiftStart, botBound.getY() + verShiftEnd);

                //set new bounds
                setRealLow(botBound.getX());
                setRealUp(topBound.getX());
                setImagLow(botBound.getY());
                setImagUp(topBound.getY());

                //update ui
                repaint();
                getGUI().getSettings().updateSet();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}