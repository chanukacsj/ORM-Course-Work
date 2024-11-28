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
import org.example.bo.custom.StudentBo;
import org.example.dto.StudentDTO;
import org.example.dto.tm.StudentTM;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class StudentFormController {


    @FXML
    private DatePicker DateReg;

    @FXML
    private AnchorPane Load;

    @FXML
    private DatePicker btnDOB;

    @FXML
    private ComboBox<String> cmbGen;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colDob;

    @FXML
    private TableColumn<?, ?> colGender;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colProgram;

    @FXML
    private TableColumn<?, ?> colRegDate;

    @FXML
    private TableView<StudentTM> tblStudent;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    Integer index;
    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    public void initialize() {
        cmbGen.getItems().addAll("Male", "Female");
        loadAllStudents();
        initializeTable();
        generateNextUserId();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        cmbGen.setValue("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        String id = txtId.getText();
        if (result.get() == yes) {
            boolean isDeleted = studentBo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
            }
        }
        loadAllStudents();
        clearFields();
        generateNextUserId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws Exception {
      boolean isValidate = validateStudent();
       if (isValidate) {
           boolean isSaved = studentBo.save(new StudentDTO(
                   txtId.getText(),
                   txtName.getText(),
                   txtAddress.getText(),
                   txtContact.getText(),
                   btnDOB.getValue().toString(),
                   cmbGen.getValue().toString(),
                   DateReg.getValue().toString()

           ));
           if (isSaved) {
               new Alert(Alert.AlertType.CONFIRMATION, "Student Saved").show();
           } else {
               new Alert(Alert.AlertType.ERROR, "Not Saved").show();
           }
           loadAllStudents();
           clearFields();
           generateNextUserId();
       }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) throws Exception {
        boolean isValidate = validateStudent();
        if (isValidate) {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String contact = txtContact.getText();
            String dob = btnDOB.getValue().toString();
            String gen = cmbGen.getValue().toString();
            String regDate = DateReg.getValue().toString();


            StudentDTO studentDTO = new StudentDTO(id, name, address, contact, dob, gen, regDate);

            boolean isUpdated = studentBo.update(studentDTO);

            if (!isUpdated) {
                System.out.println("update");
                new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Updated").show();
            }


            loadAllStudents();
            clearFields();
        }
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    private List<StudentDTO> loadAllStudents() {
        ObservableList<StudentTM> obList = FXCollections.observableArrayList();

        try {
            List<StudentDTO> allStudents = studentBo.getAll();

            for (StudentDTO studentDTO : allStudents) {
                StudentTM studentTM = new StudentTM(
                        studentDTO.getId(),
                        studentDTO.getName(),
                        studentDTO.getAddress(),
                        studentDTO.getContact(),
                        studentDTO.getDob(),
                        studentDTO.getGen(),
                        studentDTO.getRegDate()
                );

                obList.add(studentTM);
            }
            tblStudent.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gen"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));

    }

    public void getStudent(MouseEvent mouseEvent) {
        index = tblStudent.getSelectionModel().getSelectedIndex();
        if (index < -1) {
            return;
        } else {
            txtId.setText(tblStudent.getItems().get(index).getId());
            txtName.setText(tblStudent.getItems().get(index).getName());
            txtAddress.setText(tblStudent.getItems().get(index).getAddress());
            txtContact.setText(tblStudent.getItems().get(index).getContact());
            btnDOB.setValue(LocalDate.parse(tblStudent.getItems().get(index).getDob()));
            cmbGen.setValue(tblStudent.getItems().get(index).getGen());
            DateReg.setValue(LocalDate.parse(tblStudent.getItems().get(index).getRegDate()));
        }
    }
    private void generateNextUserId() {
        String nextId = null;
        try {
            nextId = studentBo.generateNewStudentID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(nextId);
    }
    private boolean validateStudent() {
        int num=0;

        String name=txtName.getText();
        boolean isNameValidate= Pattern.matches("^[A-Za-z\\s]{3,}$",name);
        if (!isNameValidate){
            num=1;
            Validate.vibrateTextField(txtName);
        }

        String contact=txtContact.getText();
        boolean isContactValidate= Pattern.matches("[0-9]{10}",contact);
        if (!isContactValidate){
            num=1;
            Validate.vibrateTextField(txtContact);
        }

        String dob= String.valueOf(btnDOB.getValue());
        boolean isDobDateValidate= Pattern.matches("[0-9 -]{10}",dob);
        if (!isDobDateValidate){
            num=1;
            Validate.vibrateTextField(btnDOB.getEditor());
        }

        String address=txtAddress.getText();
        boolean isAddressValidate= Pattern.matches("^[A-Za-z\\s]{3,}$",address);
        if (!isAddressValidate){
            num=1;
            Validate.vibrateTextField(txtAddress);
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
