package Minesweeper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * This program represents the game "Minesweeper"
 * @author  Lukas Freudenmann
 * @version 2.1
 */

public class Minesweeper extends Application {
    private static Stage window;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        Label madeBy = new Label("Made by Lukas Freudenmann");
        Label version = new Label("Version 2.1");
        Label space = new Label("");
        Button button = new Button("Start Game");
        button.setOnAction(e -> new GameLayout(window));
        //start - layout
        VBox startLayout = new VBox();
        startLayout.getChildren().addAll(button, space, madeBy, version);
        startLayout.setAlignment(Pos.CENTER);
        Scene startGame = new Scene(startLayout, 250, 200);
        window.setScene(startGame);
        window.setTitle("Minesweeper");
        window.getIcons().add(new Image (getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/logo.png")));
        window.show();

    }
}