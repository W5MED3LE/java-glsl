package js.ui.particle;

import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class RecObject extends PointObject{

    private Vec3 dimension = Vec3.nullVec();

    public Vec3 getDimension() {
        return dimension;
    }

    public void setDimension(Vec3 dimension) {
        this.dimension = dimension;
    }
}
