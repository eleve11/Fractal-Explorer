import javax.swing.*;
import javax.swing.border.TitledBorder;
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
    private JPanel pointPanel;
    private Fractal fractal;

    //construct
    public SettingsPanel(Fractal fractal){
        this.fractal = fractal;
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        TitledBorder title = new TitledBorder("Settings");
        title.setTitleFont(title.getTitleFont().deriveFont(Font.BOLD));
        this.setBorder(title);

        init();
        this.setVisible(true);
    }

    //initialise
    private void init()
    {
        //initialise components
        realUp = new JTextField(fractal.getRealUp().toString(),6);
        realLow = new JTextField(fractal.getRealLow().toString(),6);
        imagUp = new JTextField(fractal.getImagUp().toString(),6);
        imagLow = new JTextField(fractal.getImagLow().toString(),6);
        iterations = new JTextField(fractal.getMaxIterations().toString(),6);
        lastClicked = new JLabel();
        lastClicked.setHorizontalAlignment(SwingConstants.CENTER);
        pointPanel = getPointPanel();

        //prepare inner panels
        JPanel realaxis = getBoundPanel("Real axis bounds",realLow,realUp);
        JPanel imagaxis = getBoundPanel("Imaginary axis bounds",imagLow,imagUp);
        JPanel iterbox = getIterPanel(new JLabel("Iterations:"),iterations);

        //radio buttons and favourites list cannot go in JuliaFrames
        if(fractal instanceof MainFractal) {
            FractalChoose fc = new FractalChoose((MainFractal) fractal);
            this.add(fc);

            addFavourites();
        }

        //add other settings components
        this.add(realaxis);
        this.add(imagaxis);
        this.add(iterbox);
        lastClicked.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(pointPanel);

        JButton save = new SaveButton(fractal);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(save);

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
    private void addFavourites()
    {
        // display favourites
        JComboBox favList = Favourites.getInstance().getFavourites();
        favList.setSelectedIndex(-1); //set initial value
        favList.addActionListener(new ShowFav());
        favList.setFocusable(false); //don't allow key type

        favList.setBorder(new TitledBorder("Favourite Julia Sets"));
        this.add(favList);
    }

    /*
     * return a panel that handles the bounds, given 3 necessary components
     */
    private JPanel getBoundPanel(String title,JTextField lower, JTextField upper)
    {
        //init
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));
        panel.setBorder(new TitledBorder(title));
        JLabel min = new JLabel("Min:");
        JLabel max = new JLabel("Max:");
        //ADD to subpanel
        panel.add(min);
        panel.add(lower);
        panel.add(max);
        panel.add(upper);
        return panel;
    }

    //return the panel that manages and displays current maxIterations
    private JPanel getIterPanel(JLabel label,JTextField value)
    {
        //init
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        panel.setBorder(new TitledBorder("Max Iterations:"));
        //add
        panel.add(label);
        panel.add(value);
        return panel;
    }

    //update the last clicked point label
    public void updatePointLabel(Complex lastPoint){
        if(lastPoint!=null){
            lastClicked.setText(lastPoint.toString());
            pointPanel.setVisible(true);
        }
    }

    private JPanel getPointPanel(){
        JPanel point = new JPanel();
        point.setBorder(new TitledBorder("Last Clicked Point"));
        point.add(lastClicked);
        point.setVisible(false);
        return point;
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

    public void setFractal(Fractal fractal){
        this.fractal = fractal;
        updateSet();
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

                JuliaFrame.getInstance().updateJulia(target,(MainFractal)fractal);
            }
        }
    }
}