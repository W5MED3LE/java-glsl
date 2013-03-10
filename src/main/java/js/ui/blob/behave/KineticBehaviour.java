package js.ui.blob.behave;

import js.ui.blob.Blob;
import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 13.11.11
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class KineticBehaviour extends Behaviour<Blob> {

    private float acceleration = 0.1f;
    private float friction = 0.97f;

    private Vec3 velocity = Vec3.nullVec();
    private Vec3 target = null;

    public KineticBehaviour(Blob object) {
        super(object);
    }

    @Override
    public boolean caclulate(long time) {
        if (target == null) {
            return true;
        }

        Vec3 direction = target.sub(object.getPosition());
        velocity = velocity.add(direction).multiply(acceleration);
        velocity = velocity.multiply(friction);
        object.setPosition(object.getPosition().add(velocity));

        return true;
    }

    public void setTarget(Vec3 target) {
        this.target = target;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public Vec3 getVelocity() {
        return velocity;
    }
}
