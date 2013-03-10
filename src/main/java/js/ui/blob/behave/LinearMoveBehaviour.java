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
public class LinearMoveBehaviour extends Behaviour<Blob> {

    private Vec3 direction;

    public LinearMoveBehaviour(Blob object, Vec3 direction) {
        super(object);
        this.direction = direction;
    }

    public boolean caclulate(long time) {
        object.setPosition(object.getPosition().add(direction));
        return true;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }

}
