package js.ui.particle.mc;

import js.ui.particle.cube.*;
import js.ui.util.Vec3;

import java.util.*;

import static java.util.Arrays.asList;
import static js.ui.particle.cube.CubeColors.*;
import static js.ui.particle.cube.CubeColors.darkGreen;
import static js.ui.particle.cube.CubeEdge.*;
import static js.ui.particle.cube.CubeVertice.*;
import static js.ui.particle.cube.CubeVertice.g;

/**
 * Source: http://users.polytech.unice.fr/~lingrand/MarchingCubes/algo.html
 */
public class PolyTechDefinition implements CubesDefinition {

    private final Set<Cube> cubes = new HashSet<Cube>();

    public PolyTechDefinition() {
        //case 0
        define(EnumSet.noneOf(CubeVertice.class), defaultColor);

        //case 1
        define(EnumSet.of(a), CubeColors.orange, asList(i, j, m));

        //case 2
        define(EnumSet.of(a, b), blue, asList(j, k, n, m));

        //case 3
        define(EnumSet.of(a, f), red, asList(i, j, m), asList(n, q, s));

        //case 4
        define(EnumSet.of(a, h), grey, asList(i, j, m), asList(p, s, t));

        //case 5
        define(EnumSet.of(b, c, d), yellow, asList(n, i, j, o, p));

        //case 6
        define(EnumSet.of(a, b, h), pink, asList(j, k, n, m), asList(p, s, t));

        //case 7
        define(EnumSet.of(b, e, h), darkRed, asList(l, i, k), asList(r, m, q), asList(p, s, t));

        //case 8
        define(EnumSet.of(a, b, c, d), green, asList(m, n, p, o));

        //case 9
        define(EnumSet.of(a, c, d, g), white, asList(t, r, m, i), asList(i, k, p, t));

        //case 10
        define(EnumSet.of(a, d, e, h), darkBlue, asList(j, i, q, r), asList(l, k, s, t));

        //case 11
        define(EnumSet.of(a, c, d, h), darkGray, asList(o, m, i, s, t), asList(i, k, s));

        //case 12
        define(EnumSet.of(b, c, d, e), mag, asList(n, i, j, o, p), asList(m, q, r));

        //case 13
        define(EnumSet.of(a, d, f, g), purple, asList(i, j, m), asList(k, l, p), asList(q, n, s), asList(r, o, t));

        //case 14
        define(EnumSet.of(b, c, d, g), darkGreen, asList(r, j, i, p, t), asList(i, p, n));
    }

    @Override
    public List<Cube> createCubes() {
        return new ArrayList<Cube>(cubes);
    }

    private void define(Set<CubeVertice> source, Vec3 color, List<? extends CubeEdge>... paths) {
        Cube cube = new Cube(source, color, paths);
        cubes.addAll(cube.rotationHull());
    }

}
