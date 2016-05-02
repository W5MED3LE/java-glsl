package js.ui.particle;

import js.ui.particle.grid.*;
import js.ui.util.LibUtil;
import js.ui.util.Vec3;
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
public class ParticleApplication {

    public static final long TARGET_FPS = 50;
    public static final long TARGET_DELAY = 1000 / TARGET_FPS;
    public static final float DRAG_ROTATION_FACTOR = .15f;
    public static final int RENDER_SUBDIVS = 32;

    public static void main(String[] args) throws Exception {
        LibUtil.setDefaultLibPath();
        Logger log = LoggerFactory.getLogger(ParticleApplication.class);

        log.info("Load profile ...");
        GLProfile profile = GLProfile.get(GLProfile.GL4bc);
        GLCapabilities glCapabilities = new GLCapabilities(profile);
        glCapabilities.setSampleBuffers(true);
        glCapabilities.setNumSamples(8);

        final ParticleSystem particleSystem = new ParticleSystem(RENDER_SUBDIVS);
        particleSystem.setDimension(Vec3.pos(10, 10, 10));
        particleSystem.populate();

        final SimpleGridFactory simpleGridFactory = new SimpleGridFactory();
        final ParticleRenderer renderer = new ParticleRenderer();
        renderer.setParticleSystem(particleSystem);
        renderer.setOpenGlRenderer(new GridRenderer(simpleGridFactory));
//        renderer.setOpenGlRenderer(new ScalarFieldRenderer(particleSystem.getDensityField()));

        log.info("Build canvas");
        final GLCanvas glCanvas = new GLCanvas(glCapabilities);
        glCanvas.addGLEventListener(renderer);
        glCanvas.setSize(new Dimension(1280, 720));

        log.info("Build jframe");
        final JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(glCanvas);
        jFrame.setTitle(ParticleApplication.class.getName());

        jFrame.pack();
        jFrame.setVisible(true);

        final AtomicBoolean calculate = new AtomicBoolean(true);

        MouseAdapter mouseListener = new MouseAdapter() {

            Integer lastX;

            @Override
            public void mousePressed(MouseEvent e) {
                calculate.set(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                calculate.set(true);
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
        };
        glCanvas.addMouseListener(mouseListener);
        glCanvas.addMouseMotionListener(mouseListener);

        log.info("started");

        final long start = getNow();

        final Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
                long now = getNow();
                renderer.setTime((now - start) * TARGET_FPS / 1000);
                glCanvas.repaint();
                jFrame.setTitle(ParticleApplication.class.getName() + ", fps: " + renderer.getFramesPerSecond());
                return null;
            }
        };

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

            FieldGridFactory gridFactory = new FieldGridFactory(particleSystem.getDensityField());

            {
                gridFactory.setNormalField(particleSystem.getNormalField());
                gridFactory.setColorField(particleSystem.getColorField());
            }

            public void run() {
                if (calculate.get()) {
                    long now = getNow();
                    if (calculate.get()) {
                        particleSystem.calculate(now - start);
                        Grid grid = gridFactory.createGrid();
                        simpleGridFactory.setGrid(grid);
                    }
                }
            }
        }, 0, TARGET_DELAY * 2, TimeUnit.MILLISECONDS);

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            public void run() {
                SwingUtilities2.submit(callable);
            }
        }, 0, TARGET_DELAY, TimeUnit.MILLISECONDS);
    }

    private static long getNow() {
        return System.currentTimeMillis();
    }
}
