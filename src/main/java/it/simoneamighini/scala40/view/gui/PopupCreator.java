package it.simoneamighini.scala40.view.gui;

import it.simoneamighini.scala40.view.gui.guicontroller.PopupController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PopupCreator {
    public static void show(String sceneTitle, int width, int height, Node node) {
        PopupController.addNode(node);
        Platform.runLater(
                () -> {
                    Stage popupStage = new Stage();
                    Scene popupScene = null;
                    try {
                        popupScene = new Scene(FXMLLoader.load(GuiMain.class.getResource("fxml/popup.fxml")));
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                    popupStage.setScene(popupScene);
                    popupStage.initOwner(SceneLoader.getCurrentStage());
                    popupStage.setFullScreen(false);
                    popupStage.setMinWidth(width);
                    popupStage.setMaxWidth(width);
                    popupStage.setMinHeight(height);
                    popupStage.setMaxHeight(height);
                    popupStage.setTitle(sceneTitle);
                    popupStage.show();
                }
        );
    }
}
