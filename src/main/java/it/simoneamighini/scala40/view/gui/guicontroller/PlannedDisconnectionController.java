package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PlannedDisconnectionController implements Initializable, SceneController {
    @FXML
    private Label disconnectionCauseLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String text;
        switch (Data.getInstance().getDisconnectionCause()) {
            case CLIENT_ERROR -> {
                text = "rimozione di un client a seguito di un comportamento anomalo.";
            }
            case CLIENT_DISCONNECTION -> {
                text = "disconnessione di un giocatore.";
            }
            case FIRST_PLAYER_DISCONNECTION -> {
                text = "disconnessione del primo giocatore nella sala d'attesa " +
                        "prima dell'arrivo delle sue scelte.";
            }
            case NOT_A_SAVED_GAME_PLAYER -> {
                text = "il primo giocatore ha deciso di ricominciare una partita di cui " +
                        "non si faceva parte.";
            }
            case REDUNDANT_PLAYER -> {
                text = "il primo giocatore ha selezionato un numero di giocatori " +
                        "inferiore a quelli presenti in attesa.";
            }
            case null -> {
                criticalErrorManagement();
                return;
            }
        }

        disconnectionCauseLabel.setText(text);
    }

    @FXML
    public void onExitButtonClick() {
        SceneLoader.changeScene("menu.fxml");
    }
}
