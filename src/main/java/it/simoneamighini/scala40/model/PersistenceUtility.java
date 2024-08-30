package it.simoneamighini.scala40.model;

import java.io.*;

public class PersistenceUtility {
    private static final String SAVED_GAME_FILE_NAME = "savedGame.ser";

    public static void saveGameOnDisk(Game game) {
        try {
            if (existsSavedGame()) {
                deleteSavedGame();
            }

            File file = new File(SAVED_GAME_FILE_NAME);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exception) {
            throw new RuntimeException("Error during game saving: " + exception.getMessage());
        }
    }

    public static boolean existsSavedGame() {
        File file = new File(SAVED_GAME_FILE_NAME);
        return file.exists() && !file.isDirectory();
    }

    private static void deleteSavedGame() {
        File file = new File(SAVED_GAME_FILE_NAME);
        file.delete();
    }

    public static Game loadGameFromDisk() {
        try {
            File file = new File(SAVED_GAME_FILE_NAME);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game game = (Game) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return game;
        } catch (Exception exception) {
            return null;
        }
    }

    public static boolean belongsToSavedGame(String username) {
        if (existsSavedGame()) {
            Game game = loadGameFromDisk();
            try {
                return game.getActivePlayers().stream()
                        .map(Player::getUsername)
                        .anyMatch(player -> player.equals(username));
            } catch (Exception exception) {
                deleteSavedGame();
                return false;
            }
        } else {
            return false;
        }
    }

    public static Integer getSavedGamePlayersNumber() {
        if (existsSavedGame()) {
            Game game = loadGameFromDisk();
            return game.getActivePlayers().size();
        } else {
            return null;
        }
    }
}

