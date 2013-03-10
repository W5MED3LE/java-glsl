package js.ui.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 03.08.12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class Vec3Test {

    @Test
    public void testNormal() throws Exception {
        Vec3 a = Vec3.pos(1, 1, 1);
        Vec3 b = Vec3.pos(1, 2, 1);
        Vec3 c = Vec3.pos(1, 1, 2);
        Vec3 d = Vec3.pos(1, 1, 1.5f);

        Assert.assertEquals(Vec3.pos(1, 0, 0), a.normalVector(b, c));
        Assert.assertEquals(Vec3.pos(-1, 0, 0), a.normalVector(c, b));
        Assert.assertEquals(Vec3.pos(.5f, 0, 0), a.normalVector(b, d));
    }

    @Test
    public void testMean() throws Exception {
        Vec3 a = Vec3.pos(1, 1, 1);
        Vec3 b = Vec3.pos(1, 2, 1);
        Vec3 c = Vec3.pos(1, 1.5f, 1);
        Assert.assertEquals(c, Vec3.mean(Arrays.asList(a, b)));
    }

    @Test
    public void testNormalize() throws Exception {
        Vec3 a = Vec3.pos(2, 0, 0);
        Vec3 n = a.normalize();
        Assert.assertEquals(1f, n.length());
        Assert.assertEquals(Vec3.pos(1, 0, 0), n);
    }

    @Test
    public void angle() throws Exception {
        Vec3 a = Vec3.pos(1, 0, 0);
        Vec3 b = Vec3.pos(0, 1, 0);
        Vec3 c = Vec3.pos(-0.9483769f, -0.31594333f, -0.027589628f);
        Assert.assertEquals(0f, a.angle(a));
        Assert.assertEquals(0f, c.angle(c));
    }

    @Test
    public void testDiscrete() throws Exception {
        Vec3 a = Vec3.pos(1.1f, 0, 0);
        Vec3 b = Vec3.pos(1f, 0, 0);
        Assert.assertEquals(b, a.discrete(1f));
        Assert.assertEquals(b, a.discrete(.5f));


    }
}
