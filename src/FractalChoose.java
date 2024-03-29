import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A group of radio buttons to switch between the fractals in the main frame.
 * It makes it incredibly easy to add new fractals!
 */
public class FractalChoose extends JPanel {

    private ButtonGroup bg;
    private ChooseFractalLis lis;

    //constructor
    public FractalChoose() {
        this.setLayout(new GridLayout(4,1));

        setBorder(new TitledBorder("Choose a Fractal"));

        bg = new ButtonGroup();
        lis = new ChooseFractalLis();

        init();
    }

    //add components to the panel
    private void init()
    {
        //look how easy it is to add new fractals!
        addButton(new Mandelbrot());
        addButton(new BurningShip());
        addButton(new Buffalo());

        //for the multibrots I created a custom component
        this.add(new FRadioText("Multibrot"));

        //autoselect the first item in the group
        if(bg.getButtonCount()>0)
            bg.setSelected(bg.getElements().nextElement().getModel(),true);
    }

    //add a radio button representing the fractal
    public void addButton(MainFractal fractal){
        JRadioButton radio = new JRadioButton();
        radio.setText(fractal.getClass().getName());
        bg.add(radio);
        this.add(radio);
        radio.addActionListener(lis);
    }

    /**
     * set the displayed fractal to a new instance of the fractal represented
     * by the JRadioButton that has been selected
     */
    private class ChooseFractalLis implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MainFrame gui = (MainFrame) SwingUtilities.getWindowAncestor(FractalChoose.this);
            String name = ((JRadioButton)e.getSource()).getText();

            //Create a new fractal based on the name of the button
            try {
                Class c = Class.forName(name);
                gui.setFractal((MainFractal) c.newInstance());
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }

        }
    }

    /**
     * custom JComponent to allow multibrots
     */
    private class FRadioText extends JPanel
    {
        private JRadioButton button;
        private JFormattedTextField text;
        private NumberFormatter nf; //to format the jTexField

        //default constructor
        public FRadioText(String title)
        {
            this.button = new JRadioButton(title);

            nf = new NumberFormatter();
            nf.setValueClass(Integer.class);
            nf.setMinimum(1);//get illegal argument otherwise
            nf.setMaximum(100);//can't handle more than that

            this.text = new JFormattedTextField(nf);
            text.setColumns(2);

            this.setLayout(new GridLayout(1,2));
            init();
        }

        private void init()
        {
            //add the components
            bg.add(button);
            this.add(button);
            this.add(text);
            button.setFocusable(false);

            //update fractal when both is selected and has a number input
            ActionListener radioTextLis = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame gui = (MainFrame) SwingUtilities.getWindowAncestor(FractalChoose.this);

                    if(button.isSelected() && !text.getText().equals(""))
                        gui.setFractal(new Multibrot(Integer.parseInt(text.getText())));
                }
            };

            //attach listeners
            text.addActionListener(radioTextLis);
            button.addActionListener(radioTextLis);
        }
    }
}
