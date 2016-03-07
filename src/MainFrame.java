import java.awt.*;

/**
 * GUI that shows the Mandelbrot Set
 */
public class MainFrame extends FractalGUI
{
    MainFractal fractal;
    Container pane;

    public MainFrame(){
        super("Mandelbrot Set",new Mandelbrot());
        fractal = (Mandelbrot) getFractal();
        pane = this.getContentPane();
        init();
        this.setVisible(true);
    }

    @Override
    public void setFractal(Fractal fractal) {
        if(!(fractal instanceof MainFractal))
            throw new IllegalArgumentException();
        this.fractal.setVisible(false);
        //use their title
        setTitle(fractal instanceof BurningShip  ? "Burning Ship" : "Mandelbrot Set");
        this.fractal = (MainFractal) fractal;
        getSettings().setVisible(false);
        this.setSettings(new SettingsPanel(fractal));
        init();
    }

    @Override
    public void init()
    {
        pane.add(fractal,BorderLayout.CENTER);
        pane.add(getSettings(),BorderLayout.WEST);
        this.fractal.requestFocus();
    }
}