package it.simoneamighini.scala40.view.gui;

import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.view.gui.guicontroller.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    private static Stage currentStage;
    private static SceneController controller;
    private static Scene currentScene;

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static SceneController getCurrentController() {
        return controller;
    }

    public static void createFirstScene(Stage stage, String sceneFXML) throws IOException {
        currentStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource(sceneFXML));
        Scene scene = new Scene(fxmlLoader.load());
        currentScene = scene;
        stage.setScene(scene);

        controller = fxmlLoader.getController();
    }

    public static void changeScene(String sceneFXML) {
        Client.getInstance().stopEventReading();

        Platform.runLater(
                () -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(GuiMain.class.getResource("fxml/" + sceneFXML));

                    try {
                        Parent parent = fxmlLoader.load();
                        currentScene.setRoot(parent);
                        controller = fxmlLoader.getController();
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }

                    currentStage.setOnCloseRequest(event -> System.exit(0));
                    currentStage.show();
                    Client.getInstance().resumeEventReading();
                }
        );
    }
}
