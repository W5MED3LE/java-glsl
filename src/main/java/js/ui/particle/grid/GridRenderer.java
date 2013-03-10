package js.ui.particle.grid;

import js.ui.rdr.RenderUtils;
import js.ui.util.OpenGlRenderer;
import js.ui.util.Vec3;

import javax.media.opengl.GL;
import javax.media.opengl.GL4bc;

import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 20.11.11
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class GridRenderer implements OpenGlRenderer {

    public static final boolean RENDER_EDGES = false;
    public static final boolean RENDER_HARD_EDGES = false;
    public static final boolean RENDER_VERTICE_NORMALS = false;
    public static final boolean RENDER_POLYGON_NORMALS = false;
    public static final boolean USE_NODE_NORMALS = true;
    public static final boolean USE_NODE_COLOR = true;
    public static final boolean USE_LIGHTING = true;

    private final GridFactory gridFactory;

    public GridRenderer(GridFactory gridFactory) {
        this.gridFactory = gridFactory;
    }

    @Override
    public void render(GL4bc gl) {
        Grid grid = gridFactory.createGrid();
        if (grid == null) {
            return;
        }
        for (Polygon p : grid.getPolygons()) {
            renderPolygon(gl, p);
        }

        renderEdges(gl, grid);
    }

    private void renderPolygon(GL4bc gl, Polygon polygon) {
        if (polygon == null) {
            return;
        }
        gl.glPushMatrix();
        if (USE_LIGHTING) {
            gl.glEnable(GL4bc.GL_LIGHTING);
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        Vec3 pc = polygon.getColor();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        for (Node n : polygon.getNodes()) {
            Vec3 p = n.getAdaptivePosition();
            Vec3 nv = (USE_NODE_NORMALS ? n.getNormalVector(polygon) : polygon.getNormalVector());
            Vec3 c = USE_NODE_COLOR ? n.getColor() : pc;
            gl.glColor3f(c.x(), c.y(), c.z());
            gl.glNormal3f(nv.x(), nv.y(), nv.z());
            gl.glVertex3f(p.x(), p.y(), p.z());
        }
        gl.glEnd();

        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL4bc.GL_LIGHTING);

        if (RENDER_POLYGON_NORMALS) {
            renderNormal(gl, polygon.getPosition(), polygon.getNormalVector());
        }

        if (RENDER_VERTICE_NORMALS) {
            for (Node n : polygon.getNodes()) {
                renderNormal(gl, n.getAdaptivePosition(), n.getNormalVector(polygon));
            }
        }
    }

    private void renderNormal(GL4bc gl, Vec3 p, Vec3 nv) {
        gl.glPushMatrix();
        gl.glTranslatef(p.x(), p.y(), p.z());
        RenderUtils.drawArrow(gl, nv.x(), nv.y(), nv.z());
        gl.glPopMatrix();
    }

    private void renderEdges(GL4bc gl, Grid grid) {
        if (RENDER_EDGES) {
            gl.glColor3f(.3f, .3f, .3f);
            gl.glBegin(GL.GL_LINES);

            for (Edge e : grid.getEdges()) {
                Vec3 p1 = e.getN1().getAdaptivePosition();
                Vec3 p2 = e.getN2().getAdaptivePosition();
                gl.glVertex3f(p1.x(), p1.y(), p1.z());
                gl.glVertex3f(p2.x(), p2.y(), p2.z());
            }

            gl.glEnd();
        }

        if (RENDER_HARD_EDGES) {
            gl.glLineWidth(2f);
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            for (Edge e : grid.getEdges()) {
                float edgeHardness = e.getEdgeHardness();
                if (edgeHardness >= 1f) {
                    continue;
                }
                gl.glColor4f(.6f, .2f, .1f, 1f - edgeHardness);
                gl.glBegin(GL.GL_LINES);
                Vec3 p1 = e.getN1().getAdaptivePosition();
                Vec3 p2 = e.getN2().getAdaptivePosition();
                gl.glVertex3f(p1.x(), p1.y(), p1.z());
                gl.glVertex3f(p2.x(), p2.y(), p2.z());
                gl.glEnd();
            }
            gl.glLineWidth(1f);
            gl.glDisable(GL.GL_BLEND);
        }
    }


}
