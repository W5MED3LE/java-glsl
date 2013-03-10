package js.ui.particle.cube;

import js.ui.util.Vec3;
import org.apache.commons.lang.Validate;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public class CubeUtil {

    static <E extends Enum> Map<E, E> transformationMap(E[] values, E[] target) {
        Validate.isTrue(values.length == target.length);
        Map<E, E> map = new HashMap<E, E>();
        Set<E> used = new HashSet<E>();
        for (int i = 0; i < target.length; ++i) {
            E sourceValue = values[i];
            E targetValue = target[i];
            Validate.isTrue(!used.contains(targetValue));
            map.put(sourceValue, targetValue);
            used.add(targetValue);
        }
        return map;
    }

    static <E extends Enum> Map<CubeAxis, Map<E, E>> axisTransformation(E[] values, E[] targetX, E[] targetY, E[] targetZ) {
        Map<CubeAxis, Map<E, E>> result = new HashMap<CubeAxis, Map<E, E>>();
        result.put(CubeAxis.x, transformationMap(values, targetX));
        result.put(CubeAxis.y, transformationMap(values, targetY));
        result.put(CubeAxis.z, transformationMap(values, targetZ));
        return result;
    }

    static <T> T[] array(T... items) {
        return items;
    }

    public static List<Vec3> unfold(Iterable<? extends CubeElement> elements) {
        List<Vec3> result = new ArrayList<Vec3>();
        for (CubeElement e : elements) {
            result.add(e.getPosition());
        }
        return result;
    }

}
