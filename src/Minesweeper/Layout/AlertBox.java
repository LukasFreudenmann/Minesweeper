package Minesweeper.Layout;

import Minesweeper.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.stage.StageStyle;


public class AlertBox {

    public static void win(GameLayout currentLayout) {
        Stage windowWIN = new Stage();
        windowWIN.initModality(Modality.APPLICATION_MODAL);
        windowWIN.setMinWidth(500);
        windowWIN.setMinHeight(300);

        Label label = new Label();
        label.setText("YOU WIN!!!");
        Label time = new Label("Your time: " + currentLayout.getTime());
        time.setStyle("-fx-border-color: red");

        // startNew- and close-Button
        Button newGameButton = new Button("Start new Game");
        newGameButton.setOnAction(e -> {
            windowWIN.close();
            currentLayout.newGame();
        });
        Button closeButton = new Button("Close Window");
        closeButton.setOnAction(e -> System.exit(0));

        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(newGameButton, closeButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, time, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        windowWIN.setScene(scene);
        windowWIN.initStyle(StageStyle.UTILITY);
        windowWIN.show();

    }

    public static void loose(GameLayout currentLayout) {
        Stage windowLoose = new Stage();

        windowLoose.initModality(Modality.APPLICATION_MODAL);
        // window.setTitle(title);
        windowLoose.setMinWidth(300);
        windowLoose.setMinHeight(200);

        Label label = new Label();
        label.setText("YOU LOOSE!!!");
        Button newGameButton = new Button("Start new Game");
        newGameButton.setOnAction(e -> {
            windowLoose.close();
            currentLayout.newGame();
        });

        Button closeButton = new Button("Close Window");
        closeButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, newGameButton, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        windowLoose.setScene(scene);
        windowLoose.initStyle(StageStyle.UTILITY);
        windowLoose.show();

    }
}