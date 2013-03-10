package js.ui.particle.grid;

import js.ui.particle.cube.Cube;
import js.ui.particle.cube.CubeEdge;
import js.ui.particle.cube.CubeElement;
import js.ui.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class CubeGridFactory implements GridFactory {

    private Cube cube;

    @Override
    public Grid createGrid() {
        final Grid grid = new Grid();

        if (cube == null) {
            return grid;
        }
        List<List<? extends CubeEdge>> paths = cube.paths;
        for (List<? extends CubeEdge> path : paths) {
            List<Vec3> elements = new ArrayList<Vec3>();
            for (CubeElement e : path) {
                elements.add(e.getPosition());
            }

            Polygon polygon = grid.registerPolygon(elements);
            polygon.setColor(cube.color);
        }

        return grid;
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }
}
