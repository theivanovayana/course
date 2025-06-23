package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.CurentUser;
import com.course.course.Database.Database;
import com.course.course.Database.ResponseWrapper;
import com.course.course.Models.Vacancy;
import com.course.course.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OurVacanciesController {
    @FXML private FlowPane vacanciesFlowPane;
    @FXML private Label noVacanciesLabel;
    @FXML private StackPane vacanciesContainer;
    @FXML private Label titleLabel;

    private User currentUser;

    @FXML
    public void initialize() throws SQLException {
        currentUser = CurentUser.getInstance().getUser();
        if (currentUser != null) {
            titleLabel.setText("Вакансии компании " + currentUser.getCompanyName());
            loadOurVacancies();
        }
    }

    private void loadOurVacancies() throws SQLException {
        vacanciesFlowPane.getChildren().clear();

        // Получаем вакансии текущей компании
        ArrayList<Vacancy> vacancies = (ArrayList<Vacancy>) Database.getVacanciesByCompany(currentUser).getResponseData();

        if (vacancies.isEmpty()) {
            noVacanciesLabel.setVisible(true);
            return;
        }

        noVacanciesLabel.setVisible(false);

        // Создаем карточки вакансий
        for (Vacancy vacancy : vacancies) {
            vacanciesFlowPane.getChildren().add(createVacancyCard(vacancy));
        }
    }

    private Pane createVacancyCard(Vacancy vacancy) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefWidth(320);

        // Название вакансии
        Label title = new Label(vacancy.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Зарплата
        Label salary = new Label("Зарплата: " + vacancy.getSalary());
        salary.setStyle("-fx-text-fill: #2a5885; -fx-font-weight: bold;");

        // Описание (сокращенное)
        Label description = new Label(vacancy.getDescription());
        description.setStyle("-fx-text-fill: #6c757d;");
        description.setWrapText(true);
        description.setMaxHeight(60);

        // Кнопки
        HBox buttonsBox = new HBox(10);

        // Кнопка трудоустройства
        Button employButton = new Button("Трудоустройство");
        employButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        employButton.setOnAction(e -> {
            try {
                handleEmployment(vacancy);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Кнопка редактирования (только для компании)
        if (currentUser != null && !currentUser.getIsApplicant()) {
            Button editButton = new Button("Редактировать");
            editButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
            editButton.setOnAction(e -> {
                try {
                    editVacancy(vacancy);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Button deleteButton = new Button("Удалить");
            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> {
                try {
                    deleteVacancy(vacancy);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            buttonsBox.getChildren().addAll(employButton, editButton, deleteButton);
        } else {
            buttonsBox.getChildren().add(employButton);
        }

        card.getChildren().addAll(title, salary, description, buttonsBox);
        return card;
    }

    private void editVacancy(Vacancy vacancy) throws IOException {
        // Логика редактирования вакансии
        Stage stage = (Stage) vacanciesFlowPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("editVacancyView.fxml"));
        Parent root = loader.load();

        // Передаем вакансию в контроллер
        EditVacancyController controller = loader.getController();
        controller.initData(vacancy);

        stage.setScene(new Scene(root));
    }

    private void deleteVacancy(Vacancy vacancy) throws SQLException {
        ResponseWrapper response = Database.deleteVacancy(vacancy);
        loadOurVacancies();
    }

    @FXML
    private void handleCreateVacancy(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("createVacancyView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
    }
    private void handleEmployment(Vacancy vacancy) throws IOException {
        // Логика трудоустройства
        Stage stage = (Stage) vacanciesFlowPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("employmentView.fxml"));
        Parent root = loader.load();

        // Передаем вакансию в контроллер
        EmploymentController controller = loader.getController();
        controller.setVacancy(vacancy);

        stage.setScene(new Scene(root));
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