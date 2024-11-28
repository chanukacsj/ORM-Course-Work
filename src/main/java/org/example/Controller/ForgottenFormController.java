package org.example.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgottenFormController {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    void LinkOnBackButton(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

    @FXML
    void btnContinueOnAction(ActionEvent event) {

    }

    @FXML
    void userNameKey(KeyEvent event) {

    }

}
