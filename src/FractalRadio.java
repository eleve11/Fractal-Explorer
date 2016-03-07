import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A group of radio buttons to switch between the fractals in the main frame.
 */
public class FractalRadio extends JPanel {

    private ButtonGroup bg;
    private JLabel label;
    private MainFractal fractal;
    private int yCount = 1;
    private GridBagConstraints c;
    private ChooseFractalLis lis;

    public FractalRadio(MainFractal preselected) {
        this.fractal = preselected;
        this.setLayout(new GridBagLayout());

        label = new JLabel("Choose a Fractal:");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15.0f));

        bg = new ButtonGroup();
        lis = new ChooseFractalLis();
        c = new GridBagConstraints();


        init();
    }

    private void init()
    {
        c.gridy =0;
        this.add(label,c);

        //look how easy it is to add new fractals!
        addButton(new Mandelbrot());
        addButton(new BurningShip());
        addButton(new Hexabrot());
        addButton(new Octabrot());

        select(fractal);
    }

    private void addButton(MainFractal fractal){
        JRadioButton radio = new JRadioButton();
        radio.setText(fractal.getClass().getName());
        bg.add(radio);
        c.gridy = yCount;
        this.add(radio,c);
        yCount++;
        radio.addActionListener(lis);
    }

    private void select(MainFractal fractal) {
        JRadioButton button = new JRadioButton();
        for(Component c : getComponents())
            if(c instanceof JRadioButton && ((JRadioButton) c).getText().equals(fractal.getClass().getName()))
                button = (JRadioButton) c;
        bg.setSelected(button.getModel(), true);
    }

    private class ChooseFractalLis implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MainFrame gui = (MainFrame) SwingUtilities.getWindowAncestor(FractalRadio.this);
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
