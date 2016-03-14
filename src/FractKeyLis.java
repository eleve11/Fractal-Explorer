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
        int SCALE = 20;
        double horizontalShift = (fractal.getRealUp() - fractal.getRealLow()) / SCALE;
        double verticalShift = (fractal.getImagUp() - fractal.getImagLow()) / SCALE;
        int iterationShift = 100;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                fractal.setRealLow(Fractal.REAL_LOW);
                fractal.setRealUp(Fractal.REAL_UP);
                fractal.setImagLow(Fractal.IMAG_LOW);
                fractal.setImagUp(Fractal.IMAG_UP);
                fractal.setMaxIterations(Fractal.MAX_ITERATIONS);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            //zoom in and out
            case KeyEvent.VK_I:
                fractal.zoom(SCALE,true);
                break;
            case KeyEvent.VK_O:
                fractal.zoom(SCALE,false);
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
                if (fractal.getMaxIterations() < 150) //prevent user from going under 50 iterations
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