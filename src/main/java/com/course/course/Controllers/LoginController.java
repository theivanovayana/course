package com.course.course.Controllers;


import com.course.course.Application;
import com.course.course.Database.Cryptor;
import com.course.course.Database.CurentUser;
import com.course.course.Database.Database;
import com.course.course.Database.ResponseWrapper;
import com.course.course.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    // Элементы из FXML
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;


    @FXML
    private void handleLogin() throws SQLException {
        User user = new User();
        String login = loginField.getText();
        String password = Cryptor.hashSHA256(passwordField.getText());
        System.out.println(password);
        if (login.equals("") || passwordField.getText().equals("")){
            errorLabel.setText("Введите логин и пароль");
            errorLabel.setVisible(true);
            return;
        }
        user.setPassword(password);
        user.setLogin(login);
        ResponseWrapper response = Database.findUser(user);
        if(response.isSuccess()){
            User curentUser = (User) response.getResponseData();
            CurentUser.getInstance().initSession(curentUser);
        }
        else{
            errorLabel.setText("Пользователь не найден");
            errorLabel.setVisible(true);
        }
        System.out.println(CurentUser.getInstance().getUser().getCompanyName());
    }
    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("registerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Furniture");
        stage.setScene(scene);
        stage.show();
    }
}
