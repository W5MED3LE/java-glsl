package js.ui.particle.cube;

import js.ui.util.Vec3;

import java.util.Collection;
import java.util.Map;

import static js.ui.particle.cube.CubeVertice.*;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 02.08.12
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public enum CubeEdge implements CubeElement<CubeEdge> {
    i(a, b), j(a, c), k(b, d), l(c, d),
    m(a, e), n(b, f), o(c, g), p(d, h),
    q(e, f), r(e, g), s(f, h), t(g, h);

    public final CubeVertice v1, v2;

    private CubeEdge(CubeVertice v1, CubeVertice v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    private final static Vec3[] POSITION_MAP = new Vec3[]{
            Vec3.pos(.5f, 0, 0)
            , Vec3.pos(0, 0, .5f)
            , Vec3.pos(1, 0, .5f)
            , Vec3.pos(.5f, 0, 1)

            , Vec3.pos(0, .5f, 0)
            , Vec3.pos(1, .5f, 0)
            , Vec3.pos(0, .5f, 1)
            , Vec3.pos(1, .5f, 1)

            , Vec3.pos(.5f, 1, 0)
            , Vec3.pos(0, 1, .5f)
            , Vec3.pos(1, 1, .5f)
            , Vec3.pos(.5f, 1, 1)
    };

    private static final Map<CubeAxis, CubeEdge>[] ROTATION = new Map[]{
            CubeAxis.map(q, k, m)  //i
            , CubeAxis.map(m, i, r) //j
            , CubeAxis.map(n, l, j) //k
            , CubeAxis.map(i, j, o) //l

            , CubeAxis.map(r, n, q) //m
            , CubeAxis.map(s, p, i) //n
            , CubeAxis.map(j, m, t) //o
            , CubeAxis.map(k, o, l) //p

            , CubeAxis.map(t, s, n) //q
            , CubeAxis.map(o, q, s) //r
            , CubeAxis.map(p, t, k) //s
            , CubeAxis.map(l, r, p) //t
    };

    private static final Map<CubeAxis, CubeEdge>[] MIRROR = new Map[]{
            CubeAxis.map(i, q, l)   //i
            , CubeAxis.map(k, r, j) //j
            , CubeAxis.map(j, s, k) //k
            , CubeAxis.map(l, t, i) //l

            , CubeAxis.map(n, m, o)//m
            , CubeAxis.map(m, n, p) //n
            , CubeAxis.map(p, o, m) //o
            , CubeAxis.map(o, p, n) //p

            , CubeAxis.map(q, i, t) //q
            , CubeAxis.map(s, j, r) //r
            , CubeAxis.map(r, k, s) //s
            , CubeAxis.map(t, l, q) //t
    };

    @Override
    public Vec3 getPosition() {
        return POSITION_MAP[ordinal()];
    }

    public Vec3 getPosition(float v1Value, float v2Value) {
        if (v1Value == 0 && v2Value == 0) {
            return getPosition();
        }

        float sum = v1Value + v2Value;
        float v1weight = v1Value / sum;
        float v2weight = v2Value / sum;

        return v1.getPosition().multiply(v1weight).add(
                v2.getPosition().multiply(v2weight)
        );
    }

    @Override
    public CubeEdge rotate(CubeAxis axis) {
        return ROTATION[ordinal()].get(axis);
    }

    @Override
    public CubeEdge mirror(CubeAxis axis) {
        return MIRROR[ordinal()].get(axis);
    }

    public static int sig(Collection<CubeEdge> vertices) {
        return EnumConverter.encode(vertices);
    }

    public static Collection<CubeEdge> fromSig(int sig) {
        return EnumConverter.decode(sig, values());
    }

}
