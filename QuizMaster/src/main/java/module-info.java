module com.example.quizmaster {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires java.xml;
    requires java.sql;
    requires java.compiler;
    requires atlantafx.base;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.quizmaster to javafx.fxml;
    opens Models to javafx.base, com.fasterxml.jackson.databind;

    exports com.example.quizmaster;
    exports factories;
    opens factories to javafx.fxml;
}