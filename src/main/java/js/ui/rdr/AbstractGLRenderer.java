package js.ui.rdr;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.io.File;


/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGLRenderer implements GLEventListener {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractGLRenderer.class);
    public static final boolean CAPTURE = false;
    public static final boolean CAPTURE_CLEAR = true;
    public static final File CAPTURE_DIRECTORY = new File("capture");
    public static final int CAPTURE_MAX = 1200;

    private GLU glu = new GLU();
    private long time = 0L;
    private int width, height;
    private Point mousePosition;

    private long frameTime = System.currentTimeMillis();
    private int frameCounter = 0;
    private int framesPerSecond = 0;

    public void setTime(long time) {
        this.time = time;
    }

    public void setMousePosition(Point position) {
        this.mousePosition = position;
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        LOG.info("init");
        if (CAPTURE) {
            Validate.isTrue(CAPTURE_DIRECTORY.isDirectory());
            if (CAPTURE_CLEAR) {
                for (File f : CAPTURE_DIRECTORY.listFiles()) {
                    LOG.info("Deleting " + f);
                    FileUtils.deleteQuietly(f);
                }
            }
        }
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {
        LOG.info("dispose");
    }

    @Override
    public final void display(GLAutoDrawable glAutoDrawable) {
        doDisplay(glAutoDrawable);
        saveScreen((GL4bc) glAutoDrawable.getGL());
        frameCounter++;
        long now = System.currentTimeMillis();
        if (now - frameTime >= 1000) {
            frameTime = now;
            framesPerSecond = frameCounter;
            frameCounter = 0;
        }
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    protected abstract void doDisplay(GLAutoDrawable glAutoDrawable);

    protected void saveScreen(GL4bc gl, File file) {
        RenderUtils.savescreen(gl, file, getWidth(), getHeight());
    }

    protected void saveScreen(GL4bc gl) {
        if (!CAPTURE) {
            return;
        }
        long t = getTime();
        t -= 3;
        if (t < 1) {
            return;
        }
        if (t > CAPTURE_MAX) {
            return;
        }

        File file = new File(CAPTURE_DIRECTORY, String.format("%s-%08d.png", getClass().getSimpleName(), t));
        saveScreen(gl, file);
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int width, int height) {
        this.width = width;
        this.height = Math.max(1, height);
    }

    public GLU getGlu() {
        return glu;
    }

    public long getTime() {
        return time;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getWidthHeightRatio() {
        return (float) getWidth() / (float) getHeight();
    }
}
