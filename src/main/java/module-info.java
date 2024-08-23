module it.simoneamighini.scala40 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;
    requires jcommander;

    exports it.simoneamighini.scala40;
    exports it.simoneamighini.scala40.view.gui;
    exports it.simoneamighini.scala40.view.gui.guicontroller;

    opens it.simoneamighini.scala40 to javafx.fxml;
    opens it.simoneamighini.scala40.view.gui.guicontroller to javafx.fxml;
}