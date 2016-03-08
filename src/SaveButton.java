import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Save button launches a dialog that
 * lets the user save the fractal image as a png.
 */
public class SaveButton extends JButton
{
    private Fractal fractal;

    //construct using the fractal you want to save
    public SaveButton(Fractal fractal){
        this.updateIcon();
        this.fractal = fractal;
        this.setFocusable(false);
        //use action listener that activates the save dialog
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SaveDialog().setVisible(true);
            }
        });
    }

    //use the floppy disk icon because I'm old fashioned
    private void updateIcon()
    {
        Image floppy = new ImageIcon("Icons/save.png").getImage();
        Image scaled = floppy.getScaledInstance(25,25,Image.SCALE_SMOOTH);
        Icon ico = new ImageIcon(scaled);
        setIcon(ico);
    }

    /**
     * Frame that displays a dialog to save the frame panel as a png image
     */
    private class SaveDialog extends JFrame
    {
        private JTextField name;
        private JButton ok,cancel;
        private JLabel invalid; //displays error messages

        //default constructor
        private SaveDialog()
        {
            super("Save Dialog");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.name = new JTextField(15);
            this.ok = new JButton("Save");
            this.cancel = new JButton("Cancel");

            this.invalid = new JLabel();
            invalid.setForeground(Color.RED);

            this.setSize(280,120);
            this.setResizable(false);
            this.setLocationRelativeTo(SaveButton.this);

            init();
        }

        //add components to the panel
        private void init()
        {
            this.setLayout(new FlowLayout());
            this.add(new JLabel("Save as: "));
            this.add(name);
            this.add(cancel);
            this.add(ok);
            this.add(invalid);

            ActionListener saveLis = new SaveListener();
            ok.addActionListener(saveLis);
            cancel.addActionListener(saveLis);
            name.addActionListener(saveLis);
        }

        /**
         * saves the fractal panel as a png if the action is triggered by
         * the ok button or the textfield
         * finally close the dialog
         */
        private class SaveListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //if the action is triggered by anything that is not the cancel button
                if(!e.getSource().equals(cancel)) {
                    //paint the fractal on a buffered image
                    BufferedImage bi = new BufferedImage(fractal.getSize().width, fractal.getSize().height, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bi.createGraphics();
                    fractal.paint(g);
                    g.dispose();
                    //handle illegal cases
                    if (name.getText().equals("") || name.getText().contains("/")){
                        invalid.setText("Invalid name. Retry");
                        return; //do not close the dialog
                    }
                    //save it to a png file
                    try {
                        ImageIO.write(bi, "png", new File(name.getText() + ".png"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //then close the dialog anyway
                dispose();
            }
        }
    }
}
