module com.course.course {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.course.course to javafx.fxml;
    exports com.course.course;
}