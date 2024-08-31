package it.simoneamighini.scala40.view.gui.guicontroller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {
    private static Node node;

    public static void addNode(Node node) {
        PopupController.node = node;
    }

    @FXML
    private VBox space;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        space.getChildren().add(node);
    }
}
