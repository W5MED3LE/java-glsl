package js.ui.particle.grid;

import js.ui.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class Edge {

    private final Node n1;
    private final Node n2;
    private final Vec3 key;
    private List<Polygon> polygons = new ArrayList<Polygon>();

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;

        key = createKey(n1, n2);

        n1.registerEdge(this);
        n2.registerEdge(this);
    }

    public void registerPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public Node getOther(Node node) {
        return n1.equals(node) ? n2 : n1;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public boolean isOpen() {
        return polygons.size() <= 1;
    }

    public float getEdgeHardness() {
        if (polygons.size() < 2) {
            return 0;
        }
        Vec3 n1 = polygons.get(0).getNormalVector();
        Vec3 n2 = polygons.get(1).getNormalVector();
        return Node.edgeFactor(n1, n2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (key != null ? !key.equals(edge.key) : edge.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public Object getKey() {
        return key;
    }

    public static List<Node> nodePath(Iterable<Edge> edges, Node start) {
        ArrayList<Node> result = new ArrayList<Node>();

        Node lastNode = null;
        for (Edge e : edges) {
            Node n1;
            if (lastNode == null) {
                n1 = start;
                result.add(n1);
            } else {
                n1 = lastNode;
            }

            Node n2 = e.getOther(n1);
            result.add(n2);
            lastNode = n2;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "n1=" + n1 +
                ", n2=" + n2 +
                '}';
    }

    public static Vec3 createKey(Node n1, Node n2) {
        return n1.getPosition().add(n2.getPosition());
    }

}
