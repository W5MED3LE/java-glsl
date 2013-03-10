package js.ui.particle.field;

import js.ui.util.Vec3;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 04.08.12
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */
public interface ScalarFieldScanner<T> {

    void scan(Vec3 position, float stepSize);
}
