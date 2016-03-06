import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * JComboBox listing the favourites.
 */
public class FavComboBox extends JComboBox<String>
{
    Fractal fractal;

    public FavComboBox(Fractal fractal){
        super(Favourites.getInstance().getStringArray());
        this.setSelectedIndex(-1);
        this.fractal = fractal;
        this.addActionListener(new FavComboListener());
    }

    //update the item list by replacing it with the original from Favourites
    private void refreshList()
    {
        int index = getSelectedIndex();

        String[] lst = Favourites.getInstance().getStringArray();
        removeAllItems();
        for (String s : lst)
            addItem(s);

        setSelectedIndex(index);
    }

    private class FavComboListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshList();
            List<Complex> complexList = Favourites.getInstance().getFavourites();
            Complex target = complexList.get(getSelectedIndex());
            if(fractal instanceof Mandelbrot)
                ((Mandelbrot) fractal).startJulia(target);
            //else
                //TODO handle when implement Burning ship
        }
    }
}
