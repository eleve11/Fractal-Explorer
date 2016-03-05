import javax.swing.*;
import java.awt.*;

/**
 * GUI that shows the Mandelbrot Set
 */
public class MainFrame extends FractalGUI
{
    Fractal fractal;

    public MainFrame(){
        super("Mandelbrot Set",new Mandelbrot());
        fractal = getFractal();
        init();
        this.setVisible(true);
    }

    @Override
    public void init() {
        Container pane = this.getContentPane();
        pane.add(fractal,BorderLayout.CENTER);
        //pane.add(getSettings(),BorderLayout.SOUTH);
        getLastPoint().setHorizontalAlignment(SwingConstants.CENTER);
        add(getLastPoint(),BorderLayout.SOUTH);
    }
}