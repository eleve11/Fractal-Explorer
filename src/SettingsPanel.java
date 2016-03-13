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
//    private JSlider colorslider;
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
        realUp = new JTextField(fractal.getRealUp().toString(),6);
        realLow = new JTextField(fractal.getRealLow().toString(),6);
        imagUp = new JTextField(fractal.getImagUp().toString(),6);
        imagLow = new JTextField(fractal.getImagLow().toString(),6);
        iterations = new JTextField(fractal.getMaxIterations().toString(),6);
        lastClicked = new JLabel();
        lastClicked.setHorizontalAlignment(SwingConstants.CENTER);
//        colorslider = getColorSlider();

        //prepare inner panels
        JPanel realaxis = getBoundPanel("Real axis bounds",realLow,realUp);
        JPanel imagaxis = getBoundPanel("Imaginary axis bounds",imagLow,imagUp);
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
//        this.add(colorslider,c);
        c.gridy = 8;
        this.add(lastClicked,c);
        c.gridy = 9;
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
    private void addFavourites()
    {
        // display favourites
        JComboBox favList = Favourites.getInstance().getFavourites();
        favList.setSelectedIndex(-1); //set initial value
        favList.addActionListener(new ShowFav());
        favList.setFocusable(false); //don't allow key type

        favList.setBorder(new TitledBorder("Favourite Julia Sets"));

        c.weightx = 1;
        c.gridy = 6;
        this.add(favList,c);
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
        //label text is using HTML tags because
        //JLabels don't like going to a new line :(
        if(lastPoint!=null){
            lastClicked.setBorder(new TitledBorder("Last Clicked:"));
            lastClicked.setSize(this.getWidth(),lastClicked.getHeight());
            lastClicked.setText(lastPoint.toString());
        }
            //lastClicked.setText("<html>Last Selected Point:<br>"+lastPoint.toString()+"</html>");
    }

//    private JSlider getColorSlider(){
//        JSlider color = new JSlider(0,fractal.getPalette().length*100);
//        color.setFocusable(false);
//        color.setBorder(new TitledBorder("Color"));
//        color.setValue((int) fractal.getColorOffset()*100);
//        color.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                fractal.setColorOffset((double)((JSlider)e.getSource()).getValue()/100);
//                fractal.repaint();
//            }
//        });
//        return color;
//    }

    //update the settings panel when someone interacts with the fractal
    public void updateSet()
    {
        realLow.setText(fractal.getRealLow().toString());
        realUp.setText(fractal.getRealUp().toString());
        imagLow.setText(fractal.getImagLow().toString());
        imagUp.setText(fractal.getImagUp().toString());
        iterations.setText(fractal.getMaxIterations().toString());
//        colorslider.setValue((int)(fractal.getColorOffset()*100));
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