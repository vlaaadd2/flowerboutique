package org.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.dao.DatabaseManager;
import org.example.models.*;
import org.example.services.BouquetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);


    private DatabaseManager dbManager;
    private Bouquet currentBouquet;
    private BouquetService bouquetService;


    private Pane    visualBouquetPane;
    private Label   totalPriceLabel;
    private Label   bouquetCountLabel;
    private Button  removeButton;
    private ListView<Flower> assortmentListView;
    private ListView<Flower> bouquetListView;


    private final Map<Flower, ImageView> visualMap = new HashMap<>();
    private double      mouseAnchorX, mouseAnchorY;
    private ImageView   selectedImageView = null;
    private Flower      selectedFlowerData = null;


    private final List<AccessoryEntry> accessoryEntries = new ArrayList<>();
    private ImageView selectedAccImageView = null;
    private Accessory selectedAccData = null;


    private static class AccessoryEntry {
        final Accessory accessory;
        final ImageView imageView;
        AccessoryEntry(Accessory accessory, ImageView imageView) {
            this.accessory = accessory;
            this.imageView = imageView;
        }
    }


    private static final String COLOR_BG        = "#0D0D0D";
    private static final String COLOR_PANEL     = "#161616";
    private static final String COLOR_CARD      = "#1C1C1C";
    private static final String COLOR_BORDER    = "#2A2A2A";
    private static final String COLOR_ACCENT    = "#C8963E";
    private static final String COLOR_ACCENT2   = "#8B5E3C";
    private static final String COLOR_TEXT      = "#F0EAD6";
    private static final String COLOR_MUTED     = "#7A7066";
    private static final String COLOR_DANGER    = "#C0392B";
    private static final String COLOR_SUCCESS   = "#27AE60";


    private static final List<Accessory> ACCESSORY_CATALOGUE = List.of(
            new Accessory("🎀 Атласна стрічка",      35.0),
            new Accessory("📦 Подарункова упаковка", 60.0),
            new Accessory("💌 Листівка",             25.0)
    );


    private static final List<String> ACCESSORY_IMAGES = List.of(
            "ribbon.png",
            "gift_box.png",
            "postcard.png"
    );

    @Override
    public void init() {

        logger.info("Ініціалізація додатку.");
        dbManager     = new DatabaseManager();
        currentBouquet = new Bouquet();
        bouquetService = new BouquetService(dbManager);

        if (dbManager.getAllFlowers().isEmpty()) {
            logger.info("БД порожня — завантажуємо стартовий асортимент.");
            seedDatabase();
        }

    }

    private void seedDatabase() {

        dbManager.addFlowerToDB(new Rose(150.0, 60.0, 10, "rose.png",     true,  10));
        dbManager.addFlowerToDB(new Rose(120.0, 35.0,  8, "rose.png",     false, 16));
        dbManager.addFlowerToDB(new Tulip(80.0, 45.0,  9, "tulip.png",    20));
        dbManager.addFlowerToDB(new Tulip(70.0, 40.0,  6, "tulip.png",    14));
        dbManager.addFlowerToDB(new Lily(200.0, 70.0,  9, "lily.png",     true,  8));
        dbManager.addFlowerToDB(new Lily(180.0, 65.0,  7, "lily.png",     false, 12));
        dbManager.addFlowerToDB(new Chrysanthemum(90.0,  50.0, 8, "chrysanthemum.png", "Біла",   24));
        dbManager.addFlowerToDB(new Chrysanthemum(95.0,  55.0, 7, "chrysanthemum.png", "Жовта",  18));
        dbManager.addFlowerToDB(new Orchid(350.0, 80.0, 10, "orchid.png",  5, 6));
        dbManager.addFlowerToDB(new Orchid(300.0, 75.0,  8, "orchid.png",  3, 10));
        dbManager.addFlowerToDB(new Sunflower(60.0, 90.0, 9, "sunflower.png", 15.0, 30));
        dbManager.addFlowerToDB(new Sunflower(55.0, 85.0, 7, "sunflower.png", 12.0, 20));
    }

    @Override
    public void start(Stage primaryStage) {

        logger.info("Побудова головного вікна.");
        primaryStage.setTitle("🌸 Flower Boutique");

        HBox root = new HBox(0);
        root.setStyle("-fx-background-color: " + COLOR_BG + ";");

        VBox sidebar = buildSidebar(primaryStage);

        StackPane canvasWrapper = buildCanvas();
        HBox.setHgrow(canvasWrapper, Priority.ALWAYS);
        root.getChildren().addAll(sidebar, canvasWrapper);

        Scene scene = new Scene(root, 1200, 780);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        logger.info("Головне вікно відображено.");
    }



    private VBox buildSidebar(Stage owner) {

        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(360);
        sidebar.setMinWidth(320);
        sidebar.setStyle("-fx-background-color: " + COLOR_PANEL + ";"
                + "-fx-border-color: " + COLOR_BORDER + ";"
                + "-fx-border-width: 0 1 0 0;");


        VBox header = new VBox(4);
        header.setPadding(new Insets(28, 24, 20, 24));
        header.setStyle("-fx-background-color: " + COLOR_CARD + ";"
                + "-fx-border-color: " + COLOR_BORDER + ";"
                + "-fx-border-width: 0 0 1 0;");

        Label brandEmoji = new Label("🌸");
        brandEmoji.setStyle("-fx-font-size: 28px;");

        Label brandName = new Label("FLOWER BOUTIQUE");
        brandName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-text-fill: " + COLOR_ACCENT + "; "
                + "-fx-letter-spacing: 3px;");

        Label brandSub = new Label("Флористичний магазин");
        brandSub.setStyle("-fx-font-size: 11px; -fx-text-fill: " + COLOR_MUTED + ";");

        header.getChildren().addAll(brandEmoji, brandName, brandSub);


        VBox assortmentSection = new VBox(10);
        assortmentSection.setPadding(new Insets(20, 16, 12, 16));

        Label assortLabel = buildSectionLabel("АСОРТИМЕНТ");

        assortmentListView = new ListView<>();
        assortmentListView.getItems().addAll(dbManager.getAllFlowers());
        assortmentListView.setPrefHeight(200);
        assortmentListView.getStyleClass().add("boutique-list");
        assortmentListView.setCellFactory(lv -> new FlowerListCell());

        Button addBtn = buildPrimaryButton("➕  Додати до букета", COLOR_ACCENT);

        assortmentSection.getChildren().addAll(assortLabel, assortmentListView, addBtn);


        VBox bouquetSection = new VBox(10);
        bouquetSection.setPadding(new Insets(4, 16, 12, 16));

        HBox bouquetHeader = new HBox(8);
        bouquetHeader.setAlignment(Pos.CENTER_LEFT);
        Label bouquetLabel2 = buildSectionLabel("У БУКЕТІ");

        bouquetCountLabel = new Label("0 шт.");
        bouquetCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + COLOR_MUTED + ";"
                + "-fx-padding: 3 8 3 8; -fx-background-radius: 10;"
                + "-fx-background-color: " + COLOR_BORDER + ";");

        bouquetHeader.getChildren().addAll(bouquetLabel2, bouquetCountLabel);

        bouquetListView = new ListView<>();
        bouquetListView.setPrefHeight(160);
        bouquetListView.getStyleClass().add("boutique-list");
        bouquetListView.setCellFactory(lv -> new FlowerListCell());

        Button sortBtn   = buildPrimaryButton("🔃  Сортувати за свіжістю", COLOR_ACCENT2);
        Button searchBtn = buildPrimaryButton("🔍  Пошук за довжиною стебла", COLOR_SUCCESS);
        removeButton     = buildPrimaryButton("🗑  Видалити вибраний елемент", COLOR_DANGER);
        removeButton.setVisible(false);

        bouquetSection.getChildren().addAll(bouquetHeader, bouquetListView,
                sortBtn, searchBtn, removeButton);



        VBox priceBox = new VBox(6);
        priceBox.setPadding(new Insets(16, 16, 24, 16));
        priceBox.setStyle("-fx-border-color: " + COLOR_BORDER + ";"
                + "-fx-border-width: 1 0 0 0;");

        Label priceCaption = new Label("ЗАГАЛЬНА ВАРТІСТЬ БУКЕТА");
        priceCaption.setStyle("-fx-font-size: 10px; -fx-text-fill: " + COLOR_MUTED + ";"
                + "-fx-letter-spacing: 1px;");

        totalPriceLabel = new Label("0,00 грн");
        totalPriceLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;"
                + "-fx-text-fill: " + COLOR_ACCENT + ";");

        priceBox.getChildren().addAll(priceCaption, totalPriceLabel);

        VBox.setVgrow(bouquetSection, Priority.ALWAYS);
        sidebar.getChildren().addAll(header, assortmentSection, bouquetSection, priceBox);


        addBtn.setOnAction(e -> {

            Flower sel = assortmentListView.getSelectionModel().getSelectedItem();

            //logger.error("КРИТИЧНА ПОМИЛКА: Збій системи при спробі обробити квітку '{}'!", sel.getName());


            if (sel == null) {
                showAlert("Оберіть квітку зі списку асортименту.");
                return;
            }

            if (!sel.isAvailable()) {
                showAlert("Квітка «" + sel.getName() + "» закінчилась на складі!");
                return;
            }

            addFlowerToCanvas(sel);

        });

        sortBtn.setOnAction(e -> applySort());
        searchBtn.setOnAction(e -> showSearchDialog(owner));
        removeButton.setOnAction(e -> removeSelected());

        return sidebar;
    }



    private StackPane buildCanvas() {

        visualBouquetPane = new Pane();
        visualBouquetPane.setStyle("-fx-background-color: transparent;");

        VBox hint = new VBox(8);
        hint.setAlignment(Pos.CENTER);
        Label hintEmoji = new Label("💐");
        hintEmoji.setStyle("-fx-font-size: 52px; -fx-opacity: 0.18;");
        Label hintText = new Label("Перетягуйте квіти\nщоб скласти букет");
        hintText.setStyle("-fx-font-size: 15px; -fx-text-fill: #3A3A3A;"
                + "-fx-text-alignment: center; -fx-alignment: center;");

        hint.getChildren().addAll(hintEmoji, hintText);
        hint.setMouseTransparent(true);


        VBox accPanel = buildAccessoryPanel();
        StackPane.setAlignment(accPanel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(accPanel, new Insets(0, 20, 800, 0));

        StackPane stack = new StackPane(hint, visualBouquetPane, accPanel);
        stack.setStyle("-fx-background-color: " + COLOR_BG + ";");
        stack.setPadding(new Insets(20));

        return stack;
    }

    private VBox buildAccessoryPanel() {

        VBox panel = new VBox(4);
        panel.setPadding(new Insets(6, 12, 6, 12));
        panel.setMaxWidth(300);

        panel.setStyle(
                "-fx-background-color: #1C1C1C;"
                        + "-fx-border-color: #2A2A2A;"
                        + "-fx-border-width: 1;"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");

        DropShadow shadow = new DropShadow(24, Color.BLACK);
        shadow.setOffsetY(4);
        panel.setEffect(shadow);

        Label title = new Label("🎁  АКСЕСУАРИ");
        title.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;"
                + "-fx-text-fill: " + COLOR_MUTED + "; -fx-letter-spacing: 2px;");
        panel.getChildren().add(title);

        for (int i = 0; i < ACCESSORY_CATALOGUE.size(); i++) {

            Accessory acc = ACCESSORY_CATALOGUE.get(i);
            String imagePath = ACCESSORY_IMAGES.get(i);

            Button btn = new Button(acc.getName() + "  —  " +
                    String.format("%.0f грн", acc.getPrice()));
            btn.setMaxWidth(Double.MAX_VALUE);

            String baseStyle =
                    "-fx-background-color: #242424;"
                            + "-fx-text-fill: " + COLOR_TEXT + ";"
                            + "-fx-font-size: 11px;"
                            + "-fx-background-radius: 7;"
                            + "-fx-padding: 6 10;"
                            + "-fx-cursor: hand;"
                            + "-fx-border-color: " + COLOR_BORDER + ";"
                            + "-fx-border-radius: 7;"
                            + "-fx-border-width: 1;";

            btn.setStyle(baseStyle);

            btn.setOnMouseEntered(e -> btn.setStyle(baseStyle
                    .replace("-fx-background-color: #242424;",
                            "-fx-background-color: #2E2217;")));
            btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

            btn.setOnAction(e -> {
                currentBouquet.addAccessory(acc);
                updateStats();
                logger.info("Аксесуар '{}' додано до букета.", acc.getName());
                addAccessoryToCanvas(acc, imagePath);


                btn.setStyle(baseStyle.replace(
                        "-fx-border-color: " + COLOR_BORDER + ";",
                        "-fx-border-color: " + COLOR_ACCENT + ";"));

                new Thread(() -> {
                    try { Thread.sleep(350); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> btn.setStyle(baseStyle));
                }).start();

            });

            panel.getChildren().add(btn);
        }

        return panel;
    }



    private void addAccessoryToCanvas(Accessory acc, String imagePath) {

        try {

            InputStream is = getClass().getResourceAsStream("/" + imagePath);

            if (is != null) {

                ImageView iv = new ImageView(new Image(is));
                iv.setFitHeight(100);
                iv.setPreserveRatio(true);

                DropShadow shadow = new DropShadow(18, Color.BLACK);
                shadow.setOffsetY(6);
                iv.setEffect(shadow);

                int n = accessoryEntries.size();
                iv.setX(300 + (n % 6) * 55.0);
                iv.setY(40  + (n / 6) * 55.0);

                AccessoryEntry entry = new AccessoryEntry(acc, iv);

                iv.setOnMousePressed(e -> {
                    mouseAnchorX = e.getX();
                    mouseAnchorY = e.getY();
                    selectAccessory(entry);
                });

                iv.setOnMouseDragged(e -> {
                    double paneX = visualBouquetPane.localToScene(0, 0).getX();
                    double paneY = visualBouquetPane.localToScene(0, 0).getY();
                    iv.setX(e.getSceneX() - paneX - mouseAnchorX);
                    iv.setY(e.getSceneY() - paneY - mouseAnchorY);
                });

                accessoryEntries.add(entry);
                visualBouquetPane.getChildren().add(iv);

            } else {
                logger.warn("Зображення аксесуара не знайдено: {}", imagePath);
            }

        } catch (Exception ex) {
            logger.error("Помилка завантаження зображення аксесуара: {}", ex.getMessage(), ex);
        }
    }



    private void addFlowerToCanvas(Flower flower) {

        boolean added = bouquetService.addFlowerToBouquet(currentBouquet, flower);
        if (!added) { showAlert("Квітка недоступна."); return; }

        bouquetListView.getItems().add(flower);
        assortmentListView.refresh();
        updateStats();

        try {
            InputStream is = getClass().getResourceAsStream("/" + flower.getImagePath());
            if (is != null) {
                ImageView iv = new ImageView(new Image(is));
                iv.setFitHeight(170);
                iv.setPreserveRatio(true);

                DropShadow shadow = new DropShadow(18, Color.BLACK);
                shadow.setOffsetY(6);
                iv.setEffect(shadow);

                int n = visualMap.size();
                iv.setX(40 + (n % 8) * 50.0);
                iv.setY(40 + (n / 8) * 60.0);

                iv.setOnMousePressed(e -> {
                    mouseAnchorX = e.getX();
                    mouseAnchorY = e.getY();
                    selectFlower(iv, flower);
                });
                iv.setOnMouseDragged(e -> {
                    double paneX = visualBouquetPane.localToScene(0, 0).getX();
                    double paneY = visualBouquetPane.localToScene(0, 0).getY();
                    iv.setX(e.getSceneX() - paneX - mouseAnchorX);
                    iv.setY(e.getSceneY() - paneY - mouseAnchorY);
                });

                visualMap.put(flower, iv);
                visualBouquetPane.getChildren().add(iv);
            } else {
                logger.warn("Зображення не знайдено: {}", flower.getImagePath());
            }
        } catch (Exception ex) {
            logger.error("Помилка завантаження зображення: {}", ex.getMessage(), ex);
        }
    }





    private void applySort() {

        bouquetService.sortFlowersByFreshness(currentBouquet);
        bouquetListView.getItems().setAll(currentBouquet.getFlowers());

        double x = 50, y = 50;

        for (Flower f : currentBouquet.getFlowers()) {

            ImageView iv = visualMap.get(f);

            if (iv != null) {

                iv.setX(x);
                iv.setY(y);
                iv.toFront();


                x += 42;
                y += 18; }

        }

    }



    private void removeSelected() {


        if (selectedAccImageView != null && selectedAccData != null) {

            currentBouquet.getAccessories().remove(selectedAccData);
            visualBouquetPane.getChildren().remove(selectedAccImageView);

            accessoryEntries.removeIf(entry -> entry.imageView == selectedAccImageView);
            updateStats();
            clearSelection();

            logger.info("Аксесуар '{}' видалено з букета.", selectedAccData.getName());
            return;

        }


        if (selectedImageView == null || selectedFlowerData == null) {
            return;
        }

        bouquetService.removeFlowerFromBouquet(currentBouquet, selectedFlowerData);
        bouquetListView.getItems().remove(selectedFlowerData);
        visualBouquetPane.getChildren().remove(selectedImageView);
        visualMap.remove(selectedFlowerData);
        assortmentListView.refresh();

        clearSelection();
        updateStats();
    }



    private void showSearchDialog(Stage owner) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Пошук за довжиною стебла");

        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: " + COLOR_PANEL + ";");

        Label title = new Label("🔍  Пошук квітів у букеті");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        Label sub = new Label("Вкажіть діапазон довжини стебла (см):");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: " + COLOR_MUTED + ";");

        HBox inputs = new HBox(12);
        inputs.setAlignment(Pos.CENTER_LEFT);

        TextField minF = buildTextField("Мін, см");
        TextField maxF = buildTextField("Макс, см");
        inputs.getChildren().addAll(styledLabel("Від:"), minF, styledLabel("До:"), maxF);

        ListView<Flower> results = new ListView<>();
        results.setPrefHeight(180);
        results.getStyleClass().add("boutique-list");
        results.setCellFactory(lv -> new FlowerListCell());

        Button findBtn = buildPrimaryButton("Знайти", COLOR_SUCCESS);
        findBtn.setMaxWidth(Double.MAX_VALUE);

        findBtn.setOnAction(e -> {

            try {

                double min = Double.parseDouble(minF.getText());
                double max = Double.parseDouble(maxF.getText());
                List<Flower> found = bouquetService.findFlowersByStemLength(currentBouquet, min, max);
                results.getItems().setAll(found);
                if (found.isEmpty()) {
                    results.setPlaceholder(styledLabel("Нічого не знайдено"));
                }

            } catch (NumberFormatException ex) {
                results.setPlaceholder(styledLabel("⚠ Введіть коректні числа!"));
                logger.warn("Некоректний ввід у діалозі пошуку.");
            }

        });

        root.getChildren().addAll(title, sub, inputs, findBtn, results);

        Scene sc = new Scene(root, 440, 420);
        sc.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialog.setScene(sc);
        dialog.show();
    }


    private void selectFlower(ImageView iv, Flower f) {

        clearSelection();
        selectedImageView  = iv;
        selectedFlowerData = f;

        DropShadow glow = new DropShadow(30, Color.web(COLOR_ACCENT));
        glow.setSpread(0.25);
        selectedImageView.setEffect(glow);
        selectedImageView.toFront();
        removeButton.setVisible(true);
        bouquetListView.getSelectionModel().select(f);

        logger.debug("Вибрана квітка: {}", f.getName());

    }

    private void selectAccessory(AccessoryEntry entry) {

        clearSelection();
        selectedAccImageView = entry.imageView;
        selectedAccData      = entry.accessory;

        DropShadow glow = new DropShadow(30, Color.web(COLOR_ACCENT));
        glow.setSpread(0.25);
        selectedAccImageView.setEffect(glow);
        selectedAccImageView.toFront();
        removeButton.setVisible(true);

        logger.debug("Вибраний аксесуар: {}", entry.accessory.getName());

    }

    private void clearSelection() {

        if (selectedImageView != null) {
            DropShadow shadow = new DropShadow(18, Color.BLACK);
            shadow.setOffsetY(6);
            selectedImageView.setEffect(shadow);
            selectedImageView  = null;
            selectedFlowerData = null;
        }

        if (selectedAccImageView != null) {
            DropShadow shadow = new DropShadow(18, Color.BLACK);
            shadow.setOffsetY(6);
            selectedAccImageView.setEffect(shadow);
            selectedAccImageView = null;
            selectedAccData      = null;
        }

        removeButton.setVisible(false);
    }



    private void updateStats() {
        int count = currentBouquet.getFlowers().size();
        bouquetCountLabel.setText(count + " шт.");
        totalPriceLabel.setText(String.format("%,.2f грн", currentBouquet.getTotalPrice()));
    }





    private Label buildSectionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;"
                + "-fx-text-fill: " + COLOR_MUTED + ";"
                + "-fx-letter-spacing: 2px; -fx-padding: 0 0 4 0;");
        return l;
    }

    private Button buildPrimaryButton(String text, String color) {

        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);

        b.setStyle("-fx-background-color: " + color + ";"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 8;"
                + "-fx-padding: 10 16;"
                + "-fx-cursor: hand;");

        b.setOnMouseEntered(e -> b.setStyle(b.getStyle() + "-fx-opacity: 0.85;"));
        b.setOnMouseExited(e  -> b.setStyle(b.getStyle().replace("-fx-opacity: 0.85;", "")));

        return b;

    }

    private TextField buildTextField(String prompt) {

        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(100);

        tf.setStyle("-fx-background-color: " + COLOR_CARD + ";"
                + "-fx-text-fill: " + COLOR_TEXT + ";"
                + "-fx-prompt-text-fill: " + COLOR_MUTED + ";"
                + "-fx-background-radius: 6;"
                + "-fx-border-color: " + COLOR_BORDER + ";"
                + "-fx-border-radius: 6;"
                + "-fx-padding: 8;");

        return tf;

    }

    private Label styledLabel(String text) {

        Label l = new Label(text);

        l.setStyle("-fx-text-fill: " + COLOR_MUTED + "; -fx-font-size: 12px;");
        return l;

    }

    private void showAlert(String msg) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Увага");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

    }



    private static class FlowerListCell extends ListCell<Flower> {

        private static final Map<String, String> EMOJI = Map.of(
                "Троянда",    "🌹",
                "Тюльпан",   "🌷",
                "Лілія",     "🌸",
                "Хризантема","🌼",
                "Орхідея",   "🌹",
                "Соняшник",  "🌻"
        );

        private final HBox  box    = new HBox(10);
        private final Label icon   = new Label();
        private final VBox  info   = new VBox(2);
        private final Label name   = new Label();
        private final Label detail = new Label();
        private final Label stock  = new Label();



        FlowerListCell() {
            box.setAlignment(Pos.CENTER_LEFT);
            box.setPadding(new Insets(6, 10, 6, 10));
            icon.setStyle("-fx-font-size: 20px;");
            name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #F0EAD6;");
            detail.setStyle("-fx-font-size: 11px; -fx-text-fill: #7A7066;");
            stock.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;"
                    + "-fx-padding: 2 8 2 8; -fx-background-radius: 10;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            info.getChildren().addAll(name, detail);
            box.getChildren().addAll(icon, info, spacer, stock);
            setGraphic(box);
            setText(null);

        }

        @Override
        protected void updateItem(Flower flower, boolean empty) {

            super.updateItem(flower, empty);

            if (empty || flower == null) {
                setGraphic(null);
                return;
            }

            icon.setText(EMOJI.getOrDefault(flower.getName(), "🌺"));
            name.setText(flower.getName() + "  ·  " + String.format("%.0f грн", flower.getPrice()));
            detail.setText(flower.getStemLength() + " см  |  свіжість " + flower.getFreshnessLevel() + "/10");

            int qty = flower.getStockQuantity();

            if (qty > 5) {
                stock.setText(qty + " шт");
                stock.setStyle(stock.getStyle().replaceAll("-fx-background-color:[^;]+;", "")
                        + "-fx-background-color: #1A3A2A; -fx-text-fill: #27AE60;");

            } else if (qty > 0) {
                stock.setText(qty + " шт");
                stock.setStyle(stock.getStyle().replaceAll("-fx-background-color:[^;]+;", "")
                        + "-fx-background-color: #3A2A1A; -fx-text-fill: #C8963E;");

            } else {
                stock.setText("немає");
                stock.setStyle(stock.getStyle().replaceAll("-fx-background-color:[^;]+;", "")
                        + "-fx-background-color: #2A1A1A; -fx-text-fill: #C0392B;");
            }

            setGraphic(box);
            setText(null);
        }
    }
}
