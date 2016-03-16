import java.awt.*;

/**
 * GUI that shows the Mandelbrot Set
 */
public class MainFrame extends FractalGUI
{
    private MainFractal fractal;
    private Container pane;

    //default constructor starts a mandelbrot
    public MainFrame(){
        super("Mandelbrot",new Mandelbrot());
        fractal = (MainFractal) getFractal();
        pane = this.getContentPane();
        init();
        this.setVisible(true);
    }

    //add components
    @Override
    public void init()
    {
        pane.add(fractal,BorderLayout.CENTER);
        pane.add(getScrollSets(),BorderLayout.WEST);
        this.fractal.requestFocus(); //for the keyLis
    }

    //override set fractal : updates the whole frame
    @Override
    public void setFractal(Fractal fractal)
    {
        //cannot set a julia fractal
        if(!(fractal instanceof MainFractal))
            throw new IllegalArgumentException();

        //hide old fractal
        this.fractal.setVisible(false);

        //update fractal
        this.fractal = (MainFractal) fractal;
        this.setTitle(((MainFractal) fractal).getTitle());
        pane.add(fractal,BorderLayout.CENTER);

        //update settings
        getSettings().setFractal(fractal);

        this.fractal.requestFocus(); //for the keyListener
    }
}