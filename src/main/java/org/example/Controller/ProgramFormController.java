package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.example.bo.BOFactory;
import org.example.bo.custom.ProgramBo;
import org.example.dto.ProgramDTO;
import org.example.dto.tm.ProgramTM;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ProgramFormController {

    @FXML
    private AnchorPane Load;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<ProgramTM> tblProgram;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;
    Integer index;
    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAMS);

    public void initialize() {
        loadAllPrograms();
        initializeTable();
        generateNextUserId();
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        generateNextUserId();
    }
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtDuration.setText("");
        txtFee.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        String id = txtId.getText();
        if (result.get() == yes) {
            boolean isDeleted = programBo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
            }
        }
        loadAllPrograms();
        clearFields();
        generateNextUserId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws IOException {
        boolean isValidate = validateProgram();

        if (isValidate) {
            boolean isSaved = programBo.save(new ProgramDTO(
                    txtId.getText(),
                    txtName.getText(),
                    txtDuration.getText(),
                    Double.parseDouble(txtFee.getText())
            ));

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Program Saved").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Saved").show();
            }
            loadAllPrograms();
            clearFields();
            generateNextUserId();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String id = txtId.getText();
        String name = txtName.getText();
        String duration = txtDuration.getText();
        double fee = Double.parseDouble(txtFee.getText());

        ProgramDTO programDTO = new ProgramDTO(id,name,duration,fee);
        boolean isUpdated = programBo.update(programDTO);
        if (isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Not Updated").show();
        }
        loadAllPrograms();
        clearFields();

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }
    private List<ProgramDTO> loadAllPrograms() {
        ObservableList<ProgramTM> obList = FXCollections.observableArrayList();

        try {
            List<ProgramDTO> allPrograms = programBo.getAll();

            for (ProgramDTO programDTO : allPrograms) {
                ProgramTM programTM = new ProgramTM(
                        programDTO.getProgram_id(),
                        programDTO.getProgram_name(),
                        programDTO.getDuration(),
                        programDTO.getProgram_fee()
                );

                obList.add(programTM);
            }
            tblProgram.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("program_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("program_name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("program_fee"));

    }

    public void getPrograms(MouseEvent mouseEvent) {
        index = tblProgram.getSelectionModel().getSelectedIndex();
        if (index < -1) {
            return;
        } else {
            txtId.setText(tblProgram.getItems().get(index).getProgram_id());
            txtName.setText(tblProgram.getItems().get(index).getProgram_name());
            txtDuration.setText(tblProgram.getItems().get(index).getDuration());
            txtFee.setText(String.valueOf(tblProgram.getItems().get(index).getProgram_fee()));
        }
    }
    private void generateNextUserId() {
        String nextId = null;
        try {
            nextId = programBo.generateNewProgramID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(nextId);
    }
    private boolean validateProgram() {
        int num = 0;

        String name=txtName.getText();
        boolean isNameValidate= Pattern.matches("^[A-Za-z-&\\s]{3,}$",name);
        if (!isNameValidate){
            num=1;
            Validate.vibrateTextField(txtName);
        }

        String fee=txtFee.getText();
        boolean isFeeValidate= Pattern.matches("^\\d+(\\.\\d{1,2})?$",fee);
        if (!isFeeValidate){
            num=1;
            Validate.vibrateTextField(txtFee);
        }

        String duration=txtDuration.getText();
        boolean isDurationValidate= Pattern.matches("\\d+\\s+[a-zA-Z]+",duration);
        if (!isDurationValidate){
            num=1;
            Validate.vibrateTextField(txtDuration);
        }

        if(num==1){
            num=0;
            return false;
        }else {
            num=0;
            return true;

        }
    }



}
