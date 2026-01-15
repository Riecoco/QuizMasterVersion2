module com.example.quizmaster {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires java.xml;
    requires java.sql;
    requires java.compiler;

    opens com.example.quizmaster to javafx.fxml;
    opens Models to com.fasterxml.jackson.databind;
    exports com.example.quizmaster;
    exports factories;
    opens factories to javafx.fxml;
}