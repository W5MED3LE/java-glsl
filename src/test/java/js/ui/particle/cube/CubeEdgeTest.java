package js.ui.particle.cube;

import js.ui.util.Vec3;
import org.junit.Test;

import static js.ui.particle.cube.CubeEdge.j;
import static js.ui.particle.cube.CubeEdge.values;
import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public class CubeEdgeTest {


    @Test
    public void rotate() throws Exception {
        for (CubeAxis axis : CubeAxis.values()) {
            for (CubeEdge edge : CubeEdge.values()) {
                CubeEdge item = edge;
                for (int i = 0; i < 4; ++i) {
                    item = item.rotate(axis);
                }
                assertEquals(axis + " rotation", edge, item);
            }
        }
    }

    @Test
    public void mirror() throws Exception {
        for (CubeAxis axis : CubeAxis.values()) {
            CubeEdge edge = CubeEdge.i;
            for (int i = 0; i < values().length; ++i) {
                edge = edge.mirror(axis);
            }
            assertEquals(CubeEdge.i, edge);
        }
    }

    @Test
    public void position() throws Exception {
        assertEquals(Vec3.pos(.5f, 0, 0), CubeEdge.i.getPosition(1, 1));
        assertEquals(Vec3.pos(.5f, 0, 0), CubeEdge.i.getPosition(0, 0));
        assertEquals(Vec3.pos(.75f, 0, 0), CubeEdge.i.getPosition(1, 3));
    }
}
