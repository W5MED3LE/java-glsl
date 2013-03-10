package js.ui.particle.field;

import js.ui.util.ModifiedState;
import js.ui.util.Vec3;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 04.08.12
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public interface ScalarField<T> {

    T getValue(Vec3 position);

    float getSize();

    ModifiedState getModifiedState();

    void iterateField(ScalarFieldScanner<T> scanner);

    boolean isBorder(Vec3 position);
}
