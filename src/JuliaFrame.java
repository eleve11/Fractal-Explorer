import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Singleton GUI that shows the Julia Set
 */
public class JuliaFrame extends FractalGUI
{
    private JuliaSet julia;
    private JPanel toolBar;
    private boolean showSettings;

    public static final Dimension DEFAULT_SIZE = new Dimension(400,400);

    //let the frame be a singleton
    private static JuliaFrame instance = new JuliaFrame();

    //private constructor because is a singleton
    private JuliaFrame(){
        super("Filled Julia Set", new JuliaSet(new Complex(0,0),new Mandelbrot()) ,DEFAULT_SIZE.width,DEFAULT_SIZE.height);
        setTitle("Filled Julia Set");
        julia  = (JuliaSet) getFractal();
        this.setLocation(MainFrame.DEFAULT_SIZE.width, 0);

        showSettings = false;
        init();
    }

    //initialise the JFrame
    public void init()
    {
        this.requestFocus();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Container pane = this.getContentPane();
        setToolBar(julia);
        pane.add(toolBar,BorderLayout.SOUTH);
        pane.add(julia,BorderLayout.CENTER);

        //determine whether to show settings
        getScrollSets().setVisible(showSettings);
        pane.add(getScrollSets(),BorderLayout.EAST);
        toolBar.add(getSettingsToggle());
    }

    //updates the toolbar to the fractal
    private void setToolBar(JuliaSet juliaSet){
        toolBar = new JPanel();
        FavButton fav = new FavButton(juliaSet);
        SaveButton save = new SaveButton(juliaSet);
        toolBar.add(fav);
        toolBar.add(save);
    }

    //create ShowSettings CheckBox
    private JCheckBox getSettingsToggle()
    {
        JCheckBox toggle = new JCheckBox("Show Settings");
        toggle.setSelected(showSettings);
        //attach listener
        toggle.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                //if selected show
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    getScrollSets().setVisible(true);
                    showSettings = true;
                }
                //if deselected hide
                else{
                    getScrollSets().setVisible(false);
                    showSettings = false;
                }

                validate();
            }
        });
        return toggle;
    }

    /*
     * updates the julia frame with a new JuliaSet and toolbar
     * basically a second initialisation that updates the screen
     */
    public void updateJulia(Complex c, MainFractal generator)
    {
        this.setTitle("Filled Julia Set "+c.toString());
        //hide what was before
        getFractal().setVisible(false);
        toolBar.setVisible(false);
        //replace with what is new
        setFractal(new JuliaSet(c,generator));
        julia = (JuliaSet) getFractal();
        init();
        setVisible(true);
        julia.requestFocus();
    }

    //return the instance of the singleton
    public static JuliaFrame getInstance() {
        return instance;
    }
}