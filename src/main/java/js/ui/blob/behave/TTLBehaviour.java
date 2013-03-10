package js.ui.blob.behave;

import js.ui.blob.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 06.11.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class TTLBehaviour extends Behaviour<Blob> {

    private long birth = -1;
    private long maxAge;

    public TTLBehaviour(Blob object, long maxAge) {
        super(object);
        this.maxAge = maxAge;
    }

    public boolean caclulate(long time) {
        if (birth < 0) {
            birth = time;
        }
        if(time - birth > maxAge){
            return false;
        }
        return true;
    }

}
