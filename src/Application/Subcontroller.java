package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public abstract class Subcontroller extends Controller {
    public MainController main;

    @FXML Label lblX;
    @FXML Label lblY;

    public Subcontroller(MainController main) {
        super();
        this.setController(main);
        this.invAlbum = new Inventory();
    }
    public Subcontroller() {
        super();
        this.invAlbum = new Inventory();
    }
    public void setController(MainController main) {
        this.main = main;
    }
    public void setSlider(Slider sld, int size) {
        sld.setMin(1);
        sld.setMax(size);
        sld.setBlockIncrement(1);
        sld.setMajorTickUnit(1);
        sld.setMinorTickCount(0);
        sld.setSnapToTicks(true);
        sld.setShowTickMarks(true);
        sld.setShowTickLabels(true);
    }

    public abstract void setEdit(Object o);
    public abstract void setView(Object o);
    protected abstract void handleAddButtonAction();
    protected abstract void handleEditButtonAction();
}
