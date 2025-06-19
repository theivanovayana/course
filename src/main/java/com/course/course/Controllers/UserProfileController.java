package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.Cryptor;
import com.course.course.Database.CurentUser;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserProfileController {
    // Общие поля
    @FXML private Label userTypeLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    // Поля для соискателя
    @FXML private VBox applicantFields;
    @FXML private TextField fullNameField;
    @FXML private TextField qualificationField;
    @FXML private TextField professionField;
    @FXML private TextField phoneField;

    // Поля для работодателя
    @FXML private VBox employerFields;
    @FXML private TextField companyNameField;
    @FXML private TextField companyPhoneField;
    @FXML private TextField addressField;
    @FXML private ComboBox<ActivityType> activityTypeField;

    private User currentUser;
    private ObservableList<ActivityType> activityTypes = FXCollections.observableArrayList();

    public UserProfileController() {
        // Конструктор оставляем пустым, инициализация в initialize()
    }

    @FXML
    private void initialize() throws SQLException {
        // Получаем текущего пользователя из сессии
        currentUser = CurentUser.getInstance().getUser();

        if (currentUser == null) {
            showError("Пользователь не авторизован");
            return;
        }

        // Настраиваем ComboBox
       loadActivityTypes();

        setupUserTypeSpecificFields();
        // Заполняем форму данными пользователя
        populateFormWithUserData();

        // Показываем соответствующие поля в зависимости от типа пользователя

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
        if (!currentUser.getIsApplicant()){
        for (int i = 0; i < list.size(); i++) {
            if (currentUser.getActivityType().getId().equals(list.get(i).getId())){
                activityTypeField.getSelectionModel().select(i);
            }
        }}

    }

    private void populateFormWithUserData() {
        userTypeLabel.setText(currentUser.getIsApplicant() ? "Соискатель" : "Работодатель");
        usernameField.setText(currentUser.getLogin());
        if (currentUser.getIsApplicant()) {
            // Заполняем данные соискателя
            fullNameField.setText(currentUser.getFullName());
            qualificationField.setText(currentUser.getQualification());
            professionField.setText(currentUser.getProfession());
            phoneField.setText(currentUser.getPhone());
        } else {
            // Заполняем данные работодателя
            companyNameField.setText(currentUser.getCompanyName());
            companyPhoneField.setText(currentUser.getCompanyPhone());
            addressField.setText(currentUser.getAddress());

        }
    }

    private void setupUserTypeSpecificFields() {
        boolean isApplicant = currentUser.getIsApplicant();
        applicantFields.setVisible(isApplicant);
        applicantFields.setManaged(isApplicant);
        employerFields.setVisible(!isApplicant);
        employerFields.setManaged(!isApplicant);
    }

    @FXML
    private void handleSave() throws SQLException {
        // Валидация данных
        if (!validateForm()) {
            return;
        }

        // Обновление данных пользователя
        updateUserData();

        // Сохранение в БД
        saveUserChanges();

        // Закрытие формы или обновление интерфейса
    }

    private boolean validateForm() {
        // Проверка паролей (если изменяются)
        if (!passwordField.getText().isEmpty() &&
                !passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Пароли не совпадают");
            return false;
        }

        // Дополнительные проверки в зависимости от типа пользователя
        if (currentUser.getIsApplicant()) {
            if (fullNameField.getText().isEmpty()) {
                showError("ФИО обязательно для заполнения");
                return false;
            }
        } else {
            if (companyNameField.getText().isEmpty()) {
                showError("Название организации обязательно");
                return false;
            }
        }

        return true;
    }

    private void updateUserData() {
        // Обновление общих данных
        if (!passwordField.getText().isEmpty()) {
            currentUser.setPassword(Cryptor.hashSHA256(passwordField.getText()));
            currentUser.setLogin(usernameField.getText());
        }

        // Обновление данных в зависимости от типа пользователя
        if (currentUser.getIsApplicant()) {
            currentUser.setFullName(fullNameField.getText());
            currentUser.setQualification(qualificationField.getText());
            currentUser.setProfession(professionField.getText());
            currentUser.setPhone(phoneField.getText());
        } else {
            currentUser.setCompanyName(companyNameField.getText());
            currentUser.setCompanyPhone(companyPhoneField.getText());
            currentUser.setAddress(addressField.getText());
            currentUser.setActivityType(activityTypeField.getValue());
        }
    }

    private void saveUserChanges() throws SQLException {
        ResponseWrapper response = Database.updateUser(currentUser);
        if (response.isSuccess()){
            CurentUser.getInstance().clearSession();
            CurentUser.getInstance().initSession(currentUser);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vacanciesAllView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();

    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешно");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}