package js.ui.particle.cube;

import js.ui.util.Vec3;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 20.11.11
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class Cube {

    public final Set<CubeVertice> points;
    public final List<List<? extends CubeEdge>> paths;
    public final Vec3 color;
    public final String name;

    public Cube(Set<CubeVertice> points, Vec3 color, List<? extends CubeEdge>... path) {
        this(null, points, color, Arrays.asList(path));
    }

    public Cube(String name, Set<CubeVertice> points, Vec3 color, List<? extends CubeEdge>... path) {
        this(name, points, color, Arrays.asList(path));
    }

    public Cube(String name, Set<CubeVertice> points, Vec3 color, List<List<? extends CubeEdge>> paths) {
        this.name = name;
        this.points = points;
        this.color = color;
        this.paths = paths;
    }

    public Cube rotate(CubeAxis axis) {
        EnumSet<CubeVertice> newPoints = EnumSet.noneOf(CubeVertice.class);
        for (CubeVertice point : points) {
            newPoints.add(point.rotate(axis));
        }
        List<List<? extends CubeEdge>> newPaths = new ArrayList<List<? extends CubeEdge>>();
        for (List<? extends CubeEdge> path : paths) {
            List<CubeEdge> newPath = new ArrayList<CubeEdge>();
            for (CubeEdge pathElement : path) {
                newPath.add(pathElement.rotate(axis));
            }
            newPaths.add(newPath);
        }

        return new Cube(name, newPoints, color, newPaths);
    }

    public Cube mirror(CubeAxis axis) {
        EnumSet<CubeVertice> newPoints = EnumSet.noneOf(CubeVertice.class);
        for (CubeVertice point : points) {
            newPoints.add(point.mirror(axis));
        }
        List<List<? extends CubeEdge>> newPaths = new ArrayList<List<? extends CubeEdge>>();
        for (List<? extends CubeEdge> path : paths) {
            List<CubeEdge> newPath = new ArrayList<CubeEdge>();
            for (CubeEdge pathElement : path) {
                newPath.add(pathElement.mirror(axis));
            }
            Collections.reverse(newPath);
            newPaths.add(newPath);
        }

        return new Cube(name, newPoints, color, newPaths);
    }

    public Vec3 getPosition() {
        return Vec3.pos(.5f, .5f, .5f);
    }

    public int getSignature() {
        return CubeVertice.sig(points);
    }

    public String getStringSignature() {
        return "" + getSignature();
    }

    public Collection<Cube> rotationHull() {

        Cube cube = this;
        Map<Integer, Cube> result = new HashMap<Integer, Cube>();

        for (int j = 0; j < 4; ++j) {
            for (CubeAxis b : CubeAxis.values()) {
                for (CubeAxis a : CubeAxis.values()) {
                    for (int i = 0; i < 4; ++i) {
                        result.put(cube.getSignature(), cube);
                        cube = cube.rotate(a);
                    }
                }
                cube = cube.mirror(b);
            }
        }

        return result.values();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cube cube = (Cube) o;

        if (points != null ? !points.equals(cube.points) : cube.points != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return points != null ? points.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getStringSignature() + " -> " + StringUtils.join(paths, ", ");
    }
}
