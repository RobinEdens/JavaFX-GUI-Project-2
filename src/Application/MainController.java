package Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainController extends Controller implements Initializable {
    @FXML ColorPicker colorPick;

    @FXML TitledPane paneAdd;
    @FXML TitledPane paneEdit;
    @FXML TitledPane paneView;
    @FXML TitledPane paneTable;

    @FXML Button btnAddSong;
    @FXML Button btnAddMember;
    @FXML Button btnAdd;

    @FXML Button btnEditSong;
    @FXML Button btnEditBand;
    @FXML Button btnEdit;

    @FXML Button btnViewSong;
    @FXML Button btnViewBand;
    @FXML Button btnPrev;
    @FXML Button btnReset;
    @FXML Button btnNext;
    @FXML Button btnDelete;

    @FXML Button btnLoad;

    @FXML Label lblAddID;
    @FXML Label lblAlbum;
    @FXML Label lblBand;
    @FXML Label lblPublisher;
    @FXML Label lblDate;

    @FXML TextField txtAddAlbum;
    @FXML TextField txtAddBand;
    @FXML TextField txtAddPublisher;

    @FXML TextField txtEditAlbum;
    @FXML TextField txtEditBand;
    @FXML TextField txtEditPublisher;

    @FXML DatePicker dateAdd;
    @FXML DatePicker dateEdit;

    @FXML Spinner editID;
    @FXML Label lblViewID;

    @FXML TableView<Album> tblAlbum;
    @FXML TableColumn<Album, String> tcid;
    @FXML TableColumn<Album, String> tcAlbumName;
    @FXML TableColumn<Album, String> tcBandName;
    @FXML TableColumn<Album, String> tcPublisher;
    @FXML TableColumn<Album, LocalDate> tcDate;
    @FXML TableColumn<Album, Album> tcSongs;
    @FXML TableColumn<Album, Album> tcMembers;

    private boolean firstRun;


    List<Button> btnSongs = new ArrayList<>();
    List<Button> btnMembers = new ArrayList<>();

    ObservableList<Song> obsSong;
    ObservableList<BandMember> obsBand;
    ObservableList<Album> obsAlbum;

    ArrayList<Song> tmpSongs;
    ArrayList<BandMember> tmpMembers;

    public MainController() throws Exception {
        super();
        this.setFirstRun(true);
        tmpSongs = new ArrayList<>();
        tmpMembers = new ArrayList<>();
        this.obsSong = FXCollections.observableArrayList(tmpSongs);
        this.obsBand = FXCollections.observableArrayList(tmpMembers);

        this.obsSong.addListener(new ListChangeListener<Song>() {
            @Override
            public void onChanged(Change<? extends Song> c) {

            }
        });

        this.obsBand.addListener(new ListChangeListener<BandMember>() {
            @Override
            public void onChanged(Change<? extends BandMember> c) {

            }
        });
        this.invAlbum = new Inventory();
        this.loadJSON(this.invAlbum.getAlbumInventory());
        this.obsAlbum = FXCollections.observableArrayList(getInvAlbum().getAlbumInventory());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
        lblAddID.setText(Integer.toString(Album.getTotalAlbums() + 1));
        this.setButtonDisableBind();
        this.loadObservableList();
        this.setSpinners();
        this.setSpinnerLabels();

        lblViewID.setText(obsAlbum.get(0).getId());
        lblAlbum.setText(obsAlbum.get(0).getAlbumName());
        lblBand.setText(obsAlbum.get(0).getBandName());
        lblPublisher.setText(obsAlbum.get(0).getPublisher());
        lblDate.setText(obsAlbum.get(0).getDatePublished().toString());
    }

    public void setSpinners() {
        ArrayList<Album> tmpList = new ArrayList<>();
        obsAlbum.forEach(e -> tmpList.add(e));

        ObservableList tmpObs = FXCollections.observableArrayList(tmpList);
        SpinnerValueFactory<Album> value = new SpinnerValueFactory.ListSpinnerValueFactory<Album>(tmpObs);
        if (!tmpObs.isEmpty()) {
            value.setValue((Album)tmpObs.get(0));
            editID.setValueFactory(value);
            editID.valueProperty().addListener((obs, oldValue, newValue) -> {
                tmpSongs = ((Album)editID.getValue()).getTrackList();
                tmpMembers = ((Album)editID.getValue()).getMembers();
                setSpinnerLabels();
            });
        }

    }

    public void setSpinnerLabels() {
        txtEditAlbum.setText(((Album) editID.getValue()).getAlbumName());
        txtEditBand.setText(((Album) editID.getValue()).getBandName());
        txtEditPublisher.setText(((Album) editID.getValue()).getPublisher());
        dateEdit.setValue(((Album)editID.getValue()).getDatePublished());
    }

    @Override
    public void setButtonDisableBind() {
        BooleanBinding add = txtAddAlbum.textProperty().isEmpty().or(txtAddBand.textProperty().isEmpty())
                .or(txtAddPublisher.textProperty().isEmpty()).or(Bindings.isEmpty(obsSong))
                .or(Bindings.isEmpty(obsBand));
        super.disableButton(btnAdd, add);

        BooleanBinding edit = txtEditAlbum.textProperty().isEmpty().or(txtEditBand.textProperty().isEmpty()).
                or(txtEditPublisher.textProperty().isEmpty());
        this.disableButton(btnEdit, edit);

        BooleanBinding isNull = Bindings.isEmpty(obsAlbum);
        this.disableButton(btnEditBand, isNull);
        this.disableButton(btnEditSong, isNull);
        this.disableButton(btnViewBand, isNull);
        this.disableButton(btnViewSong, isNull);

        this.disableButton(btnPrev, isNull);
        this.disableButton(btnReset, isNull);
        this.disableButton(btnNext, isNull);
        this.disableButton(btnDelete, isNull);
    }

    @FXML
    public void handleAddSongAction(ActionEvent e) throws IOException{
        Song tmp = (Song)this.createPopup("AddSong.fxml", "Add Song to Album", e);
        System.out.println(tmp);
        if (tmp != null) {
            obsSong.add(tmp);
        }
        System.out.println(obsSong.isEmpty());
    }

    private Object createPopup(String fxml, String title, ActionEvent e)  throws IOException {
        if (isFirstRun()) {
            this.loadObservableList();
            this.setFirstRun(false);
        }
        Stage songPopup = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Controller control = loader.getController();
        control.setLoader(fxml);
        control.setInvAlbum(this.getInvAlbum());
        control.setButtonDisableBind();
        Scene scene = new Scene(root);
        if (fxml.contains("Add")) {
            if (fxml.contains(("Song"))) {
                ((SongController)control).sldSongs.setDisable(true);
                ((SongController)control).sldSongs.setVisible(false);
            } else if (fxml.contains(("Band"))) {
                ((BandController)control).sldMembers.setDisable(true);
                ((BandController)control).sldMembers.setVisible(false);
            }
        } else if (fxml.contains("Edit")) {
            ((Subcontroller)control).setEdit(editID.getValue());
        } else {
            Album tmp;
            if ((this.btnSongs.indexOf(e.getSource()) != -1|| this.btnMembers.indexOf(e.getSource()) != -1)) {
                if (fxml.contains("Song")) {
                    tmp = this.invAlbum.getAlbumInventory().get(this.btnSongs.indexOf(e.getSource()));
                } else {
                    tmp = this.invAlbum.getAlbumInventory().get(this.btnMembers.indexOf(e.getSource()));
                }
            } else {
                tmp = this.invAlbum.getAlbumInventory().get(Integer.parseInt(lblViewID.getText()) -1);
            }
            ((Subcontroller)control).setView(tmp);
        }
        songPopup.setTitle(title);
        songPopup.setScene(scene);
        control.setColor(this.getColor());
        control.applyColor();
        songPopup.showAndWait();
        if (fxml.contains("Add") || fxml.contains("Edit")) {
            if (fxml.contains("Song")) {
                return ((SongController)control).getNewSong();
            } else {
                return ((BandController) control).getNewBand();
            }
        } else {
            return null;
        }
    }

    private void clearAddFields() {
        dateAdd.setValue(null);
        txtAddAlbum.clear();
        txtAddBand.clear();
        txtAddPublisher.clear();
        lblAddID.setText(Integer.toString(Album.getTotalAlbums() + 1));
        txtAddAlbum.requestFocus();
        this.tmpSongs.clear();
        this.tmpMembers.clear();
        this.obsBand.clear();
        this.obsSong.clear();
    }

    private void clearEditFields() {
        dateEdit.setValue(null);
        txtEditAlbum.clear();
        txtEditBand.clear();
        txtEditPublisher.clear();
        txtEditAlbum.requestFocus();
        this.tmpSongs.clear();
        this.tmpMembers.clear();
        this.obsBand.clear();
        this.obsSong.clear();
    }

    @FXML
    public void handleAddBandAction(ActionEvent e) throws IOException {
        try {
            BandMember tmp = (BandMember)this.createPopup("AddBandMember.fxml", "Add Band Member to Album", e);
            if (tmp != null) {
                obsBand.add(tmp);
            }
        } catch (NumberFormatException error) {
            err.setTitle("String inputted");
            err.setContentText("Integers only for # of Songs/Album Length");
            err.showAndWait();
        }
    }

    @FXML
    public void handleAddAction (ActionEvent e) {
        Album tmp = new Album(txtAddBand.getText(), txtAddAlbum.getText(), txtAddPublisher.getText(),
                tmpSongs, tmpMembers, dateAdd.getValue());
        this.getInvAlbum().add(tmp);
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Album edited");
        success.setHeaderText("Success");
        success.setContentText("Selected item has been edited.");
        success.showAndWait();
        this.clearAddFields();
        this.setViewLabels(0);
        this.loadObservableList();
    }

    @FXML
    public void handleEditSongAction(ActionEvent e) throws IOException {
        try {
            Song tmp = (Song)this.createPopup("EditSong.fxml", "Edit Songs", e);
            if (tmp != null) {
                obsSong.add(tmp);
            }
        } catch (NumberFormatException error) {
            err.setTitle("String inputted");
            err.setContentText("Integers only for # of Songs/Album Length");
            err.showAndWait();
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }
    }

    @FXML
    public void handleEditBandAction(ActionEvent e) throws IOException {
        try {
            BandMember tmp = (BandMember)this.createPopup("EditBandMember.fxml", "Edit Band Members", e);
            if (tmp != null) {
                obsBand.add(tmp);
            }
        } catch (NumberFormatException error) {
            err.setTitle("String inputted");
            err.setContentText("Integers only for # of Songs/Album Length");
            err.showAndWait();
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }
    }

    @FXML
    public void handleEditAction(ActionEvent e) {
        ((Album)editID.getValue()).setBandName(txtEditBand.getText());
        ((Album)editID.getValue()).setAlbumName(txtEditAlbum.getText());
        ((Album)editID.getValue()).setPublisher(txtEditPublisher.getText());
        ((Album)editID.getValue()).setTrackList(tmpSongs);
        ((Album)editID.getValue()).setMembers(tmpMembers);
        ((Album)editID.getValue()).setDatePublished(dateEdit.getValue());
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Album edited");
        success.setHeaderText("Success");
        success.setContentText("Selected item has been edited.");
        success.showAndWait();
    }

    @FXML
    public void handleViewBandAction(ActionEvent e) throws IOException {
        try {
            this.createPopup("ViewBand.fxml", "View Band Members", e);
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }
    }

    @FXML
    public void handleViewSongAction(ActionEvent e) throws IOException {
        try {
            this.createPopup("ViewSong.fxml", "View Songs", e);
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }
    }

    @FXML
    public void handlePrevAction(ActionEvent e) {
        try {
            int prevIndex = Integer.parseInt(lblViewID.getText()) - 2;
            if (prevIndex >= 0) {
                this.setViewLabels(prevIndex);
            }
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }

    }
    @FXML
    public void handleNextAction(ActionEvent e) {

        try {
            int nextIndex = Integer.parseInt(lblViewID.getText());
            if (nextIndex < this.getInvAlbum().getAlbumInventory().size()) {
                this.setViewLabels(nextIndex);
            }
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }


    }
    @FXML
    public void handleResetAction(ActionEvent e) {

        try {
            this.setViewLabels(0);
        } catch (IndexOutOfBoundsException error ) {
            err.setTitle("List empty");
            err.setContentText("THe list is empty. Perhaps you deleted every element");
            err.showAndWait();
        }

    }
    public void setViewLabels(int index) {
        lblViewID.setText(this.getInvAlbum().getAlbumInventory().get(index).getId());
        lblAlbum.setText(this.getInvAlbum().getAlbumInventory().get(index).getAlbumName());
        lblBand.setText(this.getInvAlbum().getAlbumInventory().get(index).getBandName());
        lblPublisher.setText(this.getInvAlbum().getAlbumInventory().get(index).getPublisher());
        lblDate.setText(this.getInvAlbum().getAlbumInventory().get(index).getDatePublished().toString());
    }

    @FXML
    public void handleDeleteAction(ActionEvent e) {
        if (!invAlbum.getAlbumInventory().isEmpty()) {
            int index = Integer.parseInt(lblViewID.getText()) - 1;
            this.invAlbum.delete(index);
            if (!invAlbum.getAlbumInventory().isEmpty()) {
                if (index == invAlbum.getAlbumInventory().size()) {
                    this.setViewLabels(index-1);
                } else {
                    this.setViewLabels(index);
                }
            } else {
                lblViewID.setText("0");
                lblAlbum.setText("");
                lblBand.setText("");
                lblPublisher.setText("");
                lblDate.setText("");
            }
            this.loadObservableList();
            this.clearAddFields();
            if (!invAlbum.getAlbumInventory().isEmpty()) {
                this.setSpinners();
                this.setSpinnerLabels();
            } else {
                this.clearEditFields();
                this.obsAlbum.clear();
            }
        }
    }

    @FXML
    public void handleLoadAction(ActionEvent e) {
        this.setColor(colorPick.getValue());
        this.applyColor();
        this.applyTitledPane();
        this.loadObservableList();
    }

    public void applyTitledPane() {
        this.paneAdd.setStyle(color);
        this.paneEdit.setStyle(color);
        this.paneTable.setStyle(color);
        this.paneView.setStyle(color);
    }

    public void loadJSON(ArrayList<Album> album) throws Exception {
        Object tmpJSON = new JSONParser().parse(new FileReader("Albums.json"));
        JSONObject jObj = (JSONObject) tmpJSON;
        JSONArray albumJSON = (JSONArray)jObj.get("Albums");

        Iterator itr = albumJSON.iterator();

        while (itr.hasNext()) {
            Iterator itrType;

            JSONObject albumObj = (JSONObject)itr.next();
            String bandName = (String)albumObj.get("band_name");
            String albumName = (String)albumObj.get("album_name");

            JSONArray trackArray = (JSONArray)albumObj.get("track_list");
            Iterator itrTrack = trackArray.iterator();
            ArrayList<Song> tmpSongArray = new ArrayList<>();
            ArrayList<BandMember> tmpBandArray = new ArrayList<>();

            while (itrTrack.hasNext()) {
                itrType = ((Map)itrTrack.next()).entrySet().iterator();
                String name = null;   String length = null;
                while (itrType.hasNext()) {
                    Map.Entry trackPair = (Map.Entry) itrType.next();
                    if (trackPair.getKey().equals("name")) {
                        name = trackPair.getValue().toString();
                    } else if (trackPair.getKey().equals("length")) {
                        length = trackPair.getValue().toString();
                    }
                }
                Song tmpSong = new Song(name, Integer.parseInt(length));
                tmpSongArray.add(tmpSong);
            }

            JSONArray bandArray = (JSONArray)albumObj.get("band_members");
            Iterator itrBand = bandArray.iterator();
            while (itrBand.hasNext()) {
                itrType = ((Map)itrBand.next()).entrySet().iterator();
                String fName = null;  String lName = null;  String  instruments = null;
                while (itrType.hasNext()) {
                    Map.Entry bandPair = (Map.Entry) itrType.next();
                    if (bandPair.getKey().equals("first_name")) {
                        fName = bandPair.getValue().toString();
                    } else if (bandPair.getKey().equals("last_name")) {
                        lName = bandPair.getValue().toString();
                    } else if (bandPair.getKey().equals("instrument")) {
                        instruments = bandPair.getValue().toString();
                    }
                }
                BandMember tmpBand = new BandMember(fName, lName, instruments);
                tmpBandArray.add(tmpBand);
            }

            String publisher = (String)albumObj.get("publisher");
            String tmpDate = (String)albumObj.get("date_published");
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate datePublished = LocalDate.parse(tmpDate, format);
            System.out.println(datePublished);

            Album tmpAlbum = new Album(bandName,albumName, publisher, tmpSongArray, tmpBandArray, datePublished);
            album.add(tmpAlbum);
        }
    }

    public void loadObservableList() {
        this.obsAlbum = FXCollections.observableArrayList(getInvAlbum().getAlbumInventory());
        this.loadTable(obsAlbum);
        this.setSpinners();
        this.setSpinnerLabels();
    }

    public void loadTable (ObservableList<Album> album) {
        btnSongs.clear();
        btnMembers.clear();
        tcid.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        tcAlbumName.setCellValueFactory(
                new PropertyValueFactory<>("albumName")
        );
        tcBandName.setCellValueFactory(
                new PropertyValueFactory<>("bandName")
        );
        tcPublisher.setCellValueFactory(
                new PropertyValueFactory<>("publisher")
        );
        tcDate.setCellValueFactory(
                new PropertyValueFactory<>("datePublished")
        );
        tblAlbum.setItems(album);
        tblAlbum.setEditable(true);

        tcid.setCellFactory(TextFieldTableCell.forTableColumn());
        tcAlbumName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcBandName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcPublisher.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        tcSongs.setCellFactory(c ->  {
            Button btnSong = new Button("Songs");
            this.btnSongs.add(btnSong);
            btnSong.setOnAction(event -> {
                try {
                    this.handleViewSongAction(event);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            TableCell<Album, Album> cell = new TableCell<>() {
                @Override
                public void updateItem(Album album, boolean empty ) {
                    super.updateItem(album, empty);
                    if (empty) {
                        setGraphic(null);
                    }
                    else {
                        setGraphic(btnSong);
                    }
                }
            };
            return cell;});

        tcMembers.setCellFactory(c ->  {
            Button btnMembers = new Button("Members");
            this.btnMembers.add(btnMembers);
            btnMembers.setOnAction(event -> {
                try {
                    this.handleViewBandAction(event);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            TableCell<Album, Album> cell = new TableCell<>() {
                @Override
                public void updateItem(Album album, boolean empty ) {
                    super.updateItem(album, empty);
                    if (empty) {
                        setGraphic(null);
                    }
                    else {
                        setGraphic(btnMembers);
                    }
                }
            };
            return cell;});
        tblAlbum.setEditable(false);
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }
}
