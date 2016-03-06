import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Favourite button is a toggle button that
 * adds a given julia set to the favourites or removes it.
 */
public class FavButton extends JToggleButton {

    private JuliaSet juliaSet;

    //Construct the button
    public FavButton(JuliaSet juliaSet)
    {
        this.juliaSet = juliaSet;
        this.addActionListener(new StateListener());
        updateStatus();
        this.setSize(32,32);
        setFocusable(false);
    }

    /*
     * set as selected if the julia set is already in the favourites
     */
    private void updateStatus() {
        setSelected(Favourites.getInstance().contains(juliaSet.getC()));
        updateIcon();
    }

    /*
     * set the right icon when status chacnges
     */
    private void updateIcon()
    {
        String file = isSelected() ? "star.png" : "star_unfilled.png";
        Image star = new ImageIcon(file).getImage();
        Image scaled = star.getScaledInstance(20,20,Image.SCALE_SMOOTH);
        Icon ico = new ImageIcon(scaled);
        setIcon(ico);
    }

    /*
     * setter used when updating the JuliaSet
     */
    public void setJuliaSet(JuliaSet juliaSet) {
        this.juliaSet = juliaSet;
        updateStatus();
    }

    /**
     * class manages state changes when clicked
     */
    private class StateListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //add to favourites if selected
            if(((JToggleButton) e.getSource()).isSelected()) {
                try {
                    Favourites.getInstance().add(juliaSet.getC());
                } catch (IOException e1) {
                    System.err.println(e1.getMessage() + "\nCannot add to favourites");
                }

            }

            //Remove from favourites if deselected
            else {
                try {
                    Favourites.getInstance().remove(juliaSet.getC());
                } catch (Exception e1) {
                    System.err.println(e1.getMessage() + "\n Cannot remove from favourites");
                }
            }

            updateIcon();
        }
    }
}