import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A group of radio buttons to switch between the fractals in the main frame.
 * It makes it incredibly easy to add new fractals!
 */
public class FractalChoose extends JPanel {

    private ButtonGroup bg;
    private JLabel label;
    private MainFractal fractal;
    private ChooseFractalLis lis;

    //constructor need the fractal that is preselected
    public FractalChoose(MainFractal preselected) {
        this.fractal = preselected;
        this.setLayout(new GridLayout(5,1));

        label = new JLabel("Choose a Fractal:");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14.0f));

        bg = new ButtonGroup();
        lis = new ChooseFractalLis();

        init();
    }

    private void init()
    {
        this.add(label);

        //look how easy it is to add new fractals!
        addButton(new Mandelbrot());
        addButton(new BurningShip());
        addButton(new Octabrot());

        select(fractal);
    }

    //add a radio button representing the fractal
    public void addButton(MainFractal fractal){
        JRadioButton radio = new JRadioButton();
        radio.setText(fractal.getClass().getName());
        bg.add(radio);
        this.add(radio);
        radio.addActionListener(lis);
    }

    //preselect the main fractal
    private void select(MainFractal fractal) {
        JRadioButton button = new JRadioButton();
        for(Component c : getComponents())
            if(c instanceof JRadioButton && ((JRadioButton) c).getText().equals(fractal.getClass().getName()))
                button = (JRadioButton) c;
        bg.setSelected(button.getModel(), true);
    }

    /**
     * set the gui fractal to a new instance of the fractal represented
     * by the JRadioButton that has been selected
     */
    private class ChooseFractalLis implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MainFrame gui = (MainFrame) SwingUtilities.getWindowAncestor(FractalChoose.this);
            String name = ((JRadioButton)e.getSource()).getText();

            //Woah lots of exceptions
            try {
                Class c = Class.forName(name);
                gui.setFractal((MainFractal)c.newInstance());
            } catch (ClassNotFoundException e1){
                e1.printStackTrace();
            }catch(InstantiationException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }
        }
    }
}
