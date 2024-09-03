package it.simoneamighini.scala40.view.gui.guicontroller;

import it.simoneamighini.scala40.events.CancelTurnConfirmationEvent;
import it.simoneamighini.scala40.events.MatchInfoUpdateEvent;
import it.simoneamighini.scala40.events.TurnEndEvent;
import it.simoneamighini.scala40.events.TurnStartEvent;
import it.simoneamighini.scala40.networking.Client;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.PopupCreator;
import it.simoneamighini.scala40.view.gui.SVGLoader;
import it.simoneamighini.scala40.view.gui.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchController implements SceneController, Initializable {
    @FXML
    private Label startTypeLabel;
    @FXML
    private Label matchNumberLabel;
    @FXML
    private Label turnNumberLabel;
    @FXML
    private VBox playersInfoVBox;
    @FXML
    private ImageView deckImageView;
    @FXML
    private ImageView discardedCardsImageView;
    @FXML
    private Button drawFromDeckButton;
    @FXML
    private Button pickFromDiscardedCardsButton;
    @FXML
    private VBox groupsVBox;
    @FXML
    private Button cancelTurnButton;
    @FXML
    private Button openingButton;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button resetAddedGroupsButton;
    @FXML
    private Button viewAddedGroupsButton;
    @FXML
    private Button placeGroupButton;
    @FXML
    private Button attachGroupButton;
    @FXML
    private Button attachCardButton;
    @FXML
    private Button replaceJollyButton;
    @FXML
    private Button discardCardButton;
    @FXML
    private VBox handVBox;

    private final List<HBox> internalGroupHBoxes = new ArrayList<>();
    private final List<Map<String, ImageView>> groupsCardIDImageViewMaps = new ArrayList<>();
    private final List<List<String>> selectedCardsInGroups = new ArrayList<>();

    private HBox internalHandHBox;
    private final Map<String, ImageView> handCardIDImageViewMap = new HashMap<>();
    private final List<String> selectedCardIDsInHand = new ArrayList<>();

    private final List<List<String>> groupsAddedForOpening = new ArrayList<>();

    private final List<WildCard> selectedGroupsWildCards = new ArrayList<>();

    public static class WildCard extends Rectangle {
        public enum Position {
            START,
            END
        }

        private final int groupNumber;
        private final Position position;

        public WildCard(int groupNumber, Position position) {
            super(130, 180);
            this.groupNumber = groupNumber;
            this.position = position;

            setFill(Color.TRANSPARENT);
            setArcHeight(15);
            setArcWidth(15);
        }

        public int getGroupNumber() {
            return groupNumber;
        }

        public Position getPosition() {
            return position;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableAllMatchRelatedButtons();
    }

    @Override
    public void handle(MatchInfoUpdateEvent event) {
        disableAllMatchRelatedButtons();
        initializeInfo();
        fillStartTypeLabel();
        fillMatchAndTurnLabel();
        showDeckAndDiscardedCard();
        showGroups();
        showHand();
        showPlayersInfo();
    }

    @Override
    public void handle(TurnStartEvent event) {
        if (event.getUsername().equals(Data.getInstance().getUsername())) {
            Platform.runLater(
                    () -> {
                        drawFromDeckButton.setDisable(false);
                        pickFromDiscardedCardsButton.setDisable(!Data.getInstance().hasPlayerOpened());
                    }
            );
        }
    }

    @Override
    public void handle(TurnEndEvent event) {
        Data.getInstance().setPlayerAlreadyPickedACard(false);
    }

    @Override
    public void handle(CancelTurnConfirmationEvent event) {
        Data.getInstance().setPlayerAlreadyPickedACard(false);
    }

    private void disableAllMatchRelatedButtons() {
        Platform.runLater(
                () -> {
                    drawFromDeckButton.setDisable(true);
                    pickFromDiscardedCardsButton.setDisable(true);
                    openingButton.setDisable(true);
                    addGroupButton.setDisable(true);
                    resetAddedGroupsButton.setDisable(true);
                    viewAddedGroupsButton.setDisable(true);
                    placeGroupButton.setDisable(true);
                    attachGroupButton.setDisable(true);
                    attachCardButton.setDisable(true);
                    replaceJollyButton.setDisable(true);
                    discardCardButton.setDisable(true);
                    cancelTurnButton.setDisable(true);
                }
        );
    }

    private void enableValidMatchRelatedButtons() {
        Platform.runLater(
                () -> {
                    openingButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    Data.getInstance().hasPlayerOpened() ||
                                    groupsAddedForOpening.isEmpty()
                    );
                    addGroupButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    Data.getInstance().hasPlayerOpened() ||
                                    selectedCardIDsInHand.size() <= 2
                    );
                    resetAddedGroupsButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    Data.getInstance().hasPlayerOpened() ||
                                    groupsAddedForOpening.isEmpty()
                    );
                    viewAddedGroupsButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    Data.getInstance().hasPlayerOpened() ||
                                    groupsAddedForOpening.isEmpty()
                    );
                    discardCardButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    selectedCardIDsInHand.size() != 1
                    );
                    attachGroupButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    !Data.getInstance().hasPlayerOpened() ||
                                    selectedGroupsWildCards.size() != 1 ||
                                    selectedCardIDsInHand.size() <= 2 ||
                                    getNumberOfSelectedCardInGroups() != 0
                    );
                    attachCardButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    !Data.getInstance().hasPlayerOpened() ||
                                    selectedGroupsWildCards.size() != 1 ||
                                    selectedCardIDsInHand.size() != 1 ||
                                    getNumberOfSelectedCardInGroups() != 0
                    );
                    replaceJollyButton.setDisable(
                            !Data.getInstance().hasPlayerAlreadyPickedACard() ||
                                    !Data.getInstance().hasPlayerOpened() ||
                                    getNumberOfSelectedCardInGroups() != 1 ||
                                    selectedCardIDsInHand.size() != 1 ||
                                    !selectedGroupsWildCards.isEmpty()
                    );
                }
        );
    }

    private int getNumberOfSelectedCardInGroups() {
        AtomicInteger number = new AtomicInteger();
        selectedCardsInGroups.forEach(group -> number.addAndGet(group.size()));
        return number.get();
    }

    private void initializeInfo() {
        Platform.runLater(
                () -> {
                    deckImageView.setImage(null);
                    discardedCardsImageView.setImage(null);

                    groupsVBox.getChildren().clear();
                    groupsCardIDImageViewMaps.clear();
                    selectedCardsInGroups.clear();

                    handVBox.getChildren().clear();
                    handCardIDImageViewMap.clear();
                    selectedCardIDsInHand.clear();

                    groupsAddedForOpening.clear();

                    selectedGroupsWildCards.clear();

                    playersInfoVBox.getChildren().clear();
                }
        );
    }

    private void fillStartTypeLabel() {
        Platform.runLater(
                () -> {
                    switch (Data.getInstance().getGameStartType()) {
                        case NEW_GAME -> {
                            startTypeLabel.setText("Nuovo torneo");
                        }
                        case RESUMED_GAME -> {
                            startTypeLabel.setText("Torneo ripreso");
                        }
                        case null -> {
                            return; // do not show the label
                        }
                    }
                    startTypeLabel.setVisible(true);
                }
        );
    }

    private void fillMatchAndTurnLabel() {
        Platform.runLater(
                () -> {
                    matchNumberLabel.setText(Integer.toString(Data.getInstance().getMatchNumber()));
                    matchNumberLabel.setVisible(true);
                    turnNumberLabel.setText(Integer.toString(Data.getInstance().getTurnNumber()));
                    turnNumberLabel.setVisible(true);
                }
        );
    }

    private void showDeckAndDiscardedCard() {
        String deckTopCardBackFileName = "BACK_" +
                Arrays.stream(Data.getInstance().getDeckTopCardID().split("_"))
                .toList()
                .getLast() +
                ".svg";
        String visibleDiscardedCardFileName = convertCardIDToFileName(Data.getInstance().getVisibleDiscardedCardID()) +
                ".svg";

        Platform.runLater(
                () -> {
                    try {
                        deckImageView.setImage(SVGLoader.load("cards/" + deckTopCardBackFileName));
                    } catch (NullPointerException exception) {
                        // show nothing
                    }

                    try {
                        discardedCardsImageView.setImage(SVGLoader.load("cards/" + visibleDiscardedCardFileName));
                    } catch (NullPointerException exception) {
                        // show nothing
                    }
                }
        );
    }

    private ScrollPane generateCardsScrollPane() {
        // generate scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setMinHeight(220);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(true);

        // insert a HBox
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        scrollPane.setContent(hbox);

        return scrollPane;
    }

    private ImageView generateImageView(String cardID) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(130);
        imageView.setFitHeight(180);
        imageView.setImage(SVGLoader.load("cards/" + convertCardIDToFileName(cardID) + ".svg"));
        return imageView;
    }

    private void showGroups() {
        Platform.runLater(
                () -> {
                    List<List<String>> groups = Data.getInstance().getGroups();
                    try {
                        for (int i = 0; i < groups.size(); i++) {
                            // generate scroll pane
                            ScrollPane generatedScrollPane = generateCardsScrollPane();
                            groupsVBox.getChildren().add(generatedScrollPane);

                            // initialize group data and add wild cards + cards
                            internalGroupHBoxes.add((HBox) generatedScrollPane.getContent());
                            groupsCardIDImageViewMaps.add(i, new HashMap<>());
                            selectedCardsInGroups.add(i, new ArrayList<>());

                            WildCard startWildCard = new WildCard(i, WildCard.Position.START);
                            startWildCard.setOnMouseClicked(event -> onWildCardMouseClick(startWildCard));
                            startWildCard.setOnMouseEntered(event -> onWildCardMouseEnter(startWildCard));
                            startWildCard.setOnMouseExited(event -> onWildCardMouseExit(startWildCard));
                            internalGroupHBoxes.get(i).getChildren().add(startWildCard);

                            for (String cardID : groups.get(i)) {
                                addCardToGroup(i, cardID, generateImageView(cardID));
                            }

                            WildCard endWildCard = new WildCard(i, WildCard.Position.END);
                            endWildCard.setOnMouseClicked(event -> onWildCardMouseClick(endWildCard));
                            endWildCard.setOnMouseEntered(event -> onWildCardMouseEnter(endWildCard));
                            endWildCard.setOnMouseExited(event -> onWildCardMouseExit(endWildCard));
                            internalGroupHBoxes.get(i).getChildren().add(endWildCard);
                        }
                    } catch (NullPointerException exception) {
                        // show nothing
                    }
                }
        );
    }

    private void addCardToGroup(int groupNumber, String cardID, ImageView imageView) {
        Platform.runLater(
                () -> {
                    internalGroupHBoxes.get(groupNumber).getChildren().add(imageView);
                    imageView.setOnMouseClicked(event -> onGroupCardImageViewClick(imageView));
                    groupsCardIDImageViewMaps.get(groupNumber).put(cardID, imageView);
                }
        );
    }

    private Integer getGroupNumberOfImageView(ImageView imageView) {
        Integer groupNumber = null;
        for (int i = 0; i < groupsCardIDImageViewMaps.size(); i++) {
            if (groupsCardIDImageViewMaps.get(i).containsValue(imageView)) {
                groupNumber = i;
                break;
            }
        }
        return groupNumber;
    }

    private String getCardIDFromImageView(Map<String, ImageView> map, ImageView imageView) {
        return map.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), imageView))
                .map(Map.Entry::getKey)
                .toList().getFirst();
    }

    public void onGroupCardImageViewClick(ImageView imageView) {
        Integer groupNumber = getGroupNumberOfImageView(imageView);
        String relatedCardID = getCardIDFromImageView(groupsCardIDImageViewMaps.get(groupNumber), imageView);
        boolean cardAlreadySelected = selectedCardsInGroups.get(groupNumber).contains(relatedCardID);

        Platform.runLater(
                () -> {
                    if (cardAlreadySelected) {
                        // remove it from selected cards and make it available
                        selectedCardsInGroups.get(groupNumber).remove(
                                getCardIDFromImageView(groupsCardIDImageViewMaps.get(groupNumber), imageView)
                        );
                        imageView.setEffect(null);
                    } else {
                        // add it to selected cards and make it unavailable
                        selectedCardsInGroups.get(groupNumber).add(
                                groupsCardIDImageViewMaps.get(groupNumber).entrySet().stream()
                                        .filter(entry -> Objects.equals(entry.getValue(), imageView))
                                        .map(Map.Entry::getKey)
                                        .toList().getFirst()
                        );
                        applyShadowEffect(imageView);
                    }

                    enableValidMatchRelatedButtons();
                }
        );
    }

    public void onWildCardMouseClick(WildCard wildCard) {
        Platform.runLater(
                () -> {
                    if (selectedGroupsWildCards.contains(wildCard)) {
                        // remove it from selected cards and make it available
                        selectedGroupsWildCards.remove(wildCard);
                        wildCard.setFill(Color.TRANSPARENT);
                    } else {
                        // add it to selected cards and make it unavailable
                        selectedGroupsWildCards.add(wildCard);
                        wildCard.setFill(Color.BEIGE);
                    }

                    enableValidMatchRelatedButtons();
                }
        );
    }

    public void onWildCardMouseEnter(WildCard wildCard) {
        Platform.runLater(() -> wildCard.setFill(Color.BEIGE));
    }

    public void onWildCardMouseExit(WildCard wildCard) {
        Platform.runLater(
                () -> {
                    if (!selectedGroupsWildCards.contains(wildCard)) {
                        wildCard.setFill(Color.TRANSPARENT);
                    }
                }
        );
    }

    private void applyShadowEffect(ImageView imageView) {
        Platform.runLater(
                () -> {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-0.5);
                    imageView.setEffect(colorAdjust);
                }
        );
    }

    private void applyDarkEffect(ImageView imageView) {
        Platform.runLater(
                () -> {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-50);
                    imageView.setEffect(colorAdjust);
                }
        );
    }

    private void showHand() {
        Platform.runLater(
                () -> {
                    List<String> hand = Data.getInstance().getHand();
                    try {
                        // generate scroll pane
                        ScrollPane generatedScrollPane = generateCardsScrollPane();
                        handVBox.getChildren().add(generatedScrollPane);

                        // add cards
                        internalHandHBox = (HBox) generatedScrollPane.getContent();
                        for (String cardID : hand) {
                            addCardToHand(cardID, generateImageView(cardID));
                        }
                    } catch (NullPointerException exception) {
                        // show nothing
                    }
                }
        );
    }

    private void addCardToHand(String cardID, ImageView imageView) {
        Platform.runLater(
                () -> {
                    internalHandHBox.getChildren().add(imageView);
                    imageView.setOnMouseClicked( event -> onHandCardImageViewClick(imageView));
                    handCardIDImageViewMap.put(cardID, imageView);
                }
        );
    }

    public void onHandCardImageViewClick(ImageView imageView) {
        boolean cardAlreadySelected = selectedCardIDsInHand.contains(
                getCardIDFromImageView(handCardIDImageViewMap, imageView)
        );

        Platform.runLater(
                () -> {
                    if (cardAlreadySelected) {
                        // remove it from selected cards and make it available
                        selectedCardIDsInHand.remove(getCardIDFromImageView(handCardIDImageViewMap, imageView));
                        imageView.setEffect(null);
                    } else {
                        // add it to selected cards and make it unavailable
                        selectedCardIDsInHand.add(
                                handCardIDImageViewMap.entrySet().stream()
                                        .filter(entry -> Objects.equals(entry.getValue(), imageView))
                                        .map(Map.Entry::getKey)
                                        .toList().getFirst()
                        );
                        applyShadowEffect(imageView);
                    }

                    enableValidMatchRelatedButtons();
                }
        );
    }

    private void showPlayersInfo() {
        Platform.runLater(
                () -> {
                    playersInfoVBox.getChildren().clear();

                    Label titleLabel = new Label("[nome]: [punti]     [aperto] [#carte]");
                    titleLabel.getStyleClass().add("small-text");
                    titleLabel.setStyle("-fx-text-fill: rgb(64,64,64)");
                    playersInfoVBox.getChildren().add(titleLabel);

                    for (String username : Data.getInstance().getUsernamePointsMap().keySet()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(username).append(": ");
                        builder.append(Data.getInstance().getUsernamePointsMap().get(username));
                        builder.append("     ");
                        if (Data.getInstance().getUsernameOpeningCompletedMap().get(username)) {
                            // player has completed the opening
                            builder.append("[✔]");
                        } else {
                            // opening not completed
                            builder.append("[✖]");
                        }
                        builder.append("  ")
                                .append("[")
                                .append(Data.getInstance().getUsernameRemainingCardsMap().get(username))
                                .append("]");

                        Label label = new Label(builder.toString());
                        label.getStyleClass().add("small-text");
                        if (username.equals(Data.getInstance().getUsername())) {
                            // highlight info
                            label.setStyle("-fx-text-fill: rgb(189, 11, 11)");
                        }

                        playersInfoVBox.getChildren().add(label);
                    }
                }
        );
    }

    private String convertCardIDToFileName(String cardID) {
        try {
            List<String> words = Arrays.stream(cardID.split("_")).toList();
            StringBuilder builder = new StringBuilder();

            // remove last word and merge everything with '_'
            for (int i = 0; i < words.size() - 1; i++) {
                String convertedWord;
                switch (words.get(i)) {
                    case "TWO" -> convertedWord = "2";
                    case "THREE" -> convertedWord = "3";
                    case "FOUR" -> convertedWord = "4";
                    case "FIVE" -> convertedWord = "5";
                    case "SIX" -> convertedWord = "6";
                    case "SEVEN" -> convertedWord = "7";
                    case "EIGHT" -> convertedWord = "8";
                    case "NINE" -> convertedWord = "9";
                    case "TEN" -> convertedWord = "10";
                    default -> convertedWord = words.get(i);
                }
                builder.append(convertedWord);

                if (i != words.size() - 2) {
                    builder.append("_");
                }
            }
            return builder.toString();
        } catch (NullPointerException exception) {
            return null;
        }
    }

    private void pickCardManagement(String relatedCardID, ImageView relatedImageView) {
        Platform.runLater(
                () -> {
                    disableAllMatchRelatedButtons();

                    Data.getInstance().setPlayerAlreadyPickedACard(true);
                    drawFromDeckButton.setDisable(true);
                    pickFromDiscardedCardsButton.setDisable(true);
                    cancelTurnButton.setDisable(false);
                    relatedImageView.setDisable(true);
                    relatedImageView.setVisible(false);
                    addCardToHand(relatedCardID, generateImageView(relatedCardID));

                    enableValidMatchRelatedButtons();
                }
        );
    }

    private void showNotification(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("normal-text");
        PopupCreator.show("Avviso", 400, 200, label);
    }

    @FXML
    public void onDrawFromDeckButtonClick() {
        pickCardManagement(Data.getInstance().getDeckTopCardID(), deckImageView);
    }

    @FXML
    public void onPickFromDiscardedCardsButtonClick() {
        pickCardManagement(Data.getInstance().getVisibleDiscardedCardID(), discardedCardsImageView);
    }

    @FXML
    public void onOpeningButtonClick() {}

    @FXML
    public void onAddGroupButtonClick() {
        Platform.runLater(
                () -> {
                    disableAllMatchRelatedButtons();

                    List<String> addedGroup = new ArrayList<>();
                    addedGroup.addAll(selectedCardIDsInHand);
                    groupsAddedForOpening.add(addedGroup);

                    List<String> selectedCardIDsInHandCopy = new ArrayList<>(selectedCardIDsInHand);
                    for (String cardID : selectedCardIDsInHandCopy) {
                        ImageView relatedImageView = handCardIDImageViewMap.get(cardID);
                        onHandCardImageViewClick(relatedImageView); // cancel selection
                        applyDarkEffect(relatedImageView);
                        relatedImageView.setDisable(true);
                    }

                    enableValidMatchRelatedButtons();
                }
        );
    }

    @FXML
    public void onResetAddedGroupsButtonClick() {
        Platform.runLater(
                () -> {
                    disableAllMatchRelatedButtons();

                    for (List<String> addedGroup : groupsAddedForOpening) {
                        for (String cardID : addedGroup) {
                            ImageView relatedImageView = handCardIDImageViewMap.get(cardID);
                            relatedImageView.setEffect(null);
                            relatedImageView.setDisable(false);
                        }
                    }
                    groupsAddedForOpening.clear();

                    enableValidMatchRelatedButtons();
                }
        );
    }

    @FXML
    public void onViewAddedGroupsButtonClick() {
        Platform.runLater(
                () -> {
                    VBox containerVBox = new VBox();
                    containerVBox.setFillWidth(true);
                    containerVBox.setSpacing(10);
                    containerVBox.setAlignment(Pos.TOP_LEFT);

                    Text title = new Text("Giochi aggiunti per l'apertura");
                    title.getStyleClass().add("scene-title");
                    containerVBox.getChildren().add(title);

                    ScrollPane containerScrollPane = new ScrollPane();
                    containerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                    containerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                    VBox groupsVBox = new VBox();
                    groupsVBox.setAlignment(Pos.TOP_LEFT);
                    containerScrollPane.setContent(groupsVBox);
                    containerVBox.getChildren().add(containerScrollPane);

                    for (List<String> group : groupsAddedForOpening) {
                        ScrollPane groupScrollPane = generateCardsScrollPane();
                        HBox innerGroupHBox = (HBox) groupScrollPane.getContent();
                        groupScrollPane.setContent(innerGroupHBox);
                        for (String cardID : group) {
                            innerGroupHBox.getChildren().add(generateImageView(cardID));
                        }
                        containerVBox.getChildren().add(groupScrollPane);
                    }

                    PopupCreator.show(
                            "Anteprima apertura",
                            1280,
                            720,
                            containerVBox
                    );
                }
        );
    }

    @FXML
    public void onPlaceGroupButtonClick() {}

    @FXML
    public void onAttachGroupButtonClick() {}

    @FXML
    public void onAttachCardButtonClick() {}

    @FXML
    public void onReplaceJollyButtonClick() {}

    @FXML
    public void onDiscardCardButtonClick() {}

    @FXML
    public void onCancelTurnButtonClick() {}

    @FXML
    public void onExitButtonClick() {
        Client.getInstance().stop();
        SceneLoader.changeScene("menu.fxml");
    }
}
