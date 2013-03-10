package js.ui.blob;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import js.shader.Shaders;
import js.ui.rdr.AbstractGLRenderer;
import js.ui.rdr.RenderUtils;
import js.ui.rdr.ShaderHelper;
import js.ui.util.Vec3;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static javax.media.opengl.GL4bc.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class BlobRenderer extends AbstractGLRenderer {

    private final BlobModel model;
    private int blobShaderProgram;

    //private int texBuffer, texBufferTexture;
    private TBO tbo;
    private Texture background;

    public BlobRenderer(BlobModel model) {
        this.model = model;
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        LOG.info("init");
        GL4bc gl = glAutoDrawable.getGL().getGL4bc();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL_SMOOTH);

        gl.glEnable(GL_MULTISAMPLE);
        gl.glEnable(GL_BLEND);

        tbo = createTexBuffer(gl, new float[3 * 3 * BlobModel.MAX_BLOBS]);

        try {
            background = TextureIO.newTexture(ClassLoader.getSystemResource("images/background.png"), false, TextureIO.PNG);
            background.bind(gl);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load textures", e);
        }


        ShaderHelper sh = new ShaderHelper(gl);
        blobShaderProgram = sh.createShaderProgram(
                sh.createShader(Shaders.Blob.NAME, ShaderHelper.ShaderType.VertextShader)
                , sh.createShader(Shaders.Blob.NAME, ShaderHelper.ShaderType.FragmentShader)
        );
    }

    class TBO {
        int tex, buffer;
    }

    private TBO createTexBuffer(GL4bc gl, float[] data) {
        TBO result = new TBO();
        IntBuffer tbo = IntBuffer.allocate(1);
        gl.glGenBuffers(1, tbo);
        result.buffer = tbo.get(0);
        gl.glBindBuffer(GL_TEXTURE_BUFFER, result.buffer);
        gl.glBufferData(GL_TEXTURE_BUFFER, data.length * 4, FloatBuffer.wrap(data), GL_DYNAMIC_DRAW);
        gl.glBindBuffer(GL_TEXTURE_BUFFER, 0); //unbind

        IntBuffer tex = IntBuffer.allocate(1);
        gl.glGenTextures(1, tex);
        result.tex = tex.get(0);
        gl.glBindTexture(GL_TEXTURE_BUFFER, result.tex);
        gl.glTexBuffer(GL_TEXTURE_BUFFER, GL_RGB32F, result.tex);
        gl.glBindTexture(GL_TEXTURE_BUFFER, 0);
        return result;
    }

    private void setCamera(GL4bc gl, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = getWidthHeightRatio();
        GLU glu = getGlu();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, distance, 1, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    protected void doDisplay(GLAutoDrawable gLDrawable) {
        final GL4bc gl = gLDrawable.getGL().getGL4bc();

        setCamera(gl, 10);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        model.calculate(getTime());

        //RenderUtils.displayXZGrid(gl, 5);
        //RenderUtils.displayCoords(gl, 1);

        for (Blob b : model.getBlobs()) {
            Vec3 pos = b.getPosition();
            // RenderUtils.displayVertex(gl, pos.x, pos.y, pos.z, 0.2f);
        }

        gl.glClearColor(0.1f, 0.15f, 0.2f, 0.0f);
        renderBackground(gl);

        renderBlobRectangle(gl);
        //renderMousePosition(gl);

        gl.glFlush();

        model.setMouseCoord(getMouseCood(gl));
    }


    private void renderMousePosition(GL4bc gl) {
        Vec3 coords = getMouseCood(gl);
        if (coords != null) {
            RenderUtils.displayVertex(gl, coords.x(), coords.y(), coords.z(), 1);
        }
    }

    private Vec3 getMouseCood(GL4bc gl) {
        Point pos = getMousePosition();
        if (pos == null) {
            return null;
        }
        int viewport[] = new int[4];
        double mvmatrix[] = new double[16];
        double projmatrix[] = new double[16];
        double wcoord[] = new double[4];
        gl.glGetIntegerv(GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL_PROJECTION_MATRIX, projmatrix, 0);


        int mX = pos.x;
        int mY = viewport[3] - (int) pos.y - 1;

        //get depth getValue from depth buffer
        FloatBuffer b = FloatBuffer.allocate(1);
        gl.glReadPixels(pos.x, mY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, b);
        float depth = b.get(0);
        getGlu().gluUnProject(mX, mY, depth,
                mvmatrix, 0,
                projmatrix, 0,
                viewport, 0,
                wcoord, 0);

        return Vec3.pos((float) wcoord[0], (float) wcoord[1], (float) wcoord[2]);
    }

    private void renderBackground(GL4bc gl) {
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
        background.enable(gl);
        int size_x = 10;
        int size_z = (int) (size_x / background.getAspectRatio());
        float y = -.1f;

        gl.glColor4f(1, 1, 1, 1f);
        gl.glPushMatrix();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-size_x, y, -size_z);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(size_x, y, -size_z);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(size_x, y, size_z);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-size_x, y, size_z);
        gl.glTexCoord2f(1, 0);
        gl.glEnd();

        gl.glPopMatrix();
        background.disable(gl);
    }

    private void renderBlobRectangle(GL4bc gl) {
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glUseProgram(blobShaderProgram);
        initBlobShader(gl);
        gl.glFlush();
        gl.glBegin(GL_QUADS);
        gl.glColor3f(1, 1, 1);
        int size = 8;
        float y = 0.01f;
        gl.glVertex3f(-size, y, -size);
        gl.glVertex3f(size, y, -size);
        gl.glVertex3f(size, y, size);
        gl.glVertex3f(-size, y, size);
        gl.glEnd();
        gl.glUseProgram(0);
    }

    private void initBlobShader(GL4bc gl) {
        final List<Blob> blobs = model.getBlobs();
        final int blobsCount = blobs.size();

        gl.glUniform1i(gl.glGetUniformLocation(blobShaderProgram, "blobCount"), blobsCount);
        float[] tboData = new float[3 * blobsCount * 3];
        int idx = 0;
        for (Blob b : blobs) {
            Vec3 position = b.getPosition();
            tboData[idx++] = position.x();
            tboData[idx++] = position.y();
            tboData[idx++] = position.z();
        }
        for (Blob b : blobs) {
            Vec3 color = b.getColor();
            tboData[idx++] = color.x();
            tboData[idx++] = color.y();
            tboData[idx++] = color.z();
        }
        for (Blob b : blobs) {
            tboData[idx++] = b.getSize();
            tboData[idx++] = 0f;//reserved
            tboData[idx++] = 0f;//reserved
        }

        gl.glBindBuffer(GL_TEXTURE_BUFFER, tbo.buffer);
        gl.glBufferSubData(GL_TEXTURE_BUFFER, 0, tboData.length * 4, FloatBuffer.wrap(tboData));

        gl.glBindTexture(GL_TEXTURE_BUFFER, tbo.tex);
        gl.glUniform1i(gl.glGetUniformLocation(blobShaderProgram, "tbo"), 0);
    }

}
