import javax.swing.*;

/**
 * Main class runs everything.
 */
public class Main {
    public static void main(String[] args)
    {
        //make it thread safe for good practise
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
