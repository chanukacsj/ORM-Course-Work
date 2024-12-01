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
import org.example.bo.custom.PaymentBo;
import org.example.bo.custom.ProgramBo;
import org.example.bo.custom.StudentBo;
import org.example.dto.PaymentDTO;
import org.example.dto.tm.PaymentTM;
import org.example.util.Validate;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class PaymentFormController {

    @FXML
    private Label LblDate;
    @FXML
    private Label LblName;


    @FXML
    private AnchorPane Load;

    @FXML
    private ComboBox<String> cmbProgramID;

    @FXML
    private ComboBox<String> cmbStudentID;

    @FXML
    private ComboBox<String> CmbEnrollmentId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colEnrollmentId;

    @FXML
    private TableColumn<?, ?> colPaid;

    @FXML
    private TableView<PaymentTM> tblPay;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtId;

    Integer index;
    PaymentBo paymentBo = (PaymentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    EnrollmentBo enrollmentBo = (EnrollmentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ENROLLMENT);
    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAMS);

    public void initialize() throws Exception {
        initializeTable();
        getAll();
        getEnrollmentIds();
        generateNextUserId();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LblDate.setText(today.format(formatter));

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        String id = txtId.getText();
        if (result.get() == yes) {
            boolean isDeleted = paymentBo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
            }
        }
        getAll();
        clearFields();
        generateNextUserId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws Exception {
        boolean isValidate = validatePayment();

        if (isValidate) {
            boolean isSaved = paymentBo.save(new PaymentDTO(
                    txtId.getText(),
                    LblDate.getText(),
                    Double.parseDouble(txtFee.getText()),
                    CmbEnrollmentId.getValue().toString()

            ));

            if (isSaved) {
                updateRemainingFee();
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Saved").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Saved").show();
            }
            getAll();
            clearFields();
            generateNextUserId();
        }
    }

    private void updateRemainingFee() throws SQLException, ClassNotFoundException {
        try {
            String eid = CmbEnrollmentId.getValue().toString();
            double fee = Double.parseDouble(txtFee.getText());
            double currentRemainingFee = enrollmentBo.getRemainingFeeByEnrollmentId(eid);
            double newRemainingFee = currentRemainingFee - fee;
            boolean isUpdated = enrollmentBo.updateRemainingFee(eid, newRemainingFee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Remaining Fee Updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Not Updated").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void getPay(MouseEvent event) {
        index = tblPay.getSelectionModel().getSelectedIndex();
        if (index < -1) {
            return;
        } else {
            txtId.setText(colId.getCellData(index).toString());
            LblDate.setText(colDate.getCellData(index).toString());
            txtFee.setText(colPaid.getCellData(index).toString());
            CmbEnrollmentId.setValue(colEnrollmentId.getCellData(index).toString());
        }
    }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("pay_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("pay_amount"));
        colEnrollmentId.setCellValueFactory(new PropertyValueFactory<>("eid"));
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    private List<PaymentDTO> getAll() throws SQLException, ClassNotFoundException {
        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();
        try {
            List<PaymentDTO> allPayment = paymentBo.getAll();

            for (PaymentDTO paymentDTO : allPayment) {
                PaymentTM paymentTM = new PaymentTM(
                        paymentDTO.getPay_id(),
                        paymentDTO.getPay_date(),
                        paymentDTO.getPay_amount(),
                        paymentDTO.getEid()
                );

                obList.add(paymentTM);
            }
            tblPay.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void getEnrollmentIds() throws Exception {
        ObservableList<String> obList = FXCollections.observableArrayList();

        List<String> idList = enrollmentBo.getEnrollmentIds();
        for (String id : idList) {
            obList.add(id);
        }
        CmbEnrollmentId.setItems(obList);

    }
//    private void getStudentName() throws Exception {
//
//    }


    public void clearFields() {
        txtId.setText("");
        LblDate.setText("");
        txtFee.setText("");
    }

    private void generateNextUserId() {
        String nextId = null;
        try {
            nextId = paymentBo.generateNewPaymentID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(nextId);
    }

    private boolean validatePayment() {
        int num = 0;

        String contact = txtFee.getText();
        boolean isContactValidate = Pattern.matches("^\\d+(\\.\\d{1,2})?$", contact);
        if (!isContactValidate) {
            num = 1;
            Validate.vibrateTextField(txtFee);
        }

        if (num == 1) {
            num = 0;
            return false;
        } else {
            num = 0;
            return true;

        }
    }

    public void EnrollmentOnAction(ActionEvent actionEvent) {
        String id = CmbEnrollmentId.getValue().toString();
        String name = enrollmentBo.getStudentNameByEnrollmentId(id);
        LblName.setText(name);
    }
}
