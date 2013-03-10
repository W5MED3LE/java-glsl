package js.ui.particle.grid;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 08.08.12
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public class SimpleGridFactory implements GridFactory{

    private Grid grid;

    @Override
    public Grid createGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
