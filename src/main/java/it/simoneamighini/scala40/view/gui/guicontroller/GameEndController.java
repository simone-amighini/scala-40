package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameEndController implements SceneController, Initializable {
    @FXML
    private VBox rankingVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(
                () -> {
                    for (String username : Data.getInstance().getFinalRanking()) {
                        Text text = new Text(
                                username + ": " + Data.getInstance().getUsernamePointsMap().get(username)
                        );
                        text.getStyleClass().add("normal-text");
                        rankingVBox.getChildren().add(text);
                    }

                    rankingVBox.getChildren().add(new Text());

                    String message;
                    if (Data.getInstance().isGameEnded()) {
                        message = "VINCE IL TORNEO " + Data.getInstance().getFinalRanking().getFirst();
                    } else {
                        message = "SEI STATO ELIMINATO.";
                    }
                    Text text = new Text(message);
                    text.getStyleClass().add("error-text");
                    rankingVBox.getChildren().add(text);
                }
        );
    }

    @FXML
    public void onExitButtonClick() {
        Platform.runLater(() -> SceneLoader.changeScene("menu.fxml"));
    }
}
