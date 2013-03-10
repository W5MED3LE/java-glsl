package js.ui.blob.behave;

import js.ui.blob.Blob;
import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 13.11.11
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */
public class SpeedColorBehaviour extends Behaviour<Blob> {

    private KineticBehaviour kineticBehaviour;

    private Vec3 zeroColor = Vec3.random(0f, .2f);
    private Vec3 fullColor = Vec3.random(.5f, 1f);

    public SpeedColorBehaviour(Blob object, KineticBehaviour kineticBehaviour) {
        super(object);
        this.kineticBehaviour = kineticBehaviour;
    }

    @Override
    public boolean caclulate(long time) {
        float vel = kineticBehaviour.getVelocity().length();
        vel*=1.5f;
        vel = Math.max(0, Math.min(1, vel));
        Vec3 a = zeroColor.multiply(1- vel);
        Vec3 b = fullColor.multiply(vel);
        object.setColor(a.add(b));
        return true;
    }
}
