package js.ui.blob.behave;

import js.ui.blob.Blob;
import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 13.11.11
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public class FollowBehaviour extends Behaviour<Blob>{

    private Blob leader;
    private float gap = .2f;

    public FollowBehaviour(Blob object, Blob leader) {
        super(object);
        this.leader = leader;
    }

    @Override
    public boolean caclulate(long time) {
        Vec3 diff = leader.getPosition().sub(object.getPosition());
        float length = diff.length();
        if (length == Float.NaN || length <= 0) {
            return true;
        }
        float fac = Math.max(0, length - gap) / length;
        diff = diff.multiply(fac);
        object.setPosition(object.getPosition().add(diff));
        return true;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }
}
