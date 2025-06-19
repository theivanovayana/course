package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Models.Vacancy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class VacancyController {
    @FXML private Label titleLabel;
    @FXML private Label companyLabel;
    @FXML private Label phoneLabel;
    @FXML private Label salaryLabel;
    @FXML private TextArea descriptionArea;

    public void loadVacancy(Vacancy vacancy) {
        titleLabel.setText(vacancy.getTitle());
        companyLabel.setText(vacancy.getCompanyName());
        phoneLabel.setText("Тел.: " + vacancy.getPhone());
        salaryLabel.setText("Зарплата: " + vacancy.getSalary());
        descriptionArea.setText(vacancy.getDescription());
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("vacanciesAllView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
    }
}