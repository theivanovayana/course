module com.course.course {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.course.course.Controllers to javafx.fxml;
    exports com.course.course.Controllers;
    exports com.course.course;
}