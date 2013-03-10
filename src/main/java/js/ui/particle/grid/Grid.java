package js.ui.particle.grid;

import js.ui.util.Vec3;
import org.apache.commons.lang.Validate;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class Grid {

    private Map<Vec3, Node> nodes = new HashMap<Vec3, Node>();
    private Map<Object, Edge> edges = new HashMap<Object, Edge>();
    private List<Polygon> polygons = new ArrayList<Polygon>();

    public Node registerNode(Vec3 point) {
        Node node = nodes.get(point);
        if (node == null) {
            node = new Node(point);
            nodes.put(point, node);
        }
        return node;
    }

    public Edge registerEdge(Node n1, Node n2) {
        Vec3 key = Edge.createKey(n1, n2);
        Edge value = edges.get(key);
        if (value == null) {
            value = new Edge(n1, n2);
            edges.put(key, value);
        }
        return value;
    }

    private Edge registerEdge(Vec3 v1, Vec3 v2) {
        return registerEdge(registerNode(v1), registerNode(v2));
    }

    public synchronized Polygon registerPolygon(List<Vec3> path) {
        int size = path.size();
        Validate.isTrue(size >= 3);

        List<Edge> edges = new ArrayList<Edge>();
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < size; ++i) {
            Vec3 v1 = path.get(i);
            Vec3 v2 = path.get((i + 1) % size);
            nodes.add(registerNode(v1));
            edges.add(registerEdge(v1, v2));
        }


        Polygon result = new Polygon(edges, nodes);
        polygons.add(result);
        return result;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }


    public Collection<Edge> getEdges() {
        return edges.values();
    }

    public Node findNode(Vec3 position) {
        return nodes.get(position);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }
}
