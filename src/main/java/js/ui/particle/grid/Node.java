package js.ui.particle.grid;

import js.ui.particle.cube.CubeColors;
import js.ui.particle.cube.CubeUtil;
import js.ui.util.Vec3;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class Node {

    public static final int ADAPTATION_DEPTH = 1;
    public static final boolean USE_EDGE_ANGLE_THRESHOLD = false;
    public static final float EDGE_ANGLE_THRESHOLD_1 = (float) Math.PI / 5;
    public static final float EDGE_ANGLE_THRESHOLD_2 = (float) Math.PI / 4;
    boolean ADAPTIVE_NODES = false;

    private final Vec3 position;

    private Vec3 cachedAdaptivePosition;
    private Vec3 cachedNormalVector;
    private Vec3 color = CubeColors.defaultColor;
    private final Map<Polygon, Vec3> cachedPolygonNormalVectors = new HashMap<Polygon, Vec3>();

    private List<Edge> edges = new ArrayList<Edge>();
    private List<Polygon> polygons = new ArrayList<Polygon>();


    public Node(Vec3 position) {
        this.position = position;
    }

    public void registerEdge(Edge edge) {
        edges.add(edge);
        clearCache();
    }

    private void clearCache() {
        cachedAdaptivePosition = null;
        cachedNormalVector = null;
        cachedPolygonNormalVectors.clear();
    }

    public void registerPolygon(Polygon polygon) {
        polygons.add(polygon);
        clearCache();
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getAdaptivePosition() {
        if (!ADAPTIVE_NODES) {
            return getPosition();
        }
        if (cachedAdaptivePosition == null) {
            Collection<Vec3> nodes = new ArrayList<Vec3>();
            //nodes.add(position);

            for (Node n : getEdgeNodes(ADAPTATION_DEPTH)) {
                nodes.add(n.getPosition());
            }

            cachedAdaptivePosition = Vec3.mean(nodes);
        }

        return cachedAdaptivePosition;
    }

    public Set<Node> getEdgeNodes(int depth) {
        HashSet<Node> result = new HashSet<Node>();
        collectEdgeNodes(depth, result);
        return result;
    }

    public List<Edge> getOpenEdges() {
        List<Edge> result = new ArrayList<Edge>();

        for (Edge e : edges) {
            if (e.isOpen()) {
                result.add(e);
            }
        }

        return result;
    }

    public List<Vec3> findOpenPath() {
        if (getOpenEdges().size() != 2) {
            return null;
        }

        return findOpenPath(new LinkedHashSet<Edge>(), this);
    }

    List<Vec3> findOpenPath(Set<Edge> visited, Node target) {
        if (!visited.isEmpty() && equals(target)) {
            List<Vec3> result = new ArrayList<Vec3>();
            for (Polygon p : polygons) {
                if (p.getEdges().containsAll(visited)) {
                    return null;
                }
            }

            List<Node> path = Edge.nodePath(visited, target);
            path.remove(path.size() - 1);
            for (Node n : path) {
                result.add(n.getPosition());
            }
            return result;
        }

        List<Edge> openEdges = getOpenEdges();
        for (Edge e : openEdges) {
            if (visited.contains(e)) {
                continue;
            }

            List<Polygon> edgePolygons = e.getPolygons();
            if (edgePolygons.isEmpty()) {
                continue;
            }

            Polygon polygon = edgePolygons.get(0);
            List<Node> polygonNodes = polygon.getNodes();

            Node other = e.getOther(this);
            int iThis = polygonNodes.indexOf(this);
            int iOther = polygonNodes.indexOf(other);
            if (iThis < iOther) {
                continue;
            }

            visited.add(e);
            List<Vec3> path = other.findOpenPath(visited, target);
            if (path != null) {
                return path;
            }
            visited.remove(e);
        }
        return null;
    }


    public void collectEdgeNodes(int depth, Set<Node> nodes) {
        if (depth <= 0) {
            return;
        }
        for (Edge e : edges) {
            Node n = e.getOther(this);
            if (!nodes.contains(n)) {
                nodes.add(n);
                n.collectEdgeNodes(depth - 1, nodes);
            }
        }
    }

    public Vec3 getNormalVector() {
        if (cachedNormalVector == null) {
            Vec3 norm = Vec3.pos(0, 0, 0);

            for (Polygon p : polygons) {
                norm = norm.add(p.getNormalVector());
            }
            cachedNormalVector = norm.normalize();
        }
        return cachedNormalVector;
    }

    public void setCachedNormalVector(Vec3 cachedNormalVector) {
        this.cachedNormalVector = cachedNormalVector;
    }

    public Vec3 getNormalVector(Polygon owner) {
        if (owner == null) {
            return getNormalVector();
        }

        if (!USE_EDGE_ANGLE_THRESHOLD) {
            return getNormalVector();
        }
        Vec3 norm = cachedPolygonNormalVectors.get(owner);

        if (norm == null) {
            norm = Vec3.pos(0, 0, 0);

            Vec3 ownerNormalVector = owner.getNormalVector();

            for (Polygon p : polygons) {
                Vec3 normalVector = p.getNormalVector();
                float mult = edgeFactor(ownerNormalVector, normalVector);
                norm = norm.add(normalVector.multiply(mult));
            }
            norm = norm.normalize();
            cachedPolygonNormalVectors.put(owner, norm);
        }
        return norm;
    }

    public static float edgeFactor(Vec3 normalVector1, Vec3 normalVector2) {
        float angle = normalVector1.angle(normalVector2);
        return edgeFactor(angle);
    }

    public static float edgeFactor(float angle) {
        if (angle <= EDGE_ANGLE_THRESHOLD_1) {
            return 1f;
        }
        if (angle >= EDGE_ANGLE_THRESHOLD_2) {
            return 0f;
        } else {
            float a = angle - EDGE_ANGLE_THRESHOLD_1;
            return a / (EDGE_ANGLE_THRESHOLD_2 - EDGE_ANGLE_THRESHOLD_1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (position != null ? !position.equals(node.position) : node.position != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public Vec3 getColor() {
        return color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Node{" +
                "position=" + position +
                '}';
    }
}
