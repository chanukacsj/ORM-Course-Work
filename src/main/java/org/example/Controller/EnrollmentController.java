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
import org.example.bo.custom.EnrollmentBo;
import org.example.bo.custom.ProgramBo;
import org.example.bo.custom.StudentBo;
import org.example.dto.EnrollmentDTO;
import org.example.dto.tm.EnrollmentTm;
import org.example.entity.Enrollment;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class EnrollmentController {

    @FXML
    private ComboBox<String> CmbCourseID;

    @FXML
    private ComboBox<String> CmbStID;

    @FXML
    private Label LblCourseName;

    @FXML
    private Label LblStudentName;

    @FXML
    private AnchorPane Load;

    @FXML
    private TableColumn<?, ?> colcomment;

    @FXML
    private TableColumn<?, ?> colcourse_id;

    @FXML
    private TableColumn<?, ?> colcoursename;

    @FXML
    private TableColumn<?, ?> coldate;

    @FXML
    private TableColumn<?, ?> colenrollmentid;

    @FXML
    private TableColumn<?, ?> colremain_fee;

    @FXML
    private TableColumn<?, ?> colstudentid;

    @FXML
    private TableColumn<?, ?> colstudentname;

    @FXML
    private TableColumn<?, ?> colupfront_fee;

    @FXML
    private TableView<EnrollmentTm> tblenrollment;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtTotalFee;

    @FXML
    private TextField txtcomment;

    @FXML
    private TextField txtupfrontpayment;
    Integer index;

    ObservableList<EnrollmentTm> observableList;
    String ID;
    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAMS);
    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    EnrollmentBo enrollmentBo = (EnrollmentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ENROLLMENT);

    public void initialize() throws Exception {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txtDate.setText(today.format(formatter));
        getAll();
        setCellValueFactory();
        loadStudentIds();
        loadCourseID();
        generateNextUserId();

    }

    private void setCellValueFactory() {
        colenrollmentid.setCellValueFactory(new PropertyValueFactory<>("eid"));
        colstudentid.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colstudentname.setCellValueFactory(new PropertyValueFactory<>("StudentName"));
        colcourse_id.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colcoursename.setCellValueFactory(new PropertyValueFactory<>("ProgramName"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colupfront_fee.setCellValueFactory(new PropertyValueFactory<>("upFrontPayment"));
        colremain_fee.setCellValueFactory(new PropertyValueFactory<>("remainingFee"));
        colcomment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        txtId.setText("");
        CmbStID.setValue("");
        LblStudentName.setText("");
        CmbCourseID.setValue("");
        LblCourseName.setText("");
        txtupfrontpayment.setText("");
        txtTotalFee.setText("");
        txtcomment.setText("");
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws Exception {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();
        String id = txtId.getText();
        if (result.orElse(no) == yes) {
            if (!enrollmentBo.deleteEnrollment(id)) {
                new Alert(Alert.AlertType.ERROR, "Error!!").show();
            }
        }
        getAll();
        generateNextUserId();
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws Exception {
        boolean isValidate = validateEnrollment();

        if (isValidate) {
            String id = txtId.getText();
            String sid = (String) CmbStID.getValue();
            String studentName = LblStudentName.getText();
            String pid = (String) CmbCourseID.getValue();
            String programName = LblCourseName.getText();
            LocalDate date = LocalDate.parse(txtDate.getText());
            Double totalFee = Double.valueOf(txtTotalFee.getText());
            Double upFrontPayment = Double.valueOf(txtupfrontpayment.getText());
            Double remainingFee = totalFee - upFrontPayment;
            String comment = txtcomment.getText();

            if (enrollmentBo.EnrollmentIdExists(id)) {
                new Alert(Alert.AlertType.ERROR, "Enrollment ID " + id + " already exists!").show();
                return;
            }

            if (enrollmentBo.isStudentEnrolledInCourse(sid, pid)) {
                new Alert(Alert.AlertType.ERROR, "Student " + sid + " is already enrolled in course " + pid + "!").show();
                return;
            }

            if (enrollmentBo.saveEnrollment(new EnrollmentDTO(id, sid, studentName, pid, programName, date, upFrontPayment, remainingFee, comment))) {

                new Alert(Alert.AlertType.CONFIRMATION, "Saved!!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Error!!").show();
            }
            getAll();
            generateNextUserId();
            clearFields();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws Exception {
        String id = txtId.getText();
        String pid = CmbCourseID.getSelectionModel().getSelectedItem().toString();
        String student_id = CmbStID.getSelectionModel().getSelectedItem().toString();
        LocalDate date = LocalDate.parse(txtDate.getText());
        String comment = txtcomment.getText();
        Double upfront_payment = Double.valueOf(txtupfrontpayment.getText());
        Enrollment enrollmentById = enrollmentBo.findEnrollmentById(id);
        Double newremainfeecalculate = newremainfeecalculate(enrollmentById, upfront_payment);
        String programName = LblCourseName.getText();
        String student_name = LblStudentName.getText();

        if (enrollmentBo.updateEnrollment(new EnrollmentDTO(id, student_id, student_name, pid, programName, date, upfront_payment, newremainfeecalculate, comment))) {

        }
        getAll();
        clearFields();


    }

    private Double newremainfeecalculate(Enrollment enrollment, Double newupfrontpayment) {
        Double upfrontpayment = enrollment.getUpfrontpayment();
        Double remainingfee = enrollment.getRemainingfee();
        double fee = (remainingfee + upfrontpayment) - newupfrontpayment;
        return fee;
    }

    @FXML
    void rowOnMouseClicked(MouseEvent event) {
        index = tblenrollment.getSelectionModel().getSelectedIndex();
        if (index < -1) {
            return;
        } else {
            txtId.setText(colenrollmentid.getCellData(index).toString());
            CmbStID.setValue(colstudentid.getCellData(index).toString());
            LblStudentName.setText(colstudentname.getCellData(index).toString());
            CmbCourseID.setValue(colcourse_id.getCellData(index).toString());
            LblCourseName.setText(colcoursename.getCellData(index).toString());
            txtDate.setText(coldate.getCellData(index).toString());
            txtupfrontpayment.setText(colupfront_fee.getCellData(index).toString());
            txtTotalFee.setText(colremain_fee.getCellData(index).toString());
            txtcomment.setText(colcomment.getCellData(index).toString());
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    private void loadStudentIds() throws Exception {
        ObservableList<String> obList = FXCollections.observableArrayList();

        List<String> idList = studentBo.getStudentID();
        for (String id : idList) {
            obList.add(id);
        }
        CmbStID.setItems(obList);
    }

    public void CmbStIDOnAction(ActionEvent actionEvent) {
        String id = CmbStID.getValue();
        String name = studentBo.getNames(id);

        LblStudentName.setText(name);
    }

    public void loadCourseID() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        List<String> idList = programBo.getProgramIds();
        for (String id : idList) {
            obList.add(id);
        }
        CmbCourseID.setItems(obList);
    }

    public void CmbCourseIDOnAction(ActionEvent actionEvent) {
        String id = CmbCourseID.getValue();
        String name = programBo.getProgramName(id);

        LblCourseName.setText(name);
    }

    private void getAll() throws SQLException, ClassNotFoundException {
        tblenrollment.getItems().clear();
        observableList = FXCollections.observableArrayList();
        List<EnrollmentDTO> allenrollment = enrollmentBo.getAllEnrollment();

        for (EnrollmentDTO enrollmentDTO : allenrollment) {
            observableList.add(new EnrollmentTm(enrollmentDTO.getEid(), enrollmentDTO.getSid(), enrollmentDTO.getStudentName(), enrollmentDTO.getPid(), enrollmentDTO.getProgramName(), enrollmentDTO.getDate(), enrollmentDTO.getUpFrontPayment(), enrollmentDTO.getRemainingFee(), enrollmentDTO.getComment()));
            tblenrollment.setItems(observableList);
        }
    }
    private void generateNextUserId() {
        String nextId = null;
        try {
            nextId = enrollmentBo.generateNewEnrollmentID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(nextId);
    }
    private boolean validateEnrollment() {
        int num=0;

        String comment =txtcomment.getText();
        boolean isCommentValidate= Pattern.matches("^[A-Za-z\\s]{3,}$",comment);
        if (!isCommentValidate){
            num=1;
            Validate.vibrateTextField(txtcomment);
        }
        String amount=txtTotalFee.getText();
        boolean isAmountValidate= Pattern.matches("^\\d+(\\.\\d{1,2})?$",amount);
        if (!isAmountValidate){
            num=1;
            Validate.vibrateTextField(txtTotalFee);
        }
        String upFrontPayment =txtupfrontpayment.getText();
        boolean isupFrontPayment= Pattern.matches("^\\d+(\\.\\d{1,2})?$",upFrontPayment);
        if (!isupFrontPayment){
            num=1;
            Validate.vibrateTextField(txtupfrontpayment);
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
