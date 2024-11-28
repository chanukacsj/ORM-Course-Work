package org.example.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.bo.BOFactory;
import org.example.bo.custom.UserBo;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        boolean isValidate = validateLogin();

        if (isValidate) {

            String username = txtUserName.getText();
            String password = txtPassword.getText();
            String role = userBo.getRole(username);

            if (userBo.checkPassword(username, password)) {
                if (role.equalsIgnoreCase("admin")) {
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
                new Alert(Alert.AlertType.ERROR, "Please Check Username and password !!").show();
            }
        }

    }

    @FXML
    void linkForgottenPasswordOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/forgotten_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Forgotten Form");
    }

    @FXML
    void linkRegistrationOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/SignUp_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SinUp Form");
    }

    @FXML
    void userNameKey(KeyEvent event) {

    }

    private boolean validateLogin() {
        int num = 0;

        String userName = txtUserName.getText();
        boolean isUNValidate = Pattern.matches("[A-z]{3,}", userName);
        if (!isUNValidate) {
            num = 1;
            Validate.vibrateTextField(txtUserName);
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
