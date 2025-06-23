package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.ResponseWrapper;
import com.course.course.Models.User;
import com.course.course.Models.Vacancy;
import com.course.course.Database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EmploymentController {
    @FXML private Label titleLabel;
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private Label messageLabel;
    @FXML private TextField positionField;

    private Vacancy currentVacancy;

    public void setVacancy(Vacancy vacancy) {
        this.currentVacancy = vacancy;
        titleLabel.setText("Трудоустройство на вакансию: " + vacancy.getTitle());
    }

    @FXML
    private void handleEmployment() throws SQLException {
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();

        // Валидация
        if (fullName.isEmpty() || phone.isEmpty()) {
            showMessage("Все поля обязательны для заполнения", true);
            return;
        }

        User user = new User();
        user.setPhone(phone);
        user.setFullName(fullName);
        ResponseWrapper response = Database.findUserByFullNameAndPhone(user);

        if (!response.isSuccess()) {
            showMessage("Пользователь с такими данными не найден", true);
            return;
        }

        User candidate = (User) response.getResponseData();



        currentVacancy.setPosition(positionField.getText());
        ResponseWrapper response1 = Database.employUser(candidate, currentVacancy);

        if (response1.isSuccess()) {
            showMessage("Сотрудник " + fullName + " успешно трудоустроен!", false);
            clearFields();
        } else {
            showMessage("Ошибка при трудоустройстве", true);
        }
    }

    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.setStyle(isError ? "-fx-text-fill: #dc3545;" : "-fx-text-fill: #28a745;");
        messageLabel.setVisible(true);
    }

    private void clearFields() {
        fullNameField.clear();
        phoneField.clear();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ourVacanciesView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
    }
}