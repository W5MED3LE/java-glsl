package js.ui.particle.cube.viewer;

import com.jogamp.opengl.util.gl2.GLUT;
import js.ui.particle.cube.Cube;
import js.ui.particle.cube.CubeVertice;
import js.ui.particle.grid.CubeGridFactory;
import js.ui.particle.grid.GridRenderer;
import js.ui.particle.mc.MarchingCubesOrgDefinition;
import js.ui.rdr.AbstractGLRenderer;
import js.ui.rdr.RenderUtils;
import js.ui.util.Vec3;

import javax.media.opengl.GL4bc;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import java.util.List;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class CubeViewerRenderer extends AbstractGLRenderer {

    private static final GLU glu = new GLU();
    private static final GLUT glut = new GLUT();

    static {
        MarchingCubesOrgDefinition.USE_ROTATION = false;
    }

    private List<Cube> cubes = new MarchingCubesOrgDefinition().createCubes();
    private int cubeIndex = 0;
    private CubeGridFactory gridFactory = new CubeGridFactory();
    private GridRenderer mcRenderer = new GridRenderer(gridFactory);

    private float rotationOffset = 0f;

    public void nextCube() {
        cubeIndex = checkIndex(cubeIndex + 1);
    }

    public void prevCube() {
        cubeIndex = checkIndex(cubeIndex - 1);
    }

    public int checkIndex(int index) {
        if (index < 0) {
            return 0;
        }
        if (index >= cubes.size()) {
            return cubes.size() - 1;
        }
        return index;
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        LOG.info("init");
        GL4bc gl = glAutoDrawable.getGL().getGL4bc();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
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
        glu.gluLookAt(distance * .8, distance * .8, distance, 0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    protected void doDisplay(GLAutoDrawable glAutoDrawable) {
        final GL4bc gl = glAutoDrawable.getGL().getGL4bc();

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(.2f, .2f, .2f, .0f);

        gl.glLoadIdentity();

        setCamera(gl, 5);

        gl.glRotatef(rotationOffset + getTime() * .0f, 0, 1, 0);

        RenderUtils.displayXZGrid(gl, 10);
        RenderUtils.displayCoords(gl, 2);

        Cube cube = cubes.get(cubeIndex);
        gridFactory.setCube(cube);
        mcRenderer.render(gl);

        renderActivationPoins(gl, cube);

        gl.glPopMatrix();
        gl.glFlush();
    }

    private void renderActivationPoins(GL4bc gl, Cube cube) {
        for (CubeVertice vertice : CubeVertice.values()) {
            gl.glPointSize(10);
            gl.glBegin(gl.GL_POINTS);
            boolean active = cube.points.contains(vertice);
            if (active) {
                gl.glColor3f(1, 0, 0);
            } else {
                gl.glColor3f(1, 1, 1);
            }
            Vec3 p = vertice.getPosition();
            gl.glVertex3f(p.x(), p.y(), p.z());
            gl.glEnd();
        }
    }

    public void setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public float getRotationOffset() {
        return rotationOffset;
    }
}
