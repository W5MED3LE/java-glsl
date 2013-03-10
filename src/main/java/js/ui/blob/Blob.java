package js.ui.blob;

import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 05.11.11
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public class Blob {

    private Vec3 position;
    private Vec3 color = Vec3.random(0, 1);
    private float size = 1;

    public Blob copy(){
        Blob blob = new Blob(position.x(), position.y(), position.z());
        blob.setSize(size);
        blob.setColor(color);
        return blob;
    }

    public Blob(float x, float y, float z) {
        position = Vec3.pos(x, y, z);
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
