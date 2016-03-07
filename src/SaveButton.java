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

    public SaveButton(Fractal fractal){
        this.updateIcon();
        this.fractal = fractal;
        this.setFocusable(false);
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

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }

    private class SaveDialog extends JFrame
    {
        private JTextField name;
        private JButton ok,cancel;

        private SaveDialog()
        {
            super("Save Dialog");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.name = new JTextField(15);
            this.ok = new JButton("Save");
            this.cancel = new JButton("Cancel");
            this.setSize(300,100);
            this.setResizable(false);
            this.setLocationRelativeTo(SaveButton.this);
            init();
        }

        private void init()
        {
            this.setLayout(new FlowLayout());
            this.add(new JLabel("Save as: "));
            this.add(name);
            this.add(cancel);
            this.add(ok);

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
                if(!e.getSource().equals(cancel))
                {
                    //paint the fractal on a buffered image
                    BufferedImage bi = new BufferedImage(fractal.getSize().width, fractal.getSize().height, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bi.createGraphics();
                    fractal.paint(g);
                    g.dispose();
                    if(name.getText().equals(""))
                        name.setText("no_name");
                    //save it to a png file
                    try{
                        ImageIO.write(bi,"png",new File(name.getText()+".png"));}
                    catch (Exception ex) {
                     ex.printStackTrace();
                    }
                }
                dispose();
            }
        }
    }
}
