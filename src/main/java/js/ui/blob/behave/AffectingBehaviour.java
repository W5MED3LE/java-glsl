package js.ui.blob.behave;

import js.ui.blob.Blob;
import js.ui.blob.BlobModel;
import js.ui.util.Vec3;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 13.11.11
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class AffectingBehaviour extends Behaviour<Blob> {

    private BlobModel model;

    public AffectingBehaviour(Blob object, BlobModel model) {
        super(object);
        this.model = model;
    }

    @Override
    public boolean caclulate(long time) {
        float bSize = object.getSize();
        for (Blob otherBlob : model.getBlobs()) {
            if (otherBlob == model.getMyBlob() || otherBlob == object || model.getFriends().contains(otherBlob)) {
                continue;
            }
            float oSize = otherBlob.getSize();
            float distance = otherBlob.getPosition().distance(object.getPosition());
            float minDistance = .25f * (float) Math.sqrt(oSize + bSize);
            if (distance < minDistance) {
                float removeSize = .1f * (float) Math.exp(-3 * distance * distance);

                object.setSize(object.getSize() - removeSize);

                Vec3 oColorW = otherBlob.getColor().multiply(oSize);
                Vec3 bColorW = object.getColor().multiply(bSize);


                Vec3 color = bColorW.add(oColorW).multiply(1f / (bSize + oSize));
                otherBlob.setColor(color);
                return object.getSize() > 0;
            }
        }

        return true;
    }
}
