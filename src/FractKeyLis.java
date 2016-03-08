import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Listens to key typed and updates the fractal.
 */
public class FractKeyLis extends KeyAdapter
{
    private Fractal fractal;

    //construct the keyListener
    public FractKeyLis(Fractal fractal){
        this.fractal = fractal;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        //shift 5% of current range
        double horizontalShift = (fractal.getRealUp() - fractal.getRealLow()) / 20;
        double verticalShift = (fractal.getImagUp() - fractal.getImagLow()) / 20;
        int iterationShift = 100;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                fractal.setRealLow(Fractal.REAL_LOW);
                fractal.setRealUp(Fractal.REAL_UP);
                fractal.setImagLow(Fractal.IMAG_LOW);
                fractal.setImagUp(Fractal.IMAG_UP);
                fractal.setMaxIterations(100);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            case KeyEvent.VK_LEFT:
                horizontalShift = -horizontalShift;
            case KeyEvent.VK_RIGHT:
                fractal.setRealLow(fractal.getRealLow() + horizontalShift);
                fractal.setRealUp(fractal.getRealUp() + horizontalShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            case KeyEvent.VK_DOWN:
                verticalShift = -verticalShift;
            case KeyEvent.VK_UP:
                fractal.setImagLow(fractal.getImagLow() + verticalShift);
                fractal.setImagUp(fractal.getImagUp() + verticalShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            case KeyEvent.VK_SHIFT:
                MainFractal.setHovering(true);
                break;

            case KeyEvent.VK_MINUS:
                iterationShift = -iterationShift;
                if (fractal.getMaxIterations() == 100) //prevent user from going under 100 iterations
                    break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS: //using equals instead of plus
                fractal.setMaxIterations(fractal.getMaxIterations() + iterationShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            MainFractal.setHovering(false);
        }
    }
}