package com.course.course.Controllers;

import com.course.course.Application;
import com.course.course.Database.CurentUser;
import com.course.course.Database.Database;
import com.course.course.Models.User;
import com.course.course.Models.Vacancy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VacanciesAllController {
    @FXML private FlowPane vacanciesFlowPane;
    @FXML private TextField searchField;
    @FXML private Label noVacanciesLabel;
    @FXML private StackPane vacanciesContainer;
    @FXML private Button createVacancyButton;
    @FXML private Button ourVacanciesButton;
    private User currentUser;
    @FXML
    public void initialize() throws SQLException {
        currentUser = CurentUser.getInstance().getUser();

        // Показываем кнопку создания вакансии только для работодателей
        if (currentUser != null && !currentUser.getIsApplicant()) {
            createVacancyButton.setVisible(true);
            createVacancyButton.setVisible(true);
            createVacancyButton.setManaged(true);
            ourVacanciesButton.setVisible(true);
            ourVacanciesButton.setManaged(true);
        }
        loadVacancies();
    }

    private void loadVacancies() throws SQLException {
        vacanciesFlowPane.getChildren().clear();

        // Получаем вакансии из БД (заглушка)
        ArrayList<Vacancy> vacancies = (ArrayList<Vacancy>) Database.getAllVacancies().getResponseData();

        if (vacancies.isEmpty()) {
            noVacanciesLabel.setVisible(true);
            return;
        }

        noVacanciesLabel.setVisible(false);

        for (Vacancy vacancy : vacancies) {
            vacanciesFlowPane.getChildren().add(createVacancyCard(vacancy));
        }
    }

    private Pane createVacancyCard(Vacancy vacancy) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefWidth(250);

        // Название вакансии
        Label title = new Label(vacancy.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        // Организация
        Label company = new Label(vacancy.getCompanyName());
        company.setStyle("-fx-text-fill: #6c757d;");

        // Зарплата
        Label salary = new Label("Зарплата: " + vacancy.getSalary());
        salary.setStyle("-fx-text-fill: #2a5885; -fx-font-weight: bold;");

        // Кнопка просмотра
        Button viewButton = new Button("Подробнее");
        viewButton.setStyle("-fx-background-color: #2a5885; -fx-text-fill: white;");
        viewButton.setOnAction(e -> {
            try {
                viewVacancyDetails(vacancy);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        card.getChildren().addAll(title, company, salary, viewButton);
        return card;
    }

    private void viewVacancyDetails(Vacancy vacancy) throws IOException {
        // Здесь код для открытия детальной страницы вакансии
        Stage stage = (Stage) vacanciesFlowPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("vacancyView.fxml"));
        Parent root = loader.load();

        // Передаем вакансию в контроллер
        VacancyController controller = loader.getController();
        controller.loadVacancy(vacancy);

        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleSearch() throws SQLException {
        String searchText = searchField.getText().toLowerCase();
        vacanciesFlowPane.getChildren().clear();

        ArrayList<Vacancy> filtered = new ArrayList<>();
        for (Vacancy v : (ArrayList<Vacancy>) Database.getAllVacancies().getResponseData()) {
            if (v.getTitle().toLowerCase().contains(searchText) ||
                    v.getCompanyName().toLowerCase().contains(searchText)) {
                filtered.add(v);
            }
        }

        noVacanciesLabel.setVisible(filtered.isEmpty());
        filtered.forEach(v -> vacanciesFlowPane.getChildren().add(createVacancyCard(v)));
    }

    @FXML
    private void handleEditProfile(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("userProfileView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Поиск вакансий");
        stage.setScene(scene);
        stage.show();
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
    @FXML
    private void handleOurVacancies() {
        // Логика для отображения вакансий текущей компании
        System.out.println("Показать наши вакансии");
        // Здесь можно добавить фильтрацию вакансий по текущей компании
    }
}