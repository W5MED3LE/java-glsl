package js.ui.particle.field;

import js.ui.util.Vec3;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 05.08.12
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class SinusField extends AbstractScalarField<Float> implements ScalarField<Float> {

    @Override
    public Float getValue(Vec3 position) {
        return value(position.x(), position.y(), position.z());
    }

    public float value(float x, float y, float z) {
        if (isBorder(x, y, z)) {
            return 0;
        }
        float fac = 50f / getSize();
        float funcValue = 2f * (float) (Math.cos(x * fac) + Math.sin(z * fac));
        if (y < getSize() / 2 + funcValue - 2 || y > getSize() / 2 + funcValue) {
            return 0;
        }
        return 1;
    }

    @Override
    public float getSize() {
        return 100;
    }


}
