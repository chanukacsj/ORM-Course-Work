package org.example.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.bo.BOFactory;
import org.example.bo.custom.ProgramBo;
import org.example.bo.custom.StudentBo;
import org.example.dto.ProgramDTO;
import org.example.dto.tm.ProgramTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashBoardController {

    @FXML
    private AnchorPane Load;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colId1;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblCusCount;

    @FXML
    private Label lblEmpCount;

    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane root;


    @FXML
    private TableView<ProgramTM> tblDashBoard;

    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAMS);
    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllPrograms();
        initializeTable();
        initClock();
        setCount();

    }
    private void setUi(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fileName));
        Pane root = fxmlLoader.load();
        try {
            Load.getChildren().clear();
            Load.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        setCount();

    }

    @FXML
    void btnExitOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) throws IOException {
        setUi("payment_form.fxml");
    }

    @FXML
    void btnProgramsOnAction(ActionEvent event) throws IOException {
        setUi("program_form.fxml");
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) throws IOException {
        setUi("student_form.fxml");
    }

    @FXML
    void btnUserOnAction(ActionEvent event) throws IOException {
        setUi("user_form.fxml");
    }

    public void btnEnrollmentOnAction(ActionEvent actionEvent) throws IOException {
        setUi("Enrollment.fxml");
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
            tblDashBoard.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
    private void initializeTable() {
        colId1.setCellValueFactory(new PropertyValueFactory<>("program_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("program_name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("program_fee"));

    }
    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd        HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    public void setCount(){
        try {
            int studentCount = studentBo.getStudentCount();
            lblCusCount.setText(String.valueOf(studentCount));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
