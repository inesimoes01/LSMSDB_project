module com.example.lsmsdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    opens com.example.lsmsdb to javafx.fxml;
    exports com.example.lsmsdb;
    exports com.example.lsmsdb.GUI;
    opens com.example.lsmsdb.GUI to javafx.fxml;
    exports com.example.lsmsdb.Database;
    opens com.example.lsmsdb.Database to javafx.fxml;
    exports com.example.lsmsdb.Database.User;
    opens com.example.lsmsdb.Database.User to javafx.fxml;
}