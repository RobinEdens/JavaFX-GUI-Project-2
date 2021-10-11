package Application;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BandController extends Subcontroller implements ChangeListener {
    private BandMember newBand;
    private ArrayList<BandMember> memberList;
    @FXML Button btnAddMember;
    @FXML Button btnEditMember;
    @FXML Slider sldMembers;

    @FXML TextField txtFirst;
    @FXML TextField txtLast;
    @FXML TextField txtInstrument;

    @FXML Label lblFirst;
    @FXML Label lblLast;
    @FXML Label lblInstrument;

    public BandController() {
        super();
    }

    public void initialize() {
        super.initialize();
        sldMembers.valueProperty().addListener(this);
    }

    @Override
    void setButtonDisableBind() {
        if (!getLoader().contains("View")) {
            BooleanBinding bind = txtFirst.textProperty().isEmpty().or(txtLast.textProperty().isEmpty()).or(txtInstrument.textProperty().isEmpty());
            if (getLoader().contains("Add")) {
                super.disableButton(btnAddMember, bind);
            } else if (getLoader().contains("Edit")) {
                super.disableButton(btnEditMember, bind);
            }
        }
    }

    @Override
    public void setEdit(Object album) {
        memberList = ((Album)album).getMembers();
        lblX.setText("1");
        lblY.setText(Integer.toString(memberList.size()));
        txtFirst.setText(memberList.get(0).getfName());
        txtLast.setText(memberList.get(0).getlName());
        txtInstrument.setText(memberList.get(0).getInstruments());
        this.setSlider(sldMembers, memberList.size());
    }

    @Override
    public void setView(Object album) {
        memberList = ((Album)album).getMembers();
        lblX.setText("1");
        lblY.setText(Integer.toString(memberList.size()));
        lblFirst.setText(memberList.get(0).getfName());
        lblLast.setText(memberList.get(0).getlName());
        lblInstrument.setText(memberList.get(0).getInstruments());
        this.setSlider(sldMembers, memberList.size());
    }

    public BandMember getNewBand() {
        return newBand;
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        lblX.setText(Integer.toString((int)sldMembers.getValue()));
        int index = (int)sldMembers.getValue() - 1;

        if (getLoader().contains("Edit")) {
            txtFirst.setText(memberList.get(index).getfName());
            txtLast.setText(memberList.get(index).getlName());
            txtInstrument.setText(memberList.get(index).getInstruments());
            this.newBand = memberList.get(index);
        } else if (getLoader().contains("View")) {
            lblFirst.setText(memberList.get(index).getfName());
            lblLast.setText(memberList.get(index).getlName());
            lblInstrument.setText(memberList.get(index).getInstruments());
        }

    }

    @FXML
    protected void handleAddButtonAction() {
        newBand = new BandMember(txtFirst.getText(), txtLast.getText(), txtInstrument.getText());
        Stage stage = (Stage)btnAddMember.getScene().getWindow();
        stage.close();
    }

    @FXML
    @Override
    protected void handleEditButtonAction() {
        newBand = new BandMember(txtFirst.getText(),txtLast.getText(), txtInstrument.getText()); // It returns anyway so might as well update it
        int index = (int)sldMembers.getValue() - 1;
        memberList.get(index).setfName(txtFirst.getText());
        memberList.get(index).setlName(txtLast.getText());
        memberList.get(index).setInstruments(txtInstrument.getText());

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Member edited");
        success.setHeaderText("Success");
        success.setContentText("Band Member has been edited successfully");
        success.showAndWait();
    }
}
