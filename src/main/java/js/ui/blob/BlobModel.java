package js.ui.blob;

import js.ui.blob.behave.*;
import js.ui.util.Vec3;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 05.11.11
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public class BlobModel {

    private static final Logger LOG = LoggerFactory.getLogger(BlobModel.class);

    public static final int MAX_BLOBS = 150;

    private Collection<Blob> blobs = new LinkedHashSet<Blob>();
    private Collection<Behaviour<Blob>> behaviours = new LinkedHashSet<Behaviour<Blob>>();
    private Blob myBlob = new Blob(0, 0, 0);
    private Vec3 mouseCoord;
    private Collection<Blob> friends = new HashSet<Blob>();
    private KineticBehaviour myBlobBehaviour;

    public BlobModel() {
        init();
    }

    public void init() {
        initRandom();
        initMyBlob();
    }

    private void initRandom() {
        for (int i = 0; i < 50; ++i) {
            float x = 8 * (RandomUtils.nextFloat() - .5f);
            float z = 6 * (RandomUtils.nextFloat() - .5f);
            Blob blob = new Blob(x, 0, z);
            blob.setSize(.5f);

            behaviours.add(new SinCosBehaviour(blob));
            blobs.add(blob);
        }
    }

    private void initMyBlob() {
        myBlob = new Blob(0, 0, 0);
        myBlob.setPosition(Vec3.nullVec());
        myBlob.setSize(1f);
        myBlob.setColor(Vec3.pos(.1f, .9f, .1f));
        blobs.add(myBlob);
        myBlobBehaviour = new KineticBehaviour(myBlob);
        myBlobBehaviour.setAcceleration(.5f);
        behaviours.add(myBlobBehaviour);

        behaviours.add(new SpeedColorBehaviour(myBlob, myBlobBehaviour));
        //initTrabs();
    }

    private void initTrabs() {
        final int trabs = 15;
        Blob leader = myBlob;
        for (int i = 0; i < trabs; ++i) {
            final Blob copy = leader.copy();
            copy.setSize(copy.getSize());
            copy.setColor(Vec3.random(0, 1));
            blobs.add(copy);
            friends.add(copy);
            FollowBehaviour fb = new FollowBehaviour(copy, leader);
            fb.setGap(copy.getSize()/2f);
            behaviours.add(fb);
            leader = copy;
        }
    }

    public void setMouseCoord(Vec3 mouseCoord) {
        this.mouseCoord = mouseCoord;
    }

    public Blob getMyBlob() {
        return myBlob;
    }

    public Collection<Blob> getFriends() {
        return friends;
    }

    public void shoot() {
        Vec3 pos = myBlob.getPosition();

        final Blob blob = new Blob(pos.x(), pos.y(), pos.z());
        blob.setColor(myBlob.getColor());
        blob.setSize(0.2f);

        blobs.add(blob);
        behaviours.add(new LinearMoveBehaviour(blob, Vec3.pos(0, 0, -0.1f)));
        behaviours.add(new TTLBehaviour(blob, 100));
        behaviours.add(new AffectingBehaviour(blob, this));
    }

    public List<Blob> getBlobs() {
        return new ArrayList<Blob>(blobs);
    }

    public void calculate(long time) {
        if (mouseCoord != null) {
            myBlobBehaviour.setTarget(mouseCoord);
        }

        Iterator<Behaviour<Blob>> it = behaviours.iterator();
        while (it.hasNext()) {
            Behaviour<Blob> behaviour = it.next();
            Blob blob = behaviour.getObject();
            if (!blobs.contains(blob)) {
                it.remove();
            }
            if (!behaviour.caclulate(time)) {
                blobs.remove(blob);
                it.remove();
            }
        }
    }
}
