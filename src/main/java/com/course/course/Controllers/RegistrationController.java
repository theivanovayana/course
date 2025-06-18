package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.Cryptor;
import com.course.course.Database.Database;
import com.course.course.Database.ResponseWrapper;
import com.course.course.Models.ActivityType;
import com.course.course.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;

public class RegistrationController {
    @FXML private ToggleGroup userTypeGroup;
    @FXML private RadioButton applicantRadio;
    @FXML private RadioButton employerRadio;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    // Поля соискателя
    @FXML private VBox applicantFields;
    @FXML private TextField fullNameField;
    @FXML private TextField qualificationField;
    @FXML private TextField professionField;
    @FXML private TextField phoneField;

    // Поля работодателя
    @FXML private VBox employerFields;
    @FXML private TextField companyNameField;
    @FXML private TextField companyPhoneField;
    @FXML private TextField addressField;
    @FXML private ComboBox<ActivityType> activityTypeField;

    @FXML private Label errorLabel;

    @FXML
    private void initialize() throws SQLException {
        // Загрузка видов деятельности из базы данных
        loadActivityTypes();
    }

    private void loadActivityTypes() throws SQLException {
        ResponseWrapper response = Database.findAllActivityTypes();
        ArrayList<ActivityType> arrayList = (ArrayList<ActivityType>) response.getResponseData();
        ObservableList<ActivityType> list = FXCollections.observableArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            list.add(arrayList.get(i));
        }
        activityTypeField.setItems(list);

        activityTypeField.setCellFactory(lv -> new ListCell<ActivityType>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }

        });
        activityTypeField.setButtonCell(new ListCell<ActivityType>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName()); // Отображаем имя
            }
        });
    }

    @FXML
    private void handleUserTypeChange() {
        boolean isApplicant = applicantRadio.isSelected();
        applicantFields.setVisible(isApplicant);
        applicantFields.setManaged(isApplicant);
        employerFields.setVisible(!isApplicant);
        employerFields.setManaged(!isApplicant);
    }

    @FXML
    private void handleRegistration(ActionEvent event) throws SQLException, IOException {
        errorLabel.setVisible(false);

        // Валидация данных
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showError("Логин и пароль обязательны для заполнения");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Пароли не совпадают");
            return;
        }

        if (applicantRadio.isSelected()) {
            // Валидация данных соискателя
            if (fullNameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                showError("ФИО и телефон обязательны для соискателя");
                return;
            }

            // Логика регистрации соискателя
            registerApplicant(event);
        } else {
            // Валидация данных работодателя
            if (companyNameField.getText().isEmpty() || activityTypeField.getValue() == null) {
                showError("Название организации и вид деятельности обязательны");
                return;
            }

            // Логика регистрации работодателя
            registerEmployer(event);
        }
    }

    private void registerApplicant(ActionEvent event) throws SQLException, IOException {
        // Логика сохранения соискателя в БД
        User user = new User();
        user.setLogin(usernameField.getText());
        user.setPassword(Cryptor.hashSHA256(passwordField.getText()));
        user.setFullName(fullNameField.getText());
        user.setQualification(qualificationField.getText());
        user.setProfession(professionField.getText());
        user.setPhone(phoneField.getText());
        user.setIsApplicant(true);
        ResponseWrapper response = Database.registerUser(user);
        if (response.isSuccess()){
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Furniture");
            stage.setScene(scene);
            stage.show();
        }
    }

    private void registerEmployer(ActionEvent event) throws SQLException, IOException {
        // Логика сохранения работодателя в БД
        User user = new User();
        user.setLogin(usernameField.getText());
        user.setPassword(Cryptor.hashSHA256(passwordField.getText()));
        user.setCompanyName(companyNameField.getText());
        user.setAddress(addressField.getText());
        user.setCompanyPhone(companyPhoneField.getText());
        user.setActivityType(activityTypeField.getValue());
        user.setIsApplicant(false);
        ResponseWrapper response = Database.registerUser(user);
        if (response.isSuccess()){
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Furniture");
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Furniture");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}