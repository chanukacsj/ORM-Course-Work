package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.bo.BOFactory;
import org.example.bo.custom.UserBo;
import org.example.dto.UserDTO;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegistrationFormController {

    @FXML
    private ComboBox<String> CmbRole;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRePassword;

    @FXML
    private TextField txtUser;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);
    public void initialize() {
        generateNextUserId();
        CmbRole.getItems().addAll("Admin", "Coordinator");
    }

    @FXML
    void btnSignUpOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException, InterruptedException {
        boolean isValidate = validateRegister();
        if (isValidate) {
            if (txtPassword.getText().equals(txtRePassword.getText())) {
                boolean isSaved = userBo.save(new UserDTO(
                        txtId.getText(),
                        txtUser.getText(),
                        txtPassword.getText(),
                        (String) CmbRole.getValue()
                ));
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User Saved").show();
                    Thread.sleep(1500);
                    if (CmbRole.getValue().equalsIgnoreCase("Admin")) {
                        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

                        Scene scene = new Scene(rootNode);
                        Stage stage = (Stage) this.root.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Admin Form");
                    } else {
                        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-codi_form.fxml"));

                        Scene scene = new Scene(rootNode);
                        Stage stage = (Stage) this.root.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Login Form");
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "User UnSaved").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Password Not Match").show();
            }
        }
    }

    @FXML
    void lblHaveAccountOnMouseClicked(MouseEvent event) {

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
    private void NavigateToLogin() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

    @FXML
    void showPasswordOnMousePresseds(MouseEvent event) {

    }

    @FXML
    void showPasswordOnMouseReleased(MouseEvent event) {

    }


    public void linkLoginPageOnAction(ActionEvent actionEvent) throws IOException {
        NavigateToLogin();
    }
    private boolean validateRegister() {
        int num = 0;

        String userName = txtUser.getText();
        boolean isUNValidate = Pattern.matches("[A-z]{3,}", userName);
        if (!isUNValidate) {
            num = 1;
            Validate.vibrateTextField(txtUser);
        }

        String Password = txtPassword.getText();
        boolean isPWValidate = Pattern.matches("[A-z 0-9]{3,}", Password);
        if (!isPWValidate) {
            num = 1;
            Validate.vibrateTextField(txtPassword);
        }

        String RePassword = txtRePassword.getText();
        boolean isRePWValidate = Pattern.matches("[A-z 0-9]{3,}", RePassword);
        if (!isRePWValidate) {
            num = 1;
            Validate.vibrateTextField(txtRePassword);
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
