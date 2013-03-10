package js.ui.particle.grid;

import js.ui.util.Vec3;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 04.08.12
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class NodeTest {

    @Test
    public void openEdges() throws Exception {
        Vec3 a = Vec3.pos(0, 0, 0);
        Vec3 b = Vec3.pos(-1, 1, 0);
        Vec3 c = Vec3.pos(0, 2, 0);
        Vec3 d = Vec3.pos(1, 1, 0);
        Vec3 e = Vec3.pos(-1, -1, 0);
        Vec3 f = Vec3.pos(-1, 2, 0);
        Vec3 g = Vec3.pos(1, 2, 0);
        Vec3 h = Vec3.pos(1, -1, 0);

        Grid grid = new Grid();
        grid.registerPolygon(Arrays.asList(b, a, e));

        Node aNode = grid.findNode(a);
        assertEquals(2, aNode.getEdges().size());

        grid.registerPolygon(Arrays.asList(a, d, h));
        assertEquals(4, aNode.getEdges().size());

        grid.registerPolygon(Arrays.asList(c, b, f));
        grid.registerPolygon(Arrays.asList(d, c, g));

        //cheat
        grid.registerPolygon(Arrays.asList(a, e, h));
        assertEquals(4, aNode.getEdges().size());
        grid.registerPolygon(Arrays.asList(f, b, e));
        grid.registerPolygon(Arrays.asList(f, c, g));
        grid.registerPolygon(Arrays.asList(g, d, h));


        assertNotNull(aNode);
        List<Edge> openEdges = aNode.getOpenEdges();
        assertEquals(2, openEdges.size());
        List<Vec3> path = aNode.findOpenPath();
        assertEquals(Arrays.asList(a, b, c, d), path);

    }

    @Test
    public void edgeEquals() throws Exception {
        Vec3 a = Vec3.pos(0, 0, 0);
        Vec3 b = Vec3.pos(-1, 1, 0);

        Grid grid = new Grid();
        Node n1 = grid.registerNode(a);
        Node n2 = grid.registerNode(b);
        assertEquals(0, grid.getEdges().size());
        grid.registerEdge(n1, n2);
        assertEquals(1, grid.getEdges().size());
        grid.registerEdge(n1, n2);
        assertEquals(1, grid.getEdges().size());
        grid.registerEdge(n2, n1);
        assertEquals(1, grid.getEdges().size());
    }

    @Test
    public void edge() throws Exception {
        assertEquals(1f, Node.edgeFactor(0));
        assertEquals(1f, Node.edgeFactor(Node.EDGE_ANGLE_THRESHOLD_1));
        assertEquals(0f, Node.edgeFactor(Node.EDGE_ANGLE_THRESHOLD_2));
        assertTrue(Node.edgeFactor(Node.EDGE_ANGLE_THRESHOLD_1 + .0001f) < 1f);
        assertTrue(Node.edgeFactor(Node.EDGE_ANGLE_THRESHOLD_1 + .0001f) > 0f);
        assertEquals(0f, Node.edgeFactor((float) Math.PI));
        assertEquals(0.5f, Node.edgeFactor(.5f * (Node.EDGE_ANGLE_THRESHOLD_2 + Node.EDGE_ANGLE_THRESHOLD_1)), .01f);

    }
}
