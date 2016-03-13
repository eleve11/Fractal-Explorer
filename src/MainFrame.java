import java.awt.*;

/**
 * GUI that shows the Mandelbrot Set
 */
public class MainFrame extends FractalGUI
{
    MainFractal fractal;
    Container pane;

    //default constructor
    public MainFrame(){
        super("Mandelbrot",new Mandelbrot());
        fractal = (Mandelbrot) getFractal();
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
        this.fractal.requestFocus();
    }

    //override set fractal : updates the whole frame
    @Override
    public void setFractal(Fractal fractal)
    {
        if(!(fractal instanceof MainFractal))
            throw new IllegalArgumentException();
        this.fractal.setVisible(false);

        //update fractal
        this.fractal = (MainFractal) fractal;
        this.setTitle(((MainFractal) fractal).getTitle());

        //update settings
        getSettings().setFractal(fractal);

        init();
    }
}