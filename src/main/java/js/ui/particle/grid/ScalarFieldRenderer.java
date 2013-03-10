package js.ui.particle.grid;

import js.ui.particle.field.ScalarField;
import js.ui.particle.field.ScalarFieldScanner;
import js.ui.util.OpenGlRenderer;
import js.ui.util.Vec3;

import javax.media.opengl.GL;
import javax.media.opengl.GL4bc;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 11.08.12
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
public class ScalarFieldRenderer implements OpenGlRenderer {

    private final ScalarField<Float> field;

    public ScalarFieldRenderer(ScalarField<Float> field) {
        this.field = field;
    }

    @Override
    public void render(final GL4bc gl) {
        gl.glDisable(gl.GL_LIGHTING);
        field.iterateField(new ScalarFieldScanner<Float>() {
            @Override
            public void scan(Vec3 position, float stepSize) {
                float value = field.getValue(position) * 3;
                gl.glPointSize(Math.min(value, 10));
                gl.glColor3f(1, 1, 1);
                if (value < 1) {
                    return;
                }
                gl.glBegin(GL.GL_POINTS);
                gl.glVertex3f(position.x(), position.y(), position.z());
                gl.glEnd();
            }
        });
    }
}
