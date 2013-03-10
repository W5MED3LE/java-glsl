package js.ui.util;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.math.RandomUtils;
import sun.misc.LRUCache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 06.11.11
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class Vec3 {

    private static final Vec3 NULL_VEC = Vec3.pos(0, 0, 0);

    private final float x, y, z;

    private transient Float length;

    private Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public Vec3 copy() {
        return Vec3.pos(x, y, z);
    }

    public Vec3 add(Vec3 vec) {
        if (vec == null) {
            return this;
        }
        return Vec3.pos(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec3 add(float value) {
        return add(Vec3.pos(value, value, value));
    }

    public Vec3 sub(float value) {
        return sub(Vec3.pos(value, value, value));
    }

    public Vec3 sub(Vec3 vec) {
        if (vec == null) {
            return this;
        }
        return Vec3.pos(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vec3 intFunc() {
        return pos((int) x, (int) y, (int) z);
    }

    public float distance(Vec3 vec) {
        float[] diffs = new float[]{x - vec.x, y - vec.y, z - vec.z};
        return (float) Math.sqrt(diffs[0] * diffs[0] + diffs[1] * diffs[1] + diffs[2] * diffs[2]);
    }

    public Vec3 discrete(float stepSize) {
        return Vec3.pos(
                discrete(x, stepSize)
                , discrete(y, stepSize)
                , discrete(z, stepSize)
        );
    }

    private static float discrete(float value, float stepSize) {
        return (int) (value / stepSize) * stepSize;
    }

    public float length() {
        if (length == null) {
            length = distance(nullVec());
        }
        return length;
    }

    public Vec3 multiply(float multiplier) {
        return Vec3.pos(x * multiplier, y * multiplier, z * multiplier);
    }

    public Vec3 divide(float devisor) {
        return Vec3.pos(x / devisor, y / devisor, z / devisor);
    }

    public Vec3 multiply(Vec3 vec) {
        return Vec3.pos(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vec3 divide(Vec3 vec) {
        return Vec3.pos(div(x, vec.x), div(y, vec.y), div(z, vec.z));
    }

    public Vec3 crossProduct(Vec3 vec) {
        return Vec3.pos(
                y * vec.z - z * vec.y,
                z * vec.x - x * vec.z,
                x * vec.y - y * vec.x
        );
    }

    public Vec3 normalVector(Vec3 a, Vec3 b) {
        return a.sub(this).crossProduct(b.sub(this));
    }

    public Vec3 normalize() {
        float length = length();
        return Vec3.pos(x / length, y / length, z / length);
    }

    public Vec3 mix(Vec3 vec, float weight1, float weight2) {
        float sum = weight1 + weight2;
        if (sum == 0f) {
            return this;
        }
        return multiply(weight1 / sum).add(vec.multiply(weight2 / sum));
    }

    private float div(float x, float y) {
        if (y == 0f) {
            return Float.MAX_VALUE;
        }
        return x / y;
    }

    public Vec3 maximize(Vec3 vec3) {
        return Vec3.pos(Math.max(x, vec3.x), Math.max(y, vec3.y), Math.max(z, vec3.z));
    }

    public float angle(Vec3 vec) {
        float lp = length() * vec.length();
        if (lp == 0f) {
            return 0f;
        }

        float sp = scalarProduct(vec);
        float v = sp / lp;
        float result = (float) Math.acos(v);
        if (Float.isNaN(result)) {
            return 0f;
        }
        return result;
    }

    public Collection<Vec3> collectDimNeightbors(float distance) {
        return Arrays.asList(
                pos(x + distance, y, z)
                , pos(x - distance, y, z)
                , pos(x, y + distance, z)
                , pos(x, y - distance, z)
                , pos(x, y, z + distance)
                , pos(x, y, z - distance)
        );
    }

    public float scalarProduct(Vec3 vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public static Vec3 mean(Collection<Vec3> vectors) {
        float x = 0, y = 0, z = 0;
        for (Vec3 v : vectors) {
            x += v.x;
            y += v.y;
            z += v.z;
        }
        float size = (float) vectors.size();
        x /= size;
        y /= size;
        z /= size;
        return Vec3.pos(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (Float.compare(vec3.x, x) != 0) return false;
        if (Float.compare(vec3.y, y) != 0) return false;
        if (Float.compare(vec3.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode(x, y, z);
    }

    public static int hashCode(float x, float y, float z) {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    public static Vec3 random(float min, float max) {
        return random(min, max, new Random());
    }

    public static Vec3 random(float min, float max, Random random) {
        float[] coords = new float[3];
        for (int i = 0; i < 3; ++i) {
            coords[i] = RandomUtils.nextFloat(random) * (max - min) + min;
        }
        return Vec3.pos(coords);
    }

    public static Vec3 nullVec() {
        return NULL_VEC;
    }

    public static Vec3 pos(float x, float y, float z) {
        return new Vec3(x, y, z);
    }

    public static Vec3 pos(float[] coords) {
        return Vec3.pos(coords[0], coords[1], coords[2]);
    }

}
