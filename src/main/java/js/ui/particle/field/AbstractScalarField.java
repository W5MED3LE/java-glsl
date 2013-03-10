package js.ui.particle.field;

import js.ui.util.ModifiedState;
import js.ui.util.Vec3;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 05.08.12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractScalarField<T> implements ScalarField<T> {

    @Override
    public synchronized void iterateField(final ScalarFieldScanner<T> scanner) {
        float stepsize = getStepSize();
        float size = getSize();
        for (float x = 0; x < size; x += stepsize) {
            for (float y = 0; y < size; y += stepsize) {
                for (float z = 0; z < size; z += stepsize) {
                    scanner.scan(Vec3.pos(x, y, z), stepsize);
                }
            }
        }
    }

    protected float getStepSize() {
        return 1f;
    }

    @Override
    public ModifiedState getModifiedState() {
        return new ModifiedState() {
            @Override
            public boolean modified() {
                return true;
            }
        };
    }

    @Override
    public boolean isBorder(Vec3 position) {
        return isBorder(position.x(), position.y(), position.z());
    }

    protected boolean isBorder(float x, float y, float z) {
        float dim = getSize();
        return x <= 0 || y <= 0 || z <= 0 || x >= dim || y >= dim || z >= dim;
    }
}
