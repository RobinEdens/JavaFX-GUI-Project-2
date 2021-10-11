package Application;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SongController extends Subcontroller implements ChangeListener {
    private Song newSong;
    private ArrayList<Song> songList;
    @FXML Button btnAddSong;
    @FXML Button btnEditSong;
    @FXML Slider sldSongs;
    @FXML TextField txtName;
    @FXML TextField txtLength;

    @FXML Label lblName;
    @FXML Label lblLength;

    public Song getNewSong() {
        return newSong;
    }
    public SongController() {
        super();
    }

    @Override
    public void setEdit(Object album) {
        songList = ((Album)album).getTrackList();
        lblX.setText("1");
        lblY.setText(Integer.toString(songList.size()));
        txtName.setText(songList.get(0).getName());
        txtLength.setText(Integer.toString(songList.get(0).getLength()));
        this.setSlider(sldSongs, songList.size());
    }

    @Override
    public void setView(Object album) {
        songList = ((Album)album).getTrackList();
        lblX.setText("1");
        lblY.setText(Integer.toString(songList.size()));
        lblName.setText(songList.get(0).getName());
        lblLength.setText(Integer.toString(songList.get(0).getLength()));
        this.setSlider(sldSongs, songList.size());
    }

    public void initialize() {
        super.initialize();
        sldSongs.valueProperty().addListener(this);
    }

    @Override
    public void setButtonDisableBind() {
        if (!getLoader().contains("View")) {
            BooleanBinding bind = txtLength.textProperty().isEmpty().or(txtName.textProperty().isEmpty());
            if (getLoader().contains("Add")) {
                super.disableButton(btnAddSong, bind);
            } else if (getLoader().contains("Edit")) {
                super.disableButton(btnEditSong, bind);
            }
        }
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        lblX.setText(Integer.toString((int)sldSongs.getValue()));
        int index = (int)sldSongs.getValue() - 1;

        if (getLoader().contains("Edit")) {
            txtName.setText(songList.get(index).getName());
            txtLength.setText(Integer.toString(songList.get(index).getLength()));
            this.newSong = songList.get(index);
        } else if (getLoader().contains("View")) {
            lblName.setText(songList.get(index).getName());
            lblLength.setText(Integer.toString(songList.get(index).getLength()));
        }

    }

    @FXML
    @Override
    protected void handleAddButtonAction() {
        try {
            newSong = new Song(txtName.getText(), Integer.parseInt(txtLength.getText()));
            Stage stage = (Stage)btnAddSong.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Number mismatch");
            err.setContentText("Only enter numbers for length of song as an int or float");
            err.showAndWait();
        }
    }
    @FXML
    @Override
    protected void handleEditButtonAction() {
        try {
            newSong = new Song(txtName.getText(), Integer.parseInt(txtLength.getText())); // It returns anyway so might as well update it
            int index = (int)sldSongs.getValue() - 1;
            songList.get(index).setName(txtName.getText());
            songList.get(index).setLength(Integer.parseInt(txtLength.getText()));

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Song edited");
            success.setHeaderText("Success");
            success.setContentText("Song has been edited successfully");
            success.showAndWait();
        } catch (NumberFormatException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Number mismatch");
            err.setContentText("Only enter numbers for length of song as an int or float");
            err.showAndWait();
        }
    }
}
