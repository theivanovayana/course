package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.CurentUser;
import com.course.course.Database.Database;
import com.course.course.Database.ResponseWrapper;
import com.course.course.Models.Vacancy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateVacancyController {
    @FXML private TextField titleField;
    @FXML private TextField salaryField;
    @FXML private TextArea descriptionField;

    @FXML
    private void handleCreate(ActionEvent event) throws IOException {
        // Валидация данных
        if (titleField.getText().isEmpty()) {
            showError("Название вакансии обязательно");
            return;
        }

        // Создание объекта вакансии
        Vacancy newVacancy = new Vacancy();
        newVacancy.setTitle(titleField.getText());
        newVacancy.setCompanyId(CurentUser.getInstance().getUser().getId());
        newVacancy.setSalary(salaryField.getText());
        newVacancy.setDescription(descriptionField.getText());

        // Сохранение в БД (заглушка)
        ResponseWrapper response = Database.saveVacancy(newVacancy);

        if (response.isSuccess()) {
            showSuccess("Вакансия успешно создана");
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vacanciesAllView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Поиск вакансий");
            stage.setScene(scene);
            stage.show();
        } else {
            showError("Ошибка при создании вакансии");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        showSuccess("Вакансия успешно создана");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vacanciesAllView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успешно");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}