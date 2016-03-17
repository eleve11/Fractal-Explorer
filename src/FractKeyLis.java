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
        int iterationShift = 100; //increase/decrease iterations by 100

        switch (e.getKeyCode())
        {
            //reset to default when ESC
            case KeyEvent.VK_ESCAPE:
                fractal.setRealLow(Fractal.REAL_LOW);
                fractal.setRealUp(Fractal.REAL_UP);
                fractal.setImagLow(Fractal.IMAG_LOW);
                fractal.setImagUp(Fractal.IMAG_UP);
                fractal.setMaxIterations(Fractal.MAX_ITERATIONS);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            //zoom in and out when pressing I or O
            case KeyEvent.VK_I:
                fractal.zoom(SCALE,true);
                break;
            case KeyEvent.VK_O:
                fractal.zoom(SCALE,false);
                break;

            //Move horizontally with left and right arrows
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                horizontalShift = -horizontalShift;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                fractal.setRealLow(fractal.getRealLow() + horizontalShift);
                fractal.setRealUp(fractal.getRealUp() + horizontalShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            //move vertically with up and down arrows
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                verticalShift = -verticalShift;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                fractal.setImagLow(fractal.getImagLow() + verticalShift);
                fractal.setImagUp(fractal.getImagUp() + verticalShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;

            //start liveJulia mode when shift is down
            case KeyEvent.VK_SHIFT:
                MainFractal.setHovering(true);
                break;

            //reduce iterations when pressed -
            case KeyEvent.VK_MINUS:
                //prevent user from setting lower than 50% of iterationShift
                if (fractal.getMaxIterations() < (iterationShift*3/2))
                    break;
                iterationShift = -iterationShift;
                //increase iterations when pressed + or = which are the same key (on the english keyboard)
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS: //using equals instead of plus
                fractal.setMaxIterations(fractal.getMaxIterations() + iterationShift);
                fractal.getGUI().getSettings().updateSet();
                fractal.repaint();
                break;
        }
    }

    //exit liveJulia mode when shift is released
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            MainFractal.setHovering(false);
        }
    }
}