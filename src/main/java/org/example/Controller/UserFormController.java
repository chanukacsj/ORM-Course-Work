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
import org.example.bo.custom.UserBo;
import org.example.dto.UserDTO;
import org.example.dto.tm.UserTM;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserFormController {

    @FXML
    private AnchorPane Load;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colRole;

    @FXML
    private TableView<UserTM> tblUser;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    Integer index;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public void initialize() {
        generateNextUserId();
        initializeTable();
        loadAllUsers();
        cmbRole.getItems().addAll("Admin", "Coordinator");
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void loadAllUsers() {
        ObservableList<UserTM> obList = FXCollections.observableArrayList();

        try {
            List<UserDTO> userList = userBo.getAllUsers();

            for (UserDTO userDto : userList) {

                UserTM userTm = new UserTM(
                        userDto.getUserId(),
                        userDto.getUserName(),
                        userDto.getPassword(),
                        userDto.getRole()
                );

                obList.add(userTm);
            }

            tblUser.setItems(obList);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading users: " + e.getMessage(), ButtonType.OK).show();
        }
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        String id = txtId.getText();
        if (result.get() == yes) {
            boolean isDeleted = userBo.deleteUser(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
            }
        }
        loadAllUsers();
        clearFields();
        generateNextUserId();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        boolean isValidate = validateRegister();

        if (isValidate) {
            boolean isSaved = userBo.save(new UserDTO(
                    txtId.getText(),
                    txtName.getText(),
                    txtPassword.getText(),
                    (String) cmbRole.getValue()
            ));
            if (isSaved) {

                new Alert(Alert.AlertType.CONFIRMATION, "User Saved").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "User UnSaved").show();
            }

            loadAllUsers();
            clearFields();
            generateNextUserId();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
       boolean isValidate = validateRegister();
       if (isValidate) {
           String uid = txtId.getText();
           String name = txtName.getText();
           String password = txtPassword.getText();
           String role = (String) cmbRole.getValue();


           if (userBo.updateUser(new UserDTO(uid, name, password, role))) {
               new Alert(Alert.AlertType.CONFIRMATION, "Update Successfully!!").show();
           } else {
               new Alert(Alert.AlertType.ERROR, "Error!!").show();
           }

           loadAllUsers();
           clearFields();
           generateNextUserId();
       }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtPassword.clear();

    }


    public void getUsers(MouseEvent mouseEvent) {
        index = tblUser.getSelectionModel().getSelectedIndex();
        if (index < -1) {
            return;
        } else {
            txtId.setText(tblUser.getItems().get(index).getUserId());
            txtName.setText(tblUser.getItems().get(index).getUserName());
            txtPassword.setText(tblUser.getItems().get(index).getRole());
            cmbRole.setValue(tblUser.getItems().get(index).getPassword());
        }
    }

    private void generateNextUserId() {
        String nextId = null;
        try {
            nextId = userBo.generateNewUserID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(nextId);
    }

    private boolean validateRegister() {
        int num = 0;

        String userName = txtName.getText();
        boolean isUNValidate = Pattern.matches("[A-z]{3,}", userName);
        if (!isUNValidate) {
            num = 1;
            Validate.vibrateTextField(txtName);
        }

        String Password = txtPassword.getText();
        boolean isPWValidate = Pattern.matches("[A-Z a-z 0-9]*$", Password);
        if (!isPWValidate) {
            num = 1;
            Validate.vibrateTextField(txtPassword);
        }
        if (num == 1) {
            num = 0;
            return false;
        } else {
            num = 0;
            return true;

        }
    }
}
