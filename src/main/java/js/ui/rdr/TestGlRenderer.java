package js.ui.rdr;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import js.shader.Shaders;

import javax.media.opengl.GL;
import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLUquadric;
import java.io.IOException;

import static javax.media.opengl.GL4bc.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 03.11.11
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class TestGlRenderer extends AbstractGLRenderer {

    private Texture sandor;
    private int shaderProgram;

    public void init(GLAutoDrawable glAutoDrawable) {
        LOG.info("init");
        GL4bc gl = glAutoDrawable.getGL().getGL4bc();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL_SMOOTH);

        gl.glEnable(GL_MULTISAMPLE);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        try {
            sandor = TextureIO.newTexture(ClassLoader.getSystemResourceAsStream("images/sandor.jpeg"), false, "jpg");
            sandor.bind(gl);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load textures", e);
        }


        ShaderHelper h = new ShaderHelper(gl);
        shaderProgram = h.createShaderProgram(
                h.createShader(Shaders.Normal.NAME, ShaderHelper.ShaderType.VertextShader),
                h.createShader(Shaders.Normal.NAME, ShaderHelper.ShaderType.FragmentShader)
        );

        //gl.glGet

        gl.glUseProgram(shaderProgram);

        int baseColor = gl.glGetUniformLocation(shaderProgram, Shaders.Normal.UNIFORM_BASE_COLOR);
        gl.glUniform4f(baseColor, .1f, .2f, .6f, 1f);

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


    private void setLight(GL4bc gl) {
        gl.glLightfv(GL_LIGHT1, GL_POSITION, new float[]{0, 10, 0, 1}, 0);
        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, new float[]{1f, 1f, 1f, 1f}, 0);
        gl.glLightfv(GL_LIGHT1, GL_SPECULAR, new float[]{1f, 1f, 1f, 1f}, 0);

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT1);
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

        gl.glTranslatef(-1.5f, 0.0f, -6.0f);
        gl.glRotatef((float) (getTime()), 0f, 1f, 0f);

        gl.glBegin(GL_TRIANGLES);
        gl.glColor3f(.5f, .2f, .0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glColor3f(.5f, .2f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glColor3f(.1f, .7f, .0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glEnd();
        gl.glTranslatef(3.0f, 0.0f, 0.0f);

        gl.glPushMatrix();

        gl.glRotatef((float) (getTime() * 2), 1f, 1f, 1f);
        gl.glColor3f(1f, 1f, 1f);
        sandor.enable(gl);
        displayCube(gl, 1.3f);
        sandor.disable(gl);

        gl.glPopMatrix();

        gl.glLoadIdentity();

        gl.glBegin(GL.GL_LINE_STRIP);

        gl.glColor3f(1, 1, 1);
        for (float i = -5; i <= 5; i += .1f) {
            gl.glVertex3f(i, .25f * i * i, 0);
        }

        gl.glEnd();

        gl.glTranslatef(0, 2, 0);

        setLight(gl);
        gl.glUseProgram(shaderProgram);
        float[] rgba = {0.3f, 0.5f, 1f};
        gl.glMaterialfv(GL.GL_FRONT, GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT, GL_SHININESS, 0.5f);
        GLUquadric sphere = getGlu().gluNewQuadric();
        getGlu().gluSphere(sphere, 1, 16, 18);
        getGlu().gluDeleteQuadric(sphere);
        gl.glUseProgram(0);


        gl.glFlush();
    }

    public void displayCube(GL4bc gl, float width) {
        float p = width / 2;

        gl.glBegin(GL_QUAD_STRIP);

        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-p, -p, p);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(-p, p, p);

        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(p, -p, p);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(p, p, p);

        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(p, -p, -p);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(p, p, -p);

        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(-p, -p, -p);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(-p, p, -p);

        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-p, -p, p);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(-p, p, p);

        gl.glEnd();

        gl.glBegin(GL_QUADS);
        for (float y : new float[]{p, -p}) {
            gl.glTexCoord2f(0f, 0f);
            gl.glVertex3f(-p, y, -p);

            gl.glTexCoord2f(0f, 1f);
            gl.glVertex3f(-p, y, p);

            gl.glTexCoord2f(1f, 1f);
            gl.glVertex3f(p, y, p);

            gl.glTexCoord2f(1f, 0f);
            gl.glVertex3f(p, y, -p);
        }

        gl.glEnd();
    }

}
