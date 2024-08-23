package it.simoneamighini.scala40.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {
    public static void execute() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Scala 40");
        stage.setFullScreen(false);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);

        SceneLoader.createFirstScene(stage, "fxml/menu.fxml");

        stage.show();
    }
}
