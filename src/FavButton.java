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
        this.setIcon(getStarIcon());
        updateStatus();

        //set color background when selected
        this.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.YELLOW;
            }
        });
    }

    /*
     * set as selected if the julia set is already in the favourites
     */
    private void updateStatus() {
        setSelected(Favourites.getInstance().contains(juliaSet.getC()));
    }

    /*
     * return the adjusted icon
     */
    private Icon getStarIcon()
    {
        Image star = new ImageIcon("star.png").getImage();
        Image scaled = star.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
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
        }
    }
}
