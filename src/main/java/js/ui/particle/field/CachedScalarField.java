package js.ui.particle.field;

import js.ui.util.ModifiedState;
import js.ui.util.Vec3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 08.08.12
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 */
public class CachedScalarField<T> implements ScalarField<T> {

    private ScalarField<T> delegate;

    private final Map<Vec3, T> cache = new HashMap<Vec3, T>();

    public CachedScalarField(ScalarField<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isBorder(Vec3 position) {
        return delegate.isBorder(position);
    }

    public void iterateField(ScalarFieldScanner<T> scanner) {
        delegate.iterateField(scanner);
    }

    @Override
    public ModifiedState getModifiedState() {
        return delegate.getModifiedState();
    }

    @Override
    public float getSize() {
        return delegate.getSize();
    }

    @Override
    public T getValue(Vec3 position) {
        T value = cache.get(position);
        if (value == null) {
            value = delegate.getValue(position);
            cache.put(position, value);
        }
        return value;
    }

    public void clear() {
        cache.clear();
    }

    public static <T> CachedScalarField<T> cache(ScalarField<T> field) {
        return new CachedScalarField<T>(field);
    }

}
