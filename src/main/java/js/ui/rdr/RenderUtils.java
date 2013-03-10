package js.ui.rdr;

import com.jogamp.opengl.util.gl2.GLUT;
import js.ui.util.Vec3;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL4bc;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.concurrent.*;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL4bc.GL_QUADS;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 05.11.11
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class RenderUtils {

    static Logger LOG = LoggerFactory.getLogger(RenderUtils.class);

    static GLUT glut = new GLUT();

    public static void displayVertex(GL4bc gl, float x, float y, float z, float length) {
        gl.glBegin(GL_LINES);
        for (int i = 1; i <= 3; ++i) {
            gl.glColor3f(1f, 1f, 1f);
            gl.glVertex3f(i == 1 ? x - length : x, i == 2 ? y - length : y, i == 3 ? z - length : z);
            gl.glVertex3f(i == 1 ? x + length : x, i == 2 ? y + length : y, i == 3 ? z + length : z);
        }
        gl.glEnd();
    }

    public static void displaySimpleRect(GL4bc gl, int w, int h) {
        int x = w / 2;
        int y = 0;
        int z = h / 2;

        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-x, y, -z);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(x, y, -z);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(x, y, z);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-x, y, z);
        gl.glTexCoord2f(1, 0);
        gl.glEnd();
    }

    public static void drawArrow(GL4bc gl, float x, float y, float z) {
        gl.glPushMatrix();
        gl.glBegin(GL_LINES);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(x, y, z);
        gl.glEnd();
        gl.glTranslatef(x, y, z);
        glut.glutSolidCube(.1f);
        gl.glPopMatrix();
    }

    public static void displayCoords(GL4bc gl, int length) {
        for (int i = 1; i <= 3; ++i) {
            gl.glColor3f(i == 1 ? 1 : 0, i == 2 ? 1 : 0, i == 3 ? 1 : 0);
            drawArrow(gl, i == 1 ? length : 0, i == 2 ? length : 0, i == 3 ? length : 0);
        }
    }

    public static void drawLineBox(GL4bc gl, Vec3 pos, Vec3 size) {
        gl.glPushMatrix();
        gl.glTranslatef(pos.x(), pos.y(), pos.z());
        gl.glScalef(size.x(), size.y(), size.z());
        for (int i = 0; i < 2; ++i) {
            int a = i % 2;
            int b = (i + 1) % 2;

            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex3f(a, a, a);

            gl.glVertex3f(b, a, a);
            gl.glVertex3f(b, b, a);
            gl.glVertex3f(a, b, a);
            gl.glVertex3f(a, a, a);

            gl.glVertex3f(a, b, a);
            gl.glVertex3f(a, b, b);
            gl.glVertex3f(a, a, b);
            gl.glVertex3f(a, a, a);

            gl.glVertex3f(a, a, b);
            gl.glVertex3f(b, a, b);
            gl.glVertex3f(b, a, a);
            gl.glVertex3f(a, a, a);
            gl.glEnd();
        }

        gl.glPopMatrix();
    }

    public static void displayXZGrid(GL4bc gl, int count) {
        gl.glBegin(GL_LINES);

        gl.glColor3f(.25f, .25f, .25f);
        for (int i = -count; i <= count; ++i) {
            gl.glVertex3f(-count, 0f, i);
            gl.glVertex3f(count, 0f, i);
            gl.glVertex3f(i, 0f, -count);
            gl.glVertex3f(i, 0f, count);
        }

        gl.glEnd();
    }

    public static final ExecutorService IMAGE_WORKER = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOG.info("Wating for image workers: "+IMAGE_WORKER);
                IMAGE_WORKER.shutdown();
                try {
                    IMAGE_WORKER.awaitTermination(3, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }
        });
    }

    public static void savescreen(GL4bc gl, final File file, int width, int height) {
        LOG.info("Writing screen to " + file);
        gl.glBindFramebuffer(gl.GL_READ_FRAMEBUFFER, 0);
        gl.glBindFramebuffer(gl.GL_DRAW_FRAMEBUFFER, 1);
        gl.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, gl.GL_COLOR_BUFFER_BIT, gl.GL_LINEAR);
        gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, 1);
        IntBuffer buffer = IntBuffer.allocate(width * height * 3);
        gl.glReadPixels(0, 0, width, height, gl.GL_BGR, gl.GL_INT, buffer);

        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        int[] a = buffer.array();
        ArrayUtils.reverse(a); //why?
        image.getRaster().setPixels(0, 0, width, height, a);
        IMAGE_WORKER.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageIO.write(image, "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
