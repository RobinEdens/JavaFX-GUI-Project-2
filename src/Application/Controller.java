package Application;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public abstract class Controller {
    @FXML BorderPane pane;
    String color;
    private String loader;
    Alert err;

    protected Inventory invAlbum;

    public Controller(){
        this.color = "-fx-background-color: #f2f2f2 ";
        this.err = new Alert(Alert.AlertType.ERROR);
    }

    public void initialize () {
        this.applyColor();
    }

    public void applyColor() {
        this.pane.setStyle(color);
    }

    // Primary way that the Controller's pass colors around
    public void setColor (String color ) {
        this.color = color;
    }

    // Overloaded method that parses a java Color object into CSS, which is how the colors are being applied to the Scene
    // Is used to parse the ColorPicker result
    public void setColor (Color color) {
        this.color = "-fx-background-color: rgb(" + color.getRed() *255 + ", " + color.getGreen()*255 + ", " + color.getBlue()*255 + ");";
    }

    public String getColor(){
        return this.color;
    }

    public void setInvAlbum(Inventory invAlbum) {
        this.invAlbum = invAlbum;
    }
    public Inventory getInvAlbum() {
        return invAlbum;
    }

    public void disableButton (Button btn, BooleanBinding bind ) {
        btn.disableProperty().bind(bind);
    }

    abstract void setButtonDisableBind();

    public String getLoader() {
        return loader;
    }

    public void setLoader(String loader) {
        this.loader = loader;
    }
}
