import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel that manages the settigs of the complex plane.
 */
public class SettingsPanel extends JPanel
{
    private JTextField realLow,realUp,imagLow,imagUp,iterations;
    private JLabel lastClicked;
    private Fractal fractal;
    private GridBagConstraints c;

    //construct
    public SettingsPanel(Fractal fractal){
        this.fractal = fractal;
        this.setLayout(new GridBagLayout());
        init();
        this.setVisible(true);
    }

    //initialise
    private void init()
    {
        //initialise components
        realUp = new JTextField(fractal.getRealUp().toString(),5);
        realLow = new JTextField(fractal.getRealLow().toString(),5);
        imagUp = new JTextField(fractal.getImagUp().toString(),5);
        imagLow = new JTextField(fractal.getImagLow().toString(),5);
        iterations = new JTextField(fractal.getMaxIterations().toString(),3);
        lastClicked = new JLabel();
        lastClicked.setHorizontalAlignment(SwingConstants.CENTER);

        //prepare inner panels
        JPanel realaxis = getBoundPanel(new JLabel("R-axis bounds:"),realLow,realUp);
        JPanel imagaxis = getBoundPanel(new JLabel("I-axis bounds:"),imagLow,imagUp);
        JPanel iterbox = getIterPanel(new JLabel("Iterations:"),iterations);

        //GridBagLayout settings
        c = new GridBagConstraints();

        c.gridy = 0;

        //radio buttons and favourites list cannot go in JuliaFrames
        if(fractal instanceof MainFractal) {
            FractalChoose fc = new FractalChoose((MainFractal) fractal);
            this.add(fc,c);

            addFavourites();
        }

        //add other settings componentss
        c.gridy = 1;
        this.add(realaxis, c);
        c.gridy = 2;
        this.add(imagaxis, c);
        c.gridy = 3;
        this.add(iterbox, c);
        c.gridy = 7;
        this.add(lastClicked,c);
        c.gridy = 8;
        this.add(new SaveButton(fractal),c);

        //add listener to update image
        SettingsListener setListener = new SettingsListener();
        realLow.addActionListener(setListener);
        realUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        imagUp.addActionListener(setListener);
        imagLow.addActionListener(setListener);
        iterations.addActionListener(setListener);
    }

    /*
     * add favourites to the settings panel
     */
    private void addFavourites(){
        //favourites label
        JLabel favLabel = new JLabel("Favourite Julia Sets:");
        favLabel.setFont(favLabel.getFont().deriveFont(Font.BOLD));

        // display favourites
        JComboBox favList = Favourites.getInstance().getFavourites();
        favList.setSelectedIndex(-1); //set initial value
        favList.addActionListener(new ShowFav());
        favList.setFocusable(false); //don't allow key type

        //add to panel
        c.gridy = 5;
        c.weightx = 0.5;
        this.add(favLabel,c);
        c.weightx = 1;
        c.gridy = 6;
        this.add(favList,c);
    }

    /*
     * return a panel that handles the bounds, given 3 necessary components
     */
    private JPanel getBoundPanel(JLabel label, JTextField lower, JTextField upper)
    {
        //init
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints panConstr = new GridBagConstraints();
        label.setFont(label.getFont().deriveFont(Font.BOLD,14.0f));
        JLabel min = new JLabel("Min:");
        JLabel max = new JLabel("Max:");

        //ADD to subpanel
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

    //return the panel that manages and displays current maxIterations
    private JPanel getIterPanel(JLabel label,JTextField value)
    {
        //init
        GridBagConstraints constr = new GridBagConstraints();
        JPanel panel = new JPanel();
        label.setFont(label.getFont().deriveFont(Font.BOLD));

        //add
        constr.gridy = 0;
        constr.gridx = 0;
        constr.weightx = 0.5;
        panel.add(label,constr);
        constr.gridx = 1;
        panel.add(value,constr);
        return panel;
    }

    //update the last clicked point label
    public void updatePointLabel(Complex lastPoint){
        //label text is using HTML tags because
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

    /*
     * listens to the texFields, when pressed enter it updates the fractal
     */
    private class SettingsListener implements ActionListener
    {
        //update fractal when a setting is confirmed
        @Override
        public void actionPerformed(ActionEvent e)
        {
            fractal.setRealLow(Double.parseDouble(realLow.getText()));
            fractal.setRealUp(Double.parseDouble(realUp.getText()));
            fractal.setImagLow(Double.parseDouble(imagLow.getText()));
            fractal.setImagUp(Double.parseDouble(imagUp.getText()));
            fractal.setMaxIterations(Integer.parseInt(iterations.getText()));

            fractal.repaint();
            fractal.requestFocus();
        }
    }

    /*
     * action listener for the Favourites ComboBox
     */
    private class ShowFav implements ActionListener
    {
        //Display a Julia Frame containing the selected favourite Julia set
        @Override
        public void actionPerformed(ActionEvent e) {
            //action only if index != -1 (initial condition)
            if(((JComboBox)e.getSource()).getSelectedIndex()!= -1) {
                List<Complex> complexList = Favourites.getInstance().getFavList();
                Complex target = complexList.get(((JComboBox) e.getSource()).getSelectedIndex());

                JuliaFrame.getInstance().updateJulia(target);
            }
        }
    }
}