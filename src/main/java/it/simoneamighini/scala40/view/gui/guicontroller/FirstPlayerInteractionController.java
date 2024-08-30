package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.WaitingRoomUpdateEvent;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.networking.NetworkObserver;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstPlayerInteractionController implements Initializable, NetworkObserver, SceneController {
    @FXML
    Label selectPlayersNumberLabel;
    @FXML
    ComboBox<Integer> playersNumberComboBox;
    @FXML
    CheckBox resumeSavedGameCheckBox;
    @FXML
    Button nextButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client.getInstance().addObserver(this);
        Platform.runLater(
                () -> {
                    playersNumberComboBox.setItems(FXCollections.observableArrayList(2, 3, 4, 5, 6));
                    if (!Data.getInstance().existsSavedGame()) {
                        resumeSavedGameCheckBox.setDisable(true);
                    }
                    nextButton.setDisable(true);
                }
        );
    }

    @Override
    public void networkStopUpdate() {
        SceneLoader.changeScene("unexpectedDisconnection.fxml");
    }

    @Override
    public void connectionClosingUpdate(String remoteAddress) {}

    @FXML
    public void onComboBoxSelection() {
        nextButton.setDisable(false);
    }

    @FXML
    public void onResumeSavedGameCheckBoxClick() {
        boolean checkBoxSelected = resumeSavedGameCheckBox.isSelected();
        selectPlayersNumberLabel.setDisable(checkBoxSelected);
        playersNumberComboBox.setDisable(checkBoxSelected);
        nextButton.setDisable(!checkBoxSelected);
    }

    @FXML
    public void onNextButtonClick() {
        int playersNumber;
        try {
            playersNumber = playersNumberComboBox.getValue();
        } catch (NullPointerException exception) {
            playersNumber = 0;
        }

        Client.getInstance().send(
                new FirstPlayerChoicesEvent(
                        resumeSavedGameCheckBox.isSelected(),
                        playersNumber
                )
        );
        SceneLoader.changeScene("waitingRoom.fxml");
    }

    @FXML
    public void onExitButtonClick() {
        Client.getInstance().stop();
        SceneLoader.changeScene("menu.fxml");
    }

    @Override
    public void handle(WaitingRoomUpdateEvent event) {
        // do nothing
    }
}
