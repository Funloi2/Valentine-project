package com.example.valentineproject;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Random;

public class Testing extends Application {

    // Configuration Constants
    private static final double WINDOW_WIDTH = 450;
    private static final double WINDOW_HEIGHT = 400;
    private static final String BACKGROUND_COLOR = "#f7beff";
    private static final String BUTTON_STYLE_YES = "-fx-background-color: #ff4d6d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20;";
    private static final String BUTTON_STYLE_NO = "-fx-background-color: #ffffff; -fx-text-fill: #ff4d6d; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-color: #ff4d6d; -fx-border-radius: 20;";

    private int noClickCount = 1;
    private final Random random = new Random();

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // 1. Header Section
        Label sentence = new Label("Will you be my Valentine?");
        sentence.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #c9184a;");

        // Centering the label manually in a Pane
        sentence.layoutXProperty().bind(root.widthProperty().subtract(sentence.widthProperty()).divide(2));
        sentence.setLayoutY(50);

        // 2. GIF Container (Using StackPane for easy centering)
        StackPane gifContainer = new StackPane();
        gifContainer.setPrefSize(300, 200);
        gifContainer.layoutXProperty().bind(root.widthProperty().subtract(gifContainer.widthProperty()).divide(2));
        gifContainer.setLayoutY(80);

        // 3. Buttons
        Button yesBtn = createStyledButton("Yes", BUTTON_STYLE_YES);
        Button noBtn = createStyledButton("No", BUTTON_STYLE_NO);

        // Initial positions
        yesBtn.setLayoutX(140);
        yesBtn.setLayoutY(300);
        noBtn.setLayoutX(250);
        noBtn.setLayoutY(300);

        // 4. Logic Setup
        setupNoButtonLogic(noBtn, yesBtn, gifContainer);
        setupYesButtonLogic(yesBtn, noBtn, sentence, gifContainer, root);

        root.getChildren().addAll(gifContainer, sentence, yesBtn, noBtn);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("A Special Message ❤️");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createStyledButton(String text, String style) {
        Button btn = new Button(text);
        btn.setStyle(style);
        btn.setPrefSize(80, 40);
        return btn;
    }

    private void setupNoButtonLogic(Button noBtn, Button yesBtn, Pane gifContainer) {
        noBtn.setOnMouseEntered(e -> {
//             Strategic jumping: keep it away from the Yes button
            double newX = 50 + random.nextDouble() * (WINDOW_WIDTH - 150);
            double newY = 100 + random.nextDouble() * (WINDOW_HEIGHT - 200);

            noBtn.setLayoutX(newX);
            noBtn.setLayoutY(newY);

            // Smooth growth for Yes Button
            double targetScale = yesBtn.getScaleX() + 0.2;
            animateScale(yesBtn, targetScale);
        });

        noBtn.setOnAction(e -> {
            showTemporaryGif(gifContainer, "/com/example/valentineproject/noGif/no-" + noClickCount + ".gif");
            noClickCount = (noClickCount % 3) + 1;
        });
    }

    private void setupYesButtonLogic(Button yesBtn, Button noBtn, Label label, Pane gifContainer, Pane root) {
        yesBtn.setOnAction(e -> {
            root.getChildren().removeAll(yesBtn, noBtn);
            label.setText("YAY! See you soon! ❤️");
            label.setStyle("-fx-font-size: 28px; -fx-text-fill: #ff4d6d; -fx-font-weight: bold;");

            showPermanentGif(gifContainer, "/com/example/valentineproject/yesGif/yes.gif");
        });
    }

    private void animateScale(Button btn, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setToX(scale);
        st.setToY(scale);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }

    private void showTemporaryGif(Pane container, String path) {
        ImageView gview = loadGif(path);
        if (gview == null) return;

        container.getChildren().setAll(gview);
        container.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> container.setVisible(false));
        pause.play();
    }

    private void showPermanentGif(Pane container, String path) {
        ImageView gview = loadGif(path);
        if (gview == null) return;
        container.getChildren().setAll(gview);
        container.setVisible(true);
    }

    private ImageView loadGif(String path) {
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
            ImageView view = new ImageView(img);
            view.setFitWidth(250);
            view.setPreserveRatio(true);
            return view;
        } catch (Exception e) {
            System.err.println("Could not load image: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}