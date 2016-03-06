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
    private JLabel lastClicked;
    private Fractal fractal;
    private GridBagConstraints c;

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
        lastClicked = new JLabel();

        //layout components
        this.setLayout(new GridBagLayout());
        JPanel realaxis = getBoundPanel(new JLabel("R-axis bounds:"),realLow,realUp);
        JPanel imagaxis = getBoundPanel(new JLabel("I-axis bounds:"),imagLow,imagUp);
        JPanel iterbox = getIterPanel(new JLabel("Iterations:"),iterations);

        //GridBagLayout settings
        c = new GridBagConstraints();

        c.gridy = 0;
        this.add(realaxis, c);
        c.gridy = 2;
        this.add(imagaxis, c);
        c.gridy = 4;
        this.add(iterbox, c);

        //can only show favourites if not on a JuliaSet
        if(!(fractal instanceof JuliaSet))
            addFavourites();

        c.gridy = 7;
        this.add(lastClicked,c);

        //add listener to update image
        SettingsListener setListener = new SettingsListener();
        realLow.addActionListener(setListener);
        realUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        imagUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        iterations.addActionListener(setListener);
    }

    private void addFavourites(){
        //Display favourites
        JLabel favLabel = new JLabel("Favourites:");
        favLabel.setFont(favLabel.getFont().deriveFont(Font.BOLD));
        JComboBox favList = new FavComboBox(fractal);

        c.gridy = 5;
        c.weightx = 0.5;
        this.add(favLabel,c);
        c.weightx = 1;
        c.gridy = 6;
        this.add(favList,c);
    }

    private JPanel getBoundPanel(JLabel label, JTextField lower, JTextField upper){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints panConstr = new GridBagConstraints();
        label.setFont(label.getFont().deriveFont(Font.BOLD,14.0f));
        JLabel min = new JLabel("Min:");
        JLabel max = new JLabel("Max:");

        //set GridBagLayouot Constraints
        panConstr.gridwidth = 2;
        panConstr.gridx = 0;
        panConstr.gridy = 0;
        panel.add(label,panConstr);

        panConstr.gridwidth =1;
        panConstr.weightx = 0.5;
        panConstr.gridy = 1;
        panConstr.gridx = 0;
        panel.add(min,panConstr);
        panConstr.gridx = 1;
        panel.add(lower,panConstr);

        panConstr.gridy = 2;
        panConstr.gridx = 0;
        panel.add(max,panConstr);
        panConstr.gridx = 1;
        panel.add(upper,panConstr);
        return panel;
    }

    private JPanel getIterPanel(JLabel label,JTextField value){
        GridBagConstraints constr = new GridBagConstraints();
        JPanel panel = new JPanel();
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        constr.gridy = 0;
        constr.gridx = 0;
        constr.weightx = 0.5;
        panel.add(label,constr);
        constr.gridx = 1;
        panel.add(value,constr);
        return panel;
    }

    public void updatePointLabel(Complex lastPoint){
        //nasty label text is using HTML tags because
        //JLabels don't like going to a new line :(
        if(lastPoint!=null)
            lastClicked.setText("<html>Last Selected Point:<br>"+lastPoint.toString()+"</html>");
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
        public void actionPerformed(ActionEvent e)
        {
            fractal.setRealLow(Double.parseDouble(realLow.getText()));
            fractal.setRealUp(Double.parseDouble(realUp.getText()));
            fractal.setImagLow(Double.parseDouble(imagLow.getText()));
            fractal.setImagUp(Double.parseDouble(imagUp.getText()));
            fractal.setMaxIterations(Integer.parseInt(iterations.getText()));

            fractal.repaint();
        }
    }
}
