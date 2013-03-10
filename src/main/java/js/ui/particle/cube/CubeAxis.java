package js.ui.particle.cube;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public enum CubeAxis {

    x, y, z;

    public static <T> Map<CubeAxis, T> map(T mapX, T mapY, T mapZ) {
        EnumMap<CubeAxis, T> result = new EnumMap<CubeAxis, T>(CubeAxis.class);
        result.put(x, mapX);
        result.put(y, mapY);
        result.put(z, mapZ);
        return result;
    }

}
