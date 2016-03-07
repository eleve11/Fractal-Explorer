import java.awt.*;

/**
 * GUI that shows the Mandelbrot Set
 */
public class MainFrame extends FractalGUI
{
    Mandelbrot fractal;
    Container pane;

    public MainFrame(){
        super("Mandelbrot Set",new Mandelbrot());
        fractal = (Mandelbrot) getFractal();
        pane = this.getContentPane();
        init();
        this.setVisible(true);
    }

    public void setFractal(Mandelbrot fractal) {
        this.fractal.setVisible(false);
        setTitle(fractal instanceof BurningShip ? "Burning Ship" : "Mandelbrot Set");
        this.fractal = fractal;
        this.setSettings(new SettingsPanel(fractal));
        init();
    }

    @Override
    public void init()
    {
        pane.add(fractal,BorderLayout.CENTER);
        pane.add(getSettings(),BorderLayout.WEST);
    }
}