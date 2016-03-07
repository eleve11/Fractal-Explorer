import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A group of radio buttons to switch between the fractals in the main frame.
 */
public class FractalRadio extends JPanel {

    private JRadioButton mandelbrot,burningship,other;
    private ButtonGroup bg;
    private JLabel label;
    private JTextField function;

    public FractalRadio() {
        label = new JLabel("Choose a Fractal:");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15.0f));

        mandelbrot = new JRadioButton("Mandelbrot");
        burningship = new JRadioButton("Burning Ship");
        other = new JRadioButton("Other");
        bg = new ButtonGroup();

        this.setLayout(new GridBagLayout());
        init();
    }

    private void init()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy =0;
        this.add(label,c);
        c.gridy =1;
        this.add(mandelbrot,c);
        c.gridy =2;
        this.add(burningship,c);
        //this.add(other);

        bg.add(mandelbrot);
        bg.add(burningship);
        //bg.add(other);
        bg.setSelected(mandelbrot.getModel(),true); //mandelbrot as default

        ChooseFractalLis lis = new ChooseFractalLis();
        mandelbrot.addActionListener(lis);
        burningship.addActionListener(lis);
//        other.addActionListener(lis);
    }

    private class ChooseFractalLis implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MainFrame gui = (MainFrame) SwingUtilities.getWindowAncestor(FractalRadio.this);

            if(e.getSource().equals(mandelbrot))
                gui.setFractal(new Mandelbrot());

            if(e.getSource().equals(burningship))
                gui.setFractal(new BurningShip());
        }
    }
}
