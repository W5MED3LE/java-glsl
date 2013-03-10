package js.ui.blob.behave;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 06.11.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class Behaviour<T> {

    protected final T object;

    protected Behaviour(T object) {
        this.object = object;
    }

    public abstract boolean caclulate(long time);

    public T getObject() {
        return object;
    }


}
