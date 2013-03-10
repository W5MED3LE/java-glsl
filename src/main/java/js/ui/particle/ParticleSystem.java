package js.ui.particle;

import js.ui.particle.field.*;
import js.ui.rdr.ModelTimeProvider;
import js.ui.util.ModifiedState;
import js.ui.util.Vec3;
import org.apache.commons.lang.math.RandomUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 19.11.11
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */
public class ParticleSystem extends RecObject implements ModelTimeProvider {

    public static final boolean SCALAR_CUT_SPHERE = false;
    public static final boolean SCALAR_CUT_QUARTER = false;
    public static final boolean SCALAR_CUT_HOLES = false;
    public static final boolean SIM_OTHER_ACC = false;
    public static final int SIM_PARTICLES = 20;

    private final Collection<Particle> particles = new LinkedHashSet<Particle>();

    private long calcCount = 0L;

    private int subdivs;

    public ParticleSystem(int subdivs) {
        this.subdivs = subdivs;
    }

    private final CachedScalarField<Float> densityField = CachedScalarField.cache(new AbstractScalarField<Float>() {

        @Override
        public ModifiedState getModifiedState() {
            return createModifiedState();
        }

        @Override
        public Float getValue(Vec3 position) {
            float size = getSize();
            float hSize = size / 2;
            if (SCALAR_CUT_QUARTER) {
                if (position.x() > hSize && position.y() > hSize && position.z() > hSize) {
                    return 0f;
                }
            }

            float distance = density(position);

            if (SCALAR_CUT_SPHERE) {
                float rad = hSize;
                Vec3 center = Vec3.pos(rad, rad, rad);
                float tooFar = center.distance(position) - rad;
                if (tooFar > 0) {
                    distance = distance / (1 + tooFar * tooFar);
                }
            }

            return distance;
        }

        @Override
        protected float getStepSize() {
            return getSize() / subdivs;
        }

        @Override
        public float getSize() {
            return getDimension().x();
        }
    });

    private ScalarField<Vec3> normalField = new ParticleVectorScalarField() {
        @Override
        protected Vec3 getValue(Vec3 position, Particle particle) {
            return position.sub(particle.getPos());
        }
    };

    private ScalarField<Vec3> colorField = new ParticleVectorScalarField() {
        @Override
        protected Vec3 getValue(Vec3 position, Particle particle) {
            return particle.getColor();
        }
    };

    private abstract class ParticleVectorScalarField extends AbstractScalarField<Vec3> {

        @Override
        public Vec3 getValue(Vec3 position) {
            Vec3 vec = Vec3.nullVec();

            for (Particle p : particles) {
                Vec3 value = getValue(position, p);
                float density = p.getDensity(position);
                vec = vec.add(value.multiply(density));
            }

            return vec.normalize();
        }

        protected abstract Vec3 getValue(Vec3 position, Particle particle);

        @Override
        public float getSize() {
            return getSize() / subdivs;
        }
    }

    public void populate() {
        Random random = new Random(12);
        Vec3 size = getDimension();
        for (int i = 0; i < SIM_PARTICLES; ++i) {
            Particle particle = new Particle();
            particle.setPos(Vec3.pos(
                    RandomUtils.nextFloat(random) * size.x()
                    , RandomUtils.nextFloat(random) * size.y()
                    , RandomUtils.nextFloat(random) * size.z()
            ));
            particle.setVelocity(Vec3.random(-.02f, .02f, random));
            particle.setWeight(RandomUtils.nextFloat(random) * -1f);
            particle.setColor(Vec3.random(.3f, 1));
            particles.add(particle);
        }
    }

    public Collection<Particle> getParticles() {
        return particles;
    }

    public void calculate(long time) {
        Vec3 center = getDimension().divide(2);
        try {
            float[] sizeArray = getDimension().toArray();

            for (Particle p : particles) {
                float w = p.getWeight();
                if (w < 1f) {
                    p.setWeight(w + .02f);
                }

                Vec3 vel = p.getVelocity();
                Vec3 newPos = p.getPos().add(vel);


                float[] posArray = newPos.toArray();
                float[] velArray = vel.toArray();
                for (int i = 0; i < 3; ++i) {
                    if (posArray[i] > sizeArray[i] || posArray[i] < 0) {
                        velArray[i] = -velArray[i];
                    }
                }

                Vec3 newVel = Vec3.pos(velArray);

                if (SIM_OTHER_ACC) {
                    Vec3 otherAcc = Vec3.nullVec();
                    for (Particle p2 : particles) {
                        if (p2 == p) {
                            continue;
                        }
                        float density = p2.getDensity(p.getPos());
                        otherAcc = otherAcc.add(p2.getPos().sub(p.getPos()).multiply(density));
                    }
                    newVel = newVel.add(otherAcc.multiply(0.001f));
                }


                p.setPos(newPos);
                p.setVelocity(newVel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        calcCount++;
        densityField.clear();
    }

    public ModifiedState createModifiedState() {
        return new ModifiedState() {

            long localCounter = calcCount;

            @Override
            public boolean modified() {
                return localCounter != calcCount;
            }
        };
    }

    public float density(Vec3 position) {
        float distance = 0;

        for (Particle p : getParticles()) {
            distance += p.getDensity(position);
        }

        if (SCALAR_CUT_HOLES) {
            if (distance > 1f) {
                return 1 / (distance * distance);
            }
        }

        return distance;
    }

    @Override
    public long getModelTime() {
        return calcCount;
    }

    public CachedScalarField<Float> getDensityField() {
        return densityField;
    }

    public ScalarField<Vec3> getNormalField() {
        return normalField;
    }

    public ScalarField<Vec3> getColorField() {
        return colorField;
    }
}
