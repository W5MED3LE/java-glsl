package js.ui.blob;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 06.11.11
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class BlobController {

    private final BlobModel model;
    private final BlobRenderer renderer;

    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            renderer.setMousePosition(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            renderer.setMousePosition(e.getPoint());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            model.shoot();
        }
    };

    public BlobController(BlobRenderer renderer, BlobModel model) {
        this.renderer = renderer;
        this.model = model;
    }

    public MouseAdapter getMouseListener() {
        return mouseListener;
    }


}
