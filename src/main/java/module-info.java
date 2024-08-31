module it.simoneamighini.scala40 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;
    requires jcommander;
    requires batik.transcoder;
    requires javafx.swing;

    exports it.simoneamighini.scala40;
    exports it.simoneamighini.scala40.networking;
    exports it.simoneamighini.scala40.events;
    exports it.simoneamighini.scala40.view.gui;
    exports it.simoneamighini.scala40.view.gui.guicontroller;

    opens it.simoneamighini.scala40 to javafx.fxml;
    opens it.simoneamighini.scala40.view.gui.guicontroller to javafx.fxml;
}