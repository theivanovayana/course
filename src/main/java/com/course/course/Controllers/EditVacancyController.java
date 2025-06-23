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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditVacancyController {
    @FXML private TextField titleField;
    @FXML private TextField salaryField;
    @FXML private TextArea descriptionField;

    private Vacancy currentVacancy;

    // Метод для инициализации данных вакансии
    public void initData(Vacancy vacancy) {
        this.currentVacancy = vacancy;
        titleField.setText(vacancy.getTitle());
        salaryField.setText(vacancy.getSalary());
        descriptionField.setText(vacancy.getDescription());
    }

    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        // Валидация данных
        if (titleField.getText().isEmpty()) {
            showError("Название вакансии обязательно");
            return;
        }

        // Обновление объекта вакансии
        currentVacancy.setTitle(titleField.getText());
        currentVacancy.setSalary(salaryField.getText());
        currentVacancy.setDescription(descriptionField.getText());

        // Сохранение изменений в БД
        ResponseWrapper response = Database.updateVacancy(currentVacancy);

        if (response.isSuccess()) {
            showSuccess("Вакансия успешно обновлена");
            navigateToVacanciesView(event);
        } else {
            showError("Ошибка при обновлении вакансии: " + response.getMessage());
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) throws IOException {
        // Подтверждение удаления
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить эту вакансию?");
        alert.setContentText("Это действие нельзя отменить.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            ResponseWrapper response = Database.deleteVacancy(currentVacancy);
            if (response.isSuccess()) {
                showSuccess("Вакансия успешно удалена");
                navigateToVacanciesView(event);
            } else {
                showError("Ошибка при удалении вакансии: " + response.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        navigateToVacanciesView(event);
    }

    private void navigateToVacanciesView(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vacanciesAllView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
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