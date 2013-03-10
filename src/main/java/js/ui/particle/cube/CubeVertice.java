package js.ui.particle.cube;

import js.ui.util.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 02.08.12
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public enum CubeVertice implements CubeElement<CubeVertice> {

    a, b, c, d,
    e, f, g, h;

    private final static Vec3[] VERTICE_MAP = new Vec3[]{
            Vec3.pos(0, 0, 0)
            , Vec3.pos(1, 0, 0)
            , Vec3.pos(0, 0, 1)
            , Vec3.pos(1, 0, 1)

            , Vec3.pos(0, 1, 0)
            , Vec3.pos(1, 1, 0)
            , Vec3.pos(0, 1, 1)
            , Vec3.pos(1, 1, 1)
    };

    private static final Map<CubeAxis, CubeVertice>[] ROTATION = new Map[]{
            CubeAxis.map(e, b, e)
            , CubeAxis.map(f, d, a)
            , CubeAxis.map(a, a, g)
            , CubeAxis.map(b, c, c)

            , CubeAxis.map(g, f, f)
            , CubeAxis.map(h, h, b)
            , CubeAxis.map(c, e, h)
            , CubeAxis.map(d, g, d)
    };

    private static final Map<CubeAxis, CubeVertice>[] MIRROR = new Map[]{
            CubeAxis.map(b, e, c)
            , CubeAxis.map(a, f, d)
            , CubeAxis.map(d, g, a)
            , CubeAxis.map(c, h, b)

            , CubeAxis.map(f, a, g)
            , CubeAxis.map(e, b, h)
            , CubeAxis.map(h, c, e)
            , CubeAxis.map(g, d, f)
    };

    @Override
    public Vec3 getPosition() {
        return VERTICE_MAP[ordinal()];
    }

    public Vec3 getPosition(float factor, Vec3 relative){
        return getPosition().multiply(factor).add(relative);
    }

    @Override
    public CubeVertice rotate(CubeAxis axis) {
        return ROTATION[ordinal()].get(axis);
    }

    @Override
    public CubeVertice mirror(CubeAxis axis) {
        return MIRROR[ordinal()].get(axis);
    }

    public static int sig(Collection<CubeVertice> vertices) {
        return EnumConverter.encode(vertices);
    }

    public static Collection<CubeVertice> fromSig(int sig) {
        return EnumConverter.decode(sig, values());
    }

}
