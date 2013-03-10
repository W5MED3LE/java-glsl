package js.ui.particle.grid;

import js.ui.particle.cube.*;
import js.ui.particle.field.ScalarField;
import js.ui.particle.field.ScalarFieldScanner;
import js.ui.particle.mc.CubesDefinition;
import js.ui.particle.mc.MarchingCubesOrgDefinition;
import js.ui.util.ModifiedState;
import js.ui.util.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static js.ui.particle.cube.CubeVertice.sig;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class FieldGridFactory implements GridFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FieldGridFactory.class);
    public static final CubesDefinition CUBES_DEF = new MarchingCubesOrgDefinition();
    public static final float SCALAR_BORDER = .6f;

    public static final boolean VERIFY_GRID = true;
    public static final boolean FIX_GRID = true;

    private final ScalarField<Float> field;
    private ScalarField<Vec3> normalField;
    private ScalarField<Vec3> colorField;
    private final Map<Integer, Cube> cubes = new HashMap<Integer, Cube>();

    private ModifiedState state;
    private Grid grid;

    public FieldGridFactory(ScalarField<Float> field) {
        this.field = field;
        for (Cube c : CUBES_DEF.createCubes()) {
            cubes.put(c.getSignature(), c);
        }
    }

    public void setNormalField(ScalarField<Vec3> normalField) {
        this.normalField = normalField;
    }

    public void setColorField(ScalarField<Vec3> colorField) {
        this.colorField = colorField;
    }

    @Override
    public Grid createGrid() {
        if (grid != null && state != null && !state.modified()) {
            return grid;
        }

        grid = new Grid();
        state = field.getModifiedState();

        field.iterateField(new ScalarFieldScanner<Float>() {

            @Override
            public void scan(Vec3 position, float stepSize) {
                Cube cube = getCube(stepSize, position);
                if (cube == null) {
                    return;
                }
                List<List<? extends CubeEdge>> paths = cube.paths;
                for (List<? extends CubeEdge> path : paths) {
                    List<Vec3> elements = new ArrayList<Vec3>();
                    for (CubeEdge e : path) {
                        float w1 = weight(position, stepSize, e.v1);
                        float w2 = weight(position, stepSize, e.v2);
                        Vec3 nodePos = e.getPosition(w2, w1).multiply(stepSize).add(position);
                        elements.add(nodePos);
                    }

                    Polygon polygon = grid.registerPolygon(elements);
                    polygon.setColor(cube.color);
                }
            }

            private float weight(Vec3 position, float stepSize, CubeVertice v) {
                Vec3 position1 = v.getPosition(stepSize, position);
                return Math.abs(SCALAR_BORDER - getDensity(position1));
            }
        });


        if (VERIFY_GRID) {
            verifyGrid();
        }

        if (normalField != null) {
            for (Node n : grid.getNodes()) {
                n.setCachedNormalVector(normalField.getValue(n.getPosition()));
            }
        }
        if (colorField != null) {
            for (Node n : grid.getNodes()) {
                n.setColor(colorField.getValue(n.getPosition()));
            }
        }

        return grid;
    }

    private void verifyGrid() {
        List<Edge> openEdges = new ArrayList<Edge>();
        for (Edge e : grid.getEdges()) {
            if (e.getPolygons().size() <= 1) {
                openEdges.add(e);
            }
        }
        if (FIX_GRID) {
            for (Edge e : openEdges) {
                if (!e.isOpen()) {
                    continue;
                }
                List<Vec3> path = e.getN1().findOpenPath();
                if (path != null) {
                    LOG.info("Fixed path: " + path);
                    Polygon polygon = grid.registerPolygon(path);
                    polygon.setColor(CubeColors.blue);
                } else {
                    LOG.warn("Open edge: " + e);
                }
            }
        } else {
            for (Edge e : openEdges) {
                LOG.warn("Open edge: " + e);
            }
        }
    }

    protected boolean isDensityHard(Vec3 fieldPos) {
        return getDensity(fieldPos) > SCALAR_BORDER;
    }

    protected float getDensity(Vec3 fieldPos) {
        if (field.isBorder(fieldPos)) {
            return 0f;
        }

        return field.getValue(fieldPos);
    }

    private int createCoord(float factor, Vec3 position) {
        EnumSet<CubeVertice> vertices = EnumSet.noneOf(CubeVertice.class);
        for (CubeVertice v : CubeVertice.values()) {
            if (isDensityHard(v.getPosition(factor, position))) {
                vertices.add(v);
            }
        }
        return sig(vertices);
    }

    protected Cube getCube(float factor, Vec3 position) {
        int coord = createCoord(factor, position);
        return cubes.get(coord);
    }

}
