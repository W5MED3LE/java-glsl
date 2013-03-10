package js.ui.particle.cube;

import js.ui.util.Vec3;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public interface CubeElement<E extends CubeElement> {
    Vec3 getPosition();

    E rotate(CubeAxis axis);

    E mirror(CubeAxis axis);

}
