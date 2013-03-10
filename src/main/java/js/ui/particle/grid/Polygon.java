package js.ui.particle.grid;

import js.ui.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class Polygon {

    private final List<Edge> edges;
    private final List<Node> nodes;
    private Vec3 color = Vec3.pos(1, 1, 1);
    private Vec3 cachedNormalVector;

    public Polygon(List<Edge> edges, List<Node> nodes) {
        this.edges = edges;
        this.nodes = nodes;
        for (Edge e : edges) {
            e.registerPolygon(this);
        }
        for (Node n : nodes) {
            n.registerPolygon(this);
        }
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public Vec3 getColor() {
        return color;
    }

    public Vec3 getNormalVector() {
        if (cachedNormalVector == null) {
            List<Node> nodes = getNodes();
            Vec3 a = nodes.get(0).getAdaptivePosition();
            Vec3 b = nodes.get(1).getAdaptivePosition();
            Vec3 c = nodes.get(2).getAdaptivePosition();
            cachedNormalVector = a.normalVector(c, b).normalize();
        }
        return cachedNormalVector;
    }

    public Vec3 getPosition() {
        List<Vec3> positions = new ArrayList<Vec3>();
        for (Node n : getNodes()) {
            positions.add(n.getPosition());
        }
        return Vec3.mean(positions);
    }

    public List<Node> getNodes() {
        return nodes;
    }


}
