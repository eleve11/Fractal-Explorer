import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that manages the settigs of the complex plane.
 */
//TODO: comments
public class SettingsPanel extends JPanel
{
    private JTextField realLow,realUp,imagLow,imagUp,iterations;
    private Fractal fractal;
    private GridBagConstraints panConstr; //don't really like this one here

    public SettingsPanel(Fractal fractal){
        this.fractal = fractal;
        init();
        this.setVisible(true);
    }

    private void init()
    {
        //display current settings
        realUp = new JTextField(fractal.getRealUp().toString(),5);
        realLow = new JTextField(fractal.getRealLow().toString(),5);
        imagUp = new JTextField(fractal.getImagUp().toString(),5);
        imagLow = new JTextField(fractal.getImagLow().toString(),5);
        iterations = new JTextField(fractal.getMaxIterations().toString(),3);

        //layout components
        this.setLayout(new GridBagLayout());
        JPanel realaxis = getSetPanel(new JLabel("R-axis bounds:"),realLow,realUp);
        JPanel imagaxis = getSetPanel(new JLabel("I-axis bounds:"),imagLow,imagUp);
        JPanel iterbox = getSetPanel(new JLabel("Iterations:"),iterations);

        //GridBagLayout settings
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.4;
        this.add(realaxis, c);
        this.add(imagaxis, c);
        c.weightx = 0.2;
        this.add(iterbox, c);

        //add listener to update image
        SettingsListener setListener = new SettingsListener();
        realLow.addActionListener(setListener);
        realUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        imagUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        iterations.addActionListener(setListener);
    }

    private JPanel getSetPanel(JLabel label, JTextField textbox){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panConstr = new GridBagConstraints();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        //set GridBagLayouot Constraints
        panConstr.gridwidth = 2;
        panConstr.gridx = 0;
        panConstr.gridy = 0;
        panel.add(label,panConstr);

        panConstr.gridwidth = 1;
        panConstr.weightx = 0.5;
        panConstr.gridy = 1;
        panConstr.gridx = 0;
        panel.add(textbox,panConstr);
        return panel;
    }

    private JPanel getSetPanel(JLabel label,JTextField lower, JTextField upper){
        JPanel panel = getSetPanel(label, lower);
        //use previews constraints plus this
        panConstr.gridx = 1;
        panel.add(upper,panConstr);
        return panel;
    }

    //update the settings panel when someone interacts with the fractal
    public void updateSet()
    {
        realLow.setText(fractal.getRealLow().toString());
        realUp.setText(fractal.getRealUp().toString());
        imagLow.setText(fractal.getImagLow().toString());
        imagUp.setText(fractal.getImagUp().toString());
        iterations.setText(fractal.getMaxIterations().toString());
    }

    private class SettingsListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {

            fractal.setRealLow(Double.parseDouble(realLow.getText()));
            fractal.setRealUp(Double.parseDouble(realUp.getText()));
            fractal.setImagLow(Double.parseDouble(imagLow.getText()));
            fractal.setImagUp(Double.parseDouble(imagUp.getText()));
            fractal.setMaxIterations(Integer.parseInt(iterations.getText()));

            fractal.repaint();
        }
    }
}
