package js.ui.particle.cube.viewer;

import js.ui.util.LibUtil;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.swing.SwingUtilities2;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class CubeViewerApplication {

    public static final long TARGET_FPS = 80;
    public static final float DRAG_ROTATION_FACTOR = .15f;

    public static void main(String[] args) throws Exception {
        LibUtil.setDefaultLibPath();
        BasicConfigurator.configure();

        Logger log = LoggerFactory.getLogger(CubeViewerApplication.class);

        log.info("Load profile ...");
        GLProfile profile = GLProfile.get(GLProfile.GL4bc);
        GLCapabilities glCapabilities = new GLCapabilities(profile);
        glCapabilities.setSampleBuffers(true);

        final CubeViewerRenderer renderer = new CubeViewerRenderer();

        log.info("Build canvas");
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);
        glCanvas.addGLEventListener(renderer);
        glCanvas.setSize(new Dimension(1280, 720));

        log.info("Build jframe");
        final JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(glCanvas);
        jFrame.setTitle(CubeViewerApplication.class.getName());

        jFrame.pack();
        jFrame.setVisible(true);

        MouseAdapter mouseListener = new MouseAdapter() {

            Integer lastX;

            @Override
            public void mousePressed(MouseEvent e) {
                INC_COUNT.set(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                INC_COUNT.set(true);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastX != null) {
                    int diff = e.getX() - lastX;
                    renderer.setRotationOffset(renderer.getRotationOffset() + diff * DRAG_ROTATION_FACTOR);
                }

                lastX = e.getX();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0){
                    renderer.nextCube();
                } else {
                    renderer.prevCube();
                }

            }
        };
        glCanvas.addMouseListener(mouseListener);
        glCanvas.addMouseMotionListener(mouseListener);
        glCanvas.addMouseWheelListener(mouseListener);

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
                    jFrame.setTitle(CubeViewerApplication.class.getName() + ", fps: " + cnt);
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

    private static final boolean DO_COUNT = true;
    private static AtomicBoolean INC_COUNT = new AtomicBoolean(true);
    private static long cnt = 0;

    private static long getNow() {
        if (DO_COUNT) {
            return cnt += (INC_COUNT.get() ? 30 : 0);
        }
        return System.currentTimeMillis();
    }
}
