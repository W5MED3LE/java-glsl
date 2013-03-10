package js.ui.particle.mc;

import js.ui.particle.cube.*;
import js.ui.util.Vec3;

import java.util.*;

import static java.util.Arrays.asList;
import static js.ui.particle.cube.CubeColors.*;
import static js.ui.particle.cube.CubeEdge.*;
import static js.ui.particle.cube.CubeVertice.*;

/**
 * Source: http://www.marchingcubes.org/images/8/87/MCCases.png
 */
public class MarchingCubesOrgDefinition implements CubesDefinition {

    private final Set<Cube> cubes = new LinkedHashSet<Cube>();

    public static boolean USE_ROTATION = true;

    public MarchingCubesOrgDefinition() {
        define("case 0", EnumSet.noneOf(CubeVertice.class), defaultColor);

        define("case 1", EnumSet.of(c), lightGreen, asList(j,o, l));

        define("case 2", EnumSet.of(c, d), pink, asList(j, o, p, k));

        define("case 3", EnumSet.of(c, h), darkRed, asList(j,o,  l), asList(p, t, s));

        define("case 3c", EnumSet.of(a, b, d, e, f, g), white, asList(j, l, p, s), asList(j, s, t, o));

        define("case 4", EnumSet.of(c, f), lightBlue, asList(j, o, l), asList(n, s, q));

        define("case 5", EnumSet.of(a, b, d), blue, asList(m, j, l, p), asList(m, p, n));

        define("case 6", EnumSet.of(a, d, g), grey, asList(j, i, m), asList(k, l, p), asList(r, t,o));

        define("case 6c", EnumSet.of(b, c, e, f, h), grey, asList(j, o, l), asList(m, i, k, p,t, r));

        define("case 7", EnumSet.of(c, d, f), darkGray, asList(j, o, p, k), asList(n, s, q));

        define("case 7c", EnumSet.of(a, b, e, g, h), darkGreen, asList(j, k, q), asList(j, q, o), asList(o, q, p), asList(q, s, p));

        define("case 8", EnumSet.of(a, b, c, d), yellow, asList(n, m, o, p));

        define("case 9", EnumSet.of(a, c, d, g), red, asList(t, p, k, i, m, r));

        define("case 10", EnumSet.of(b, c, f, g), darkBlue, asList(r, t, l, j), asList(i, k, s, q));

        //todo: check
        define("case 11", EnumSet.of(a, b, c, f), orange, asList(m, o, l), asList(s, l, k), asList(s, m,l), asList(s, q, m));

        define("case 12", EnumSet.of(a, b, d, g), black, asList(r, t, o), asList(p, n, m, j), asList(j, l, p));

        define("case 12c", EnumSet.of(c, e, f, h), purple, asList(j, o, l), asList(t, r, m, p), asList(m, n, p));

        define("case 13", EnumSet.of(a, d, f, g), mag, asList(j, i, m), asList(k, l, p), asList(r, t,o), asList(n, s, q));

        define("case 14", EnumSet.of(b, c, e, f, g, h), green, asList(i, k, p,m), asList(m, p, l, j));

        define("case 14c", EnumSet.of(a, d), yellow, asList(j, i, m), asList(k, l, p));


    }

    @Override
    public List<Cube> createCubes() {
        return new ArrayList<Cube>(cubes);
    }

    private void define(String name, EnumSet<CubeVertice> source, Vec3 color, List<? extends CubeEdge>... paths) {
//        if(!name.contains("11")){
//            return;
//        }

        Cube c1 = new Cube(name, source, color, reverse(paths));
        cubes.add(c1);
        if (USE_ROTATION) {
            cubes.addAll(c1.rotationHull());
            Cube c2 = new Cube(name, EnumSet.complementOf(source), color, paths);
            cubes.addAll(c2.rotationHull());
        } else {
            cubes.add(c1);
        }
    }

    private List<? extends CubeEdge>[] reverse(List<? extends CubeEdge>[] paths) {
        List<? extends CubeEdge>[] result = new ArrayList[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            ArrayList<? extends CubeEdge> list = new ArrayList<CubeEdge>(paths[i]);
            Collections.reverse(list);
            result[i] = list;
        }
        return result;
    }

}
