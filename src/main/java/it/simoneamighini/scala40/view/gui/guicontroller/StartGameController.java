package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.ClientMain;
import it.simoneamighini.scala40.events.FirstPlayerResponseEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.events.GameEnterResponseEvent;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.networking.NetworkObserver;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class StartGameController implements Initializable, NetworkObserver, SceneController {
    @FXML
    TextField username;
    @FXML
    Button enterButton;
    @FXML
    Label connectionResult;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client.getInstance().addObserver(this);
    }

    @Override
    public void networkStopUpdate() {
        SceneLoader.changeScene("unexpectedDisconnection.fxml");
    }

    @Override
    public void connectionClosingUpdate(String remoteAddress) {}

    @FXML
    public void onEnterButtonClick() {
        String readUsername = username.getText();
        if (readUsername.isEmpty() ||readUsername.length() > 20 || readUsername.contains(" ")) {
            Platform.runLater(
                    () -> {
                        connectionResult.getStyleClass().add("error-text");
                        connectionResult.setText("Username non valido.");
                        enterButton.setDisable(false);
                    }
            );
            Client.getInstance().stop();
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
            Client.getInstance().stop();
            return;
        }

        // if connection is ok
        Client.getInstance().getNewEventReaderThread().start();
        Data.getInstance().setUsername(readUsername);
        Client.getInstance().send(new GameEnterEvent(readUsername));
    }

    @FXML
    public void onBackButtonClick() {
        Client.getInstance().stop();
        SceneLoader.changeScene("menu.fxml");
    }

    @Override
    public void handle(FirstPlayerResponseEvent event) {
        SceneLoader.changeScene("firstPlayerInteraction.fxml");
    }

    @Override
    public void handle(GameEnterResponseEvent event) {
        GameEnterResponseEvent.Response response = event.getResponse();

        String messageToShow;
        switch (response) {
            case ACCEPTED -> {
                SceneLoader.changeScene("waitingRoom.fxml");
                return;
            }
            case REFUSED -> {
                messageToShow = "Connessione rifiutata: il server non accetta nuovi giocatori.";
            }
            case USERNAME_REFUSED -> {
                messageToShow = "Connessione rifiutata: username già in uso o invalido. Ritenta cambiando username.";
            }
            case NOT_A_SAVED_GAME_PLAYER -> {
                messageToShow = "Connessione rifiutata: una partita di cui non si faceva parte è stata ripresa.";
            }
            case null -> {
                criticalErrorManagement();
                return;
            }
        }

        // if there is a message, show it: then shutdown the networking
        Platform.runLater(
                () -> {
                    connectionResult.getStyleClass().clear();
                    connectionResult.getStyleClass().add("normal-text");
                    connectionResult.setText(messageToShow);
                    enterButton.setDisable(false);
                }
        );
        Client.getInstance().stop();
    }
}
