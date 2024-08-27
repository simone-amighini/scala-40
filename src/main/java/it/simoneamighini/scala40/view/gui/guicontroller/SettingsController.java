package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.ClientMain;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable, SceneController {
    private static String originalServerAddress;
    private static String originalServerPort;
    @FXML
    TextField serverAddress;
    @FXML
    TextField serverPort;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // first time initialisation
        if (originalServerAddress == null && originalServerPort == null) {
            originalServerAddress = ClientMain.getArguments().serverAddress;
            originalServerPort = ClientMain.getArguments().serverPort;
        }

        serverAddress.setText(ClientMain.getArguments().serverAddress);
        serverPort.setText(ClientMain.getArguments().serverPort);
    }

    @FXML
    public void onBackButtonClick() {
        // restore the original arguments if the new ones are invalid
        if (ClientMain.getArguments().serverAddress.isBlank()) {
            ClientMain.getArguments().serverAddress = originalServerAddress;
        }
        if (ClientMain.getArguments().serverPort.isBlank()) {
            ClientMain.getArguments().serverPort = originalServerPort;
        }

        SceneLoader.changeScene("menu.fxml");
    }

    @FXML
    public void onServerAddressChange() {
        ClientMain.getArguments().serverAddress = serverAddress.getText();
    }

    @FXML
    public void onServerPortChange() {
        ClientMain.getArguments().serverPort = serverPort.getText();
    }
}
