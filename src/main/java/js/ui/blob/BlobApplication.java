package js.ui.blob;

import js.ui.util.LibUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.swing.SwingUtilities2;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class BlobApplication {

    public static final long TARGET_FPS = 80;


    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(BlobApplication.class);
        LibUtil.setDefaultLibPath();

        log.info("Load profile ...");
        GLProfile profile = GLProfile.get(GLProfile.GL4bc);
        GLCapabilities glCapabilities = new GLCapabilities(profile);
        glCapabilities.setSampleBuffers(true);

        final BlobModel model = new BlobModel();
        final BlobRenderer renderer = new BlobRenderer(model);
        BlobController controller = new BlobController(renderer, model);

        log.info("Build canvas");
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);
        //glCanvas.addGLEventListener(new TestGlRenderer2());
        glCanvas.addGLEventListener(renderer);
        glCanvas.setSize(new Dimension(1280, 720));
        glCanvas.addMouseMotionListener(controller.getMouseListener());
        glCanvas.addMouseListener(controller.getMouseListener());

        log.info("Build jframe");
        final JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(glCanvas);
        jFrame.setTitle(BlobApplication.class.getName());

        jFrame.pack();
        jFrame.setVisible(true);
        log.info("started");

        final long start = getNow();

        final Callable<Object> callable = new Callable<Object>() {
            private long lastSec = getNow();
            private int cnt = 0;

            public Object call() throws Exception {
                long now = getNow();
                renderer.setTime((now - start) * TARGET_FPS / 1000);
                glCanvas.repaint();

                cnt++;

                if (now - lastSec >= 1000) {
                    jFrame.setTitle(BlobApplication.class.getName() + ", fps: " + cnt);
                    cnt = 0;
                    lastSec = now;
                }

                return null;
            }
        };

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            public void run() {
                SwingUtilities2.submit(callable);
            }
        }, 1000 / TARGET_FPS, 1000 / TARGET_FPS, TimeUnit.MILLISECONDS);
    }



    private static final boolean DO_COUNT = false;
    private static long cnt = 0;

    private static long getNow() {
        if (DO_COUNT) {
            return cnt += 3;
        }
        return System.currentTimeMillis();
    }
}
