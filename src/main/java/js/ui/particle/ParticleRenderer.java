package js.ui.particle;

import com.jogamp.opengl.util.gl2.GLUT;
import js.ui.particle.field.ScalarField;
import js.ui.particle.grid.FieldGridFactory;
import js.ui.particle.grid.Grid;
import js.ui.particle.grid.GridRenderer;
import js.ui.rdr.AbstractGLRenderer;
import js.ui.rdr.RenderUtils;
import js.ui.util.OpenGlRenderer;
import js.ui.util.Vec3;

import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES1.*;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class ParticleRenderer extends AbstractGLRenderer {

    private static final GLUT glut = new GLUT();

    private float rotationOffset = 0f;
    private OpenGlRenderer gridRenderer;
    private ParticleSystem particleSystem;

    public void setOpenGlRenderer(OpenGlRenderer gridRenderer) {
        this.gridRenderer = gridRenderer;
    }

    public void setParticleSystem(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    @Override
    public long getTime() {
        return particleSystem.getModelTime();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        super.init(glAutoDrawable);
        GL4bc gl = glAutoDrawable.getGL().getGL4bc();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL_SMOOTH);

        gl.glEnable(GL_MULTISAMPLE);
        gl.glEnable(gl.GL_COLOR_MATERIAL);

        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(GL_FRONT);
    }

    private void setCamera(GL4bc gl, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = getWidthHeightRatio();
        GLU glu = getGlu();
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(distance * .8, distance * .5, distance, 0, -1, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void setLight(GL4bc gl) {
        float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
        float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] position = {10.0f, 20.0f, 10.0f, 0.0f};

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, position, 0);
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, specular, 0);

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_LIGHT0);

    }

    protected void doDisplay(GLAutoDrawable glAutoDrawable) {
        final GL4bc gl = glAutoDrawable.getGL().getGL4bc();

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(.2f, .2f, .2f, .0f);

        setLight(gl);

        gl.glLoadIdentity();

        setCamera(gl, 15);
        Vec3 diff = particleSystem.getDimension().multiply(-.5f);
        gl.glRotatef(rotationOffset + getTime() * .0f, 0, 1, 0);

        gl.glDisable(GL_LIGHTING);
//        RenderUtils.displayXZGrid(gl, 10);
        RenderUtils.displayCoords(gl, 2);
        gl.glEnable(GL_LIGHTING);

//        drawTestPot(gl);

        gl.glDisable(GL_LIGHTING);
        gl.glTranslatef(diff.x(), diff.y(), diff.z());
        gl.glColor4f(.15f, .15f, .15f, 1f);
        RenderUtils.drawLineBox(gl, particleSystem.getPos(), particleSystem.getDimension());
        gl.glEnable(GL_LIGHTING);

        gl.glPushMatrix();
        gridRenderer.render(gl);

        gl.glPopMatrix();
        gl.glFlush();
    }

    private void drawTestPot(GL4bc gl) {
        gl.glPushMatrix();
        gl.glTranslatef(10, 0, 0);
        glut.glutSolidTeapot(3);
        gl.glPopMatrix();
    }

    private void renderParticlePoints(GL4bc gl) {
        gl.glBegin(GL_POINTS);
        for (Particle p : particleSystem.getParticles()) {
            Vec3 pos = p.getPos();
            gl.glVertex3f(pos.x(), pos.y(), pos.z());
        }
        gl.glEnd();
    }

    public void setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public float getRotationOffset() {
        return rotationOffset;
    }
}
