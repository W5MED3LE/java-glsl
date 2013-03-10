package js.ui.blob.behave;

import js.ui.blob.Blob;
import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 06.11.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class SinCosBehaviour extends Behaviour<Blob> {

    private float speed = (float) (2f * (Math.random() - .5));
    private Vec3 base;

    public SinCosBehaviour(Blob object) {
        super(object);
        base = object.getPosition().copy();
    }

    public boolean caclulate(long time) {
        float sin = 1f * (float) Math.sin(time / 100.0 * speed);
        float cos = 1f * (float) Math.cos(time / 100.0 * speed);

        object.setPosition(Vec3.pos(base.x() + sin, base.y(), base.z() + cos));
        return true;
    }
}
