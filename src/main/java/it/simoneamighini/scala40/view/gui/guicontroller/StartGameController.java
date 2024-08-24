package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.ClientMain;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StartGameController implements SceneController {
    @FXML
    TextField username;
    @FXML
    Button enterButton;
    @FXML
    Label connectionResult;

    @FXML
    public void onEnterButtonClick() {
        if (username.getText().isEmpty() || username.getText().length() > 20 || username.getText().contains(" ")) {
            Platform.runLater(
                    () -> {
                        connectionResult.getStyleClass().add("error-text");
                        connectionResult.setText("Username non valido.");
                        enterButton.setDisable(false);
                    }
            );
            return;
        }

        // if check is passed
        Platform.runLater(
                () -> {
                    enterButton.setDisable(true);
                }
        );

        boolean startSuccess = Client.getInstance().start(
                ClientMain.getArguments().serverAddress,
                Integer.parseInt(ClientMain.getArguments().serverPort)
        );

        if (!startSuccess) {
            Platform.runLater(
                    () -> {
                        connectionResult.getStyleClass().add("error-text");
                        connectionResult.setText("Errore nella connessione: " +
                                "verifica la correttezza delle impostazioni di connessione e riprova.");
                        enterButton.setDisable(false);
                    }
            );
            return;
        }

        // if connection is ok
        Platform.runLater(
                () -> {
                    connectionResult.getStyleClass().clear();
                    connectionResult.getStyleClass().add("normal-text");
                    connectionResult.setText("Connessione riuscita.");
                }
        );
    }

    @FXML
    public void onBackButtonClick() {
        Client.getInstance().stop();

        Platform.runLater(() -> SceneLoader.changeScene("fxml/menu.fxml"));
    }

    @Override
    public void handle(Event event) {}
}
