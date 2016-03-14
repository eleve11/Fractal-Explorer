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
        this.addActionListener(new SaveListener());
    }

    //use the floppy disk icon because I'm old fashioned
    private void updateIcon()
    {
        Image floppy = new ImageIcon("Icons/save.png").getImage();
        //scale it t 25x25
        Image scaled = floppy.getScaledInstance(25,25,Image.SCALE_SMOOTH);
        Icon ico = new ImageIcon(scaled);
        this.setIcon(ico);
    }

    //return fractal
    private Fractal getFractal() {
        return fractal;
    }

    /**
     * Use a JFileChooser to let the user save the
     * fractal png image at his preferred location
     */
    private class SaveListener implements ActionListener
    {
        private JFileChooser fc = new JFileChooser();

        @Override
        public void actionPerformed(ActionEvent e)
        {
            int returnVal = fc.showSaveDialog(getFractal());
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                //prepare file object
                String fileName = fc.getSelectedFile().toString();
                //ensure extension, but let user add hidden files
                if (!fileName.endsWith(".png") && !fileName.equals(".png")) //equals handles a nasty an edge case
                    fileName += ".png";
                File image = new File(fileName);

                //prepare image
                BufferedImage bi = new BufferedImage(getFractal().getWidth(), getFractal().getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                getFractal().paint(g);
                g.dispose();

                //save it to a png file
                try {
                    ImageIO.write(bi, "png", image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
