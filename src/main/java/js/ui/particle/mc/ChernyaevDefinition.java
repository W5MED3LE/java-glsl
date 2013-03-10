package js.ui.particle.mc;

import js.ui.particle.cube.*;
import js.ui.util.Vec3;

import java.util.*;

import static java.util.Arrays.asList;
import static js.ui.particle.cube.CubeColors.*;
import static js.ui.particle.cube.CubeEdge.*;
import static js.ui.particle.cube.CubeVertice.*;

/**
 * Source: http://www.matmidia.mat.puc-rio.br/tomlew/pdfs/marching_cubes_jgt.pdf
 */
public class ChernyaevDefinition implements CubesDefinition {

    private final Set<Cube> cubes = new HashSet<Cube>();

    public ChernyaevDefinition() {
        //case 0
        define(EnumSet.noneOf(CubeVertice.class), defaultColor);

        //case 1
        define(EnumSet.of(a), CubeColors.orange, asList(i, j, m));

        //case 2
        define(EnumSet.of(a, b), CubeColors.orange, asList(j, k, n, m));

        //case 3.1
        define(EnumSet.of(a, f), CubeColors.orange, asList(i, j, m), asList(q, n, s));

        //case 3.2
        define(EnumSet.of(a,b,h), CubeColors.orange, asList(j, k, n, m), asList(p,s,t));

        //case 4.1
        define(EnumSet.of(a,b,h), CubeColors.orange, asList(j, k, n, m), asList(p,s,t));

        //todo...

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
