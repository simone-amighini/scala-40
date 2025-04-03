package it.simoneamighini.scala40.view.gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {
    public static void execute() {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Scala 40");
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("images/icons/ICON_16X16.png")),
                new Image(getClass().getResourceAsStream("images/icons/ICON_32X32.png")),
                new Image(getClass().getResourceAsStream("images/icons/ICON_64X64.png")),
                new Image(getClass().getResourceAsStream("images/icons/ICON_128X128.png")),
                new Image(getClass().getResourceAsStream("images/icons/ICON_256X256.png"))
        );
        stage.setFullScreen(false);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);

        SceneLoader.createFirstScene(stage, "fxml/menu.fxml");

        stage.show();
    }
}
