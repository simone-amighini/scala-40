module it.simoneamighini.scala40 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;

    opens it.simoneamighini.scala40 to javafx.fxml;
    exports it.simoneamighini.scala40;
}