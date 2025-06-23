package com.course.course.Database;
import com.course.course.Models.ActivityType;
import com.course.course.Models.User;
import com.course.course.Models.Vacancy;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Database extends Config {
    private static Connection connection;

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString, user, password);
        return connection;
    }

    public static ResponseWrapper findUser(User user) throws SQLException {
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        ResultSet res = null;
        String select = "SELECT * FROM users WHERE login = ? AND pass = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());

            res = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        while (res.next()) {
            user.setId(res.getString("id"));
        }

        if (user.getId() == null){
            response.wrapError("User not found");
            return response;
        }


        ResultSet res1 = null;
        String select1 = "SELECT * FROM applicants WHERE id = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select1);
            prSt.setString(1, user.getId());

            res1 = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        while (res1.next()) {
            user.setFullName(res1.getString("full_name"));
            user.setQualification(res1.getString("qualification"));
            user.setProfession(res1.getString("profession"));
            user.setPhone(res1.getString("phone"));
            user.setIsApplicant(true);
        }
        if(user.getIsApplicant()){
            response.wrapSuccess(user);
            return response;
        }
        else {
            ResultSet res2 = null;
            String select2 = "SELECT * FROM companys JOIN activitytypes on companys.id_activity = activitytypes.id WHERE companys.id = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(select2);
                prSt.setString(1, user.getId());

                res2 = prSt.executeQuery();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            while (res2.next()) {
                user.setCompanyName(res2.getString("companys.name"));
                user.setCompanyPhone(res2.getString("companys.phone"));
                user.setAddress(res2.getString("companys.address"));
                ActivityType activityType = new ActivityType();
                activityType.setName(res2.getString("activitytypes.name"));
                activityType.setId(res2.getString("activitytypes.id"));
                user.setActivityType(activityType);
                user.setIsApplicant(false);
            }
            if (user.getId() == null){
                response.wrapError("User not found");
            }
            else{response.wrapSuccess(user);}
            return response;
        }

    }
    public  static ResponseWrapper findAllActivityTypes() throws SQLException {
        ResultSet res = null;
        String select = "SELECT * FROM activitytypes";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select);
            res = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        ArrayList<ActivityType> array = new ArrayList<ActivityType>();
        while (res.next()) {
           ActivityType type = new ActivityType();
           type.setId(res.getString("id"));
           type.setName(res.getString("name"));
           array.add(type);
        }
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        response.wrapSuccess(array);
        return response;
    }
    public static ResponseWrapper registerUser(User user) throws SQLException {
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        if (user.getIsApplicant()){
            String insert = "INSERT INTO users (login, pass) VALUE (?,?)";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            ResultSet res = null;
            String select = "SELECT * FROM users WHERE login = ? AND pass = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(select);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());

                res = prSt.executeQuery();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}
            while (res.next()) {
                user.setId(res.getString("id"));
            }
            if(user.getId() == null){
                response.wrapError("User not found");
                return response;
            }
            String insert1 = "INSERT INTO applicants (id, full_name, qualification, profession, phone) VALUE (?,?,?,?,? )";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert1);
                prSt.setString(1, user.getId());
                prSt.setString(2, user.getFullName());
                prSt.setString(3, user.getProfession());
                prSt.setString(4, user.getQualification());
                prSt.setString(5, user.getPhone());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            response.wrapSuccess("uraaaaa");
            return response;



        }
        else {
            String insert = "INSERT INTO users (login, pass) VALUE (?,?)";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            ResultSet res = null;
            String select = "SELECT * FROM users WHERE login = ? AND pass = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(select);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());

                res = prSt.executeQuery();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}
            while (res.next()) {
                user.setId(res.getString("id"));
            }
            if(user.getId() == null){
                response.wrapError("User not found");
                return response;
            }

            String insert1 = "INSERT INTO companys (id, name, phone, address, id_activity) VALUE (?,?,?,?,? )";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert1);
                prSt.setString(1, user.getId());
                prSt.setString(2, user.getCompanyName());
                prSt.setString(3, user.getCompanyPhone());
                prSt.setString(4, user.getAddress());
                prSt.setString(5, user.getActivityType().getId());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            response.wrapSuccess("uraaaaa");
            return response;
        }

    }
    public static ResponseWrapper updateUser(User user) throws SQLException {
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        if (user.getIsApplicant()){
            String insert = "UPDATE users SET login = ?, pass = ? WHERE id = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());
                prSt.setString(3, user.getId());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            String insert1 = "UPDATE applicants SET full_name = ?, phone = ?, profession = ?, qualification = ? WHERE id = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert1);
                prSt.setString(1, user.getFullName());
                prSt.setString(2, user.getPhone());
                prSt.setString(3, user.getProfession());
                prSt.setString(4, user.getQualification());
                prSt.setString(5, user.getId());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            response.wrapSuccess("uraaaaa");
            return response;



        }
        else {
            String insert = "UPDATE users SET login = ?, pass = ? WHERE id = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert);
                prSt.setString(1, user.getLogin());
                prSt.setString(2, user.getPassword());
                prSt.setString(3, user.getId());
                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            String insert1 = "UPDATE companys SET name = ?, phone = ?, address = ?, id_activity = ? WHERE id = ?";
            try {
                PreparedStatement prSt = getConnection().prepareStatement(insert1);
                prSt.setString(1, user.getCompanyName());
                prSt.setString(2, user.getCompanyPhone());
                prSt.setString(3, user.getAddress());
                prSt.setString(4, user.getActivityType().getId());
                prSt.setString(5, user.getId());

                prSt.executeUpdate();
            }
            catch (SQLException e){e.printStackTrace();}
            catch (ClassNotFoundException e){e.printStackTrace();}

            response.wrapSuccess("uraaaaa");
            return response;
        }
    }
    public static ResponseWrapper getAllVacancies() throws SQLException {
        ResultSet res = null;
        String select = "SELECT * FROM vacancies JOIN companys ON vacancies.company_id = companys.id WHERE vacancies.is_active = 1";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select);
            res = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        ArrayList<Vacancy> array = new ArrayList<Vacancy>();
        while (res.next()) {
            Vacancy vacancy = new Vacancy();
            vacancy.setId(res.getString("vacancies.id"));
            vacancy.setCompanyName(res.getString("companys.name"));
            vacancy.setSalary(res.getString("vacancies.salary"));
            vacancy.setTitle(res.getString("vacancies.title"));
            vacancy.setDescription(res.getString("vacancies.description"));
            vacancy.setPhone(res.getString("companys.phone"));
            array.add(vacancy);
        }
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        response.wrapSuccess(array);
        return response;
    }
    public static ResponseWrapper saveVacancy(Vacancy vacancy){
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        String insert1 = "INSERT INTO vacancies (title, salary, description, is_active, company_id) VALUE (?,?,?,1,? )";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(insert1);
            prSt.setString(1, vacancy.getTitle());
            prSt.setString(2, vacancy.getSalary());
            prSt.setString(3, vacancy.getDescription());
            prSt.setString(4, vacancy.getCompanyId());
            prSt.executeUpdate();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        response.wrapSuccess("uraaaaa");
        return response;
    }
    public static ResponseWrapper getVacanciesByCompany(User user) throws SQLException {
        ResultSet res = null;
        String select = "SELECT * FROM vacancies JOIN companys ON vacancies.company_id = companys.id WHERE vacancies.is_active = 1 AND vacancies.company_id = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select);
            prSt.setString(1, user.getId());
            res = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        ArrayList<Vacancy> array = new ArrayList<Vacancy>();
        while (res.next()) {
            Vacancy vacancy = new Vacancy();
            vacancy.setId(res.getString("vacancies.id"));
            vacancy.setCompanyName(res.getString("companys.name"));
            vacancy.setSalary(res.getString("vacancies.salary"));
            vacancy.setTitle(res.getString("vacancies.title"));
            vacancy.setDescription(res.getString("vacancies.description"));
            vacancy.setPhone(res.getString("companys.phone"));
            array.add(vacancy);
        }
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        response.wrapSuccess(array);
        return response;
    }
    public static ResponseWrapper findUserByFullNameAndPhone(User user) throws SQLException {
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        ResultSet res1 = null;
        String select1 = "SELECT * FROM applicants WHERE full_name = ? and phone = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(select1);
            prSt.setString(1, user.getFullName());
            prSt.setString(2, user.getPhone());

            res1 = prSt.executeQuery();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        while (res1.next()) {
            user.setId(res1.getString("id"));
            user.setFullName(res1.getString("full_name"));
            user.setQualification(res1.getString("qualification"));
            user.setProfession(res1.getString("profession"));
            user.setPhone(res1.getString("phone"));
            user.setIsApplicant(true);
        }
        if(user.getIsApplicant()){
            response.wrapSuccess(user);
            return response;
        }
        else{
            response.wrapError("User not found");
            return response;
        }
    }
    public static ResponseWrapper employUser(User user, Vacancy vacancy){
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        String insert1 = "INSERT INTO employments (id_vacancy, id_applicant, post, commission) VALUE (?,?,?,?)";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(insert1);
            prSt.setString(1, vacancy.getId());
            prSt.setString(2, user.getId());
            prSt.setString(3, vacancy.getPosition());
            prSt.setString(4, calculate15Percent(vacancy.getSalary()));
            prSt.executeUpdate();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        response.wrapSuccess("uraaaaa");
        return response;
    }
    private static String calculate15Percent(String numberStr) {
        try {
            // Заменяем точку на запятую, если нужно для Locale
            String formattedNumberStr = numberStr.replace(".", ",");

            // Парсим число
            double number = NumberFormat.getNumberInstance().parse(formattedNumberStr).doubleValue();

            // Вычисляем 15%
            double result = number * 0.15;

            // Форматируем обратно в строку с 2 знаками после запятой и точкой как разделителем
            DecimalFormat df = new DecimalFormat("#0.00");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US)); // Устанавливаем точку как разделитель
            return df.format(result);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Некорректный формат числа: " + numberStr, e);
        }
    }
    public static ResponseWrapper updateVacancy(Vacancy vacancy){
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        String insert1 = "UPDATE vacancies SET title = ?, description = ?, salary = ? WHERE id = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(insert1);
            prSt.setString(1, vacancy.getTitle());
            prSt.setString(2, vacancy.getDescription());
            prSt.setString(3, vacancy.getSalary());
            prSt.setString(4, vacancy.getId());
            prSt.executeUpdate();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        response.wrapSuccess("uraaaaa");
        return response;
    }
    public static ResponseWrapper deleteVacancy(Vacancy vacancy){
        ResponseWrapper response = ResponseWrapper.getInstance();
        response.clear();
        String insert1 = "UPDATE vacancies SET is_active = 0 WHERE id = ?";
        try {
            PreparedStatement prSt = getConnection().prepareStatement(insert1);
            prSt.setString(1, vacancy.getId());
            prSt.executeUpdate();
        }
        catch (SQLException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}

        response.wrapSuccess("uraaaaa");
        return response;
    }
}


