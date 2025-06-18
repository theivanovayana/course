package com.course.course.Database;
import com.course.course.Models.ActivityType;
import com.course.course.Models.User;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

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
                activityType.setId(res2.getString("activitytypes.name"));
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
}


