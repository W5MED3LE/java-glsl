package js.ui.particle;

import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class PointObject {

    private Vec3 pos;

    public PointObject(Vec3 pos) {
        this.pos = pos;
    }

    public PointObject() {
        this(Vec3.nullVec());
    }

    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
        this.pos = pos;
    }
}

