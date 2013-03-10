package js.ui.rdr;

import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static javax.media.opengl.GL4bc.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class TestGlRenderer2 extends AbstractGLRenderer {

    private int texBufferTexture;
    private int texBuffer;
    private int shaderProgramm;

    public void init(GLAutoDrawable glAutoDrawable) {
        LOG.info("init");
        GL4bc gl = glAutoDrawable.getGL().getGL4bc();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL_SMOOTH);

        gl.glEnable(GL_MULTISAMPLE);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        ShaderHelper sh = new ShaderHelper(gl);
        shaderProgramm = sh.createShaderProgram(
                sh.createShader("tex", ShaderHelper.ShaderType.FragmentShader)
        );

        createTexBuffer(gl);
    }

    private void createTexBuffer(GL4bc gl) {
        IntBuffer tbo = IntBuffer.allocate(1);
        gl.glGenBuffers(1, tbo);
        texBuffer = tbo.get(0);
        gl.glBindBuffer(GL_TEXTURE_BUFFER, texBuffer);
        float[] data = {.0f, .5f, .8f, .9f, .5f, .5f, .5f, .5f};
        gl.glBufferData(GL_TEXTURE_BUFFER, data.length * Float.SIZE / 8, FloatBuffer.wrap(data), GL_DYNAMIC_DRAW);

        IntBuffer tex = IntBuffer.allocate(1);
        gl.glGenTextures(1, tex);
        texBufferTexture = tex.get(0);
        gl.glBindTexture(GL_TEXTURE_BUFFER, texBufferTexture);
        gl.glTexBuffer(GL_TEXTURE_BUFFER, GL4bc.GL_RGBA32F, texBufferTexture);
    }

    private void setCamera(GL4bc gl, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = getWidthHeightRatio();
        getGlu().gluPerspective(45, widthHeightRatio, 1, 1000);
        getGlu().gluLookAt((distance / 2f) * Math.sin(getTime() / 100.0), distance, distance, 0, 0, 0, 0, 1, 0);

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

        gl.glDisable(GL_LIGHTING);
        gl.glDisable(GL_LIGHT1);

        RenderUtils.displayXZGrid(gl, 5);
        RenderUtils.displayCoords(gl, 1);

        gl.glTranslatef(0, 1, 0);

        gl.glBindTexture(GL_TEXTURE_BUFFER, texBufferTexture);
        gl.glBufferSubData(GL_TEXTURE_BUFFER,0,4,FloatBuffer.wrap(new float[]{1f}));

        gl.glUseProgram(shaderProgramm);
        gl.glUniform1i(gl.glGetUniformLocation(shaderProgramm, "tboSampler"), 0);

        gl.glColor3f(1, 1, 1);
        RenderUtils.displaySimpleRect(gl, 8, 5);
        gl.glUseProgram(0);

        gl.glFlush();
    }

}
