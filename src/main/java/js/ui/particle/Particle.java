package js.ui.particle;

import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class Particle extends PointObject {

    private float weight = 1f;
    private Vec3 velocity;
    private Vec3 color;

    public Vec3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public Vec3 getColor() {
        return color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public float getDensity(Vec3 position) {
        float d = getPos().distance(position);
        return weight / (d * d * d);
    }
}
