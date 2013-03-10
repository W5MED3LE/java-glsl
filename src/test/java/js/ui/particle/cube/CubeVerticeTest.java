package js.ui.particle.cube;

import js.ui.util.Vec3;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static js.ui.particle.cube.CubeVertice.*;
import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 02.08.12
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class CubeVerticeTest {

    @Test
    public void simple() throws Exception {
        assertEquals(0, a.ordinal());
        assertEquals(1, b.ordinal());

        assertEquals(Collections.emptyList(), fromSig(0));
        assertEquals(Arrays.asList(a, b), fromSig(3));
        assertEquals(Arrays.asList(a, b, e), fromSig(19));

        assertEquals(Vec3.pos(0, 0, 0), a.getPosition());
        assertEquals(Vec3.pos(1, 1, 1), h.getPosition());
    }

    @Test
    public void rotate() throws Exception {
        for (CubeAxis axis : CubeAxis.values()) {
            CubeVertice vertice = a;
            for (int i = 0; i < values().length; ++i) {
                vertice = vertice.rotate(axis);
            }
            assertEquals(a, vertice);
        }
    }

    @Test
    public void mirror() throws Exception {
        for (CubeAxis axis : CubeAxis.values()) {
            CubeVertice vertice = a;
            for (int i = 0; i < values().length; ++i) {
                vertice = vertice.mirror(axis);
            }
            assertEquals(a, vertice);
        }
    }
}
