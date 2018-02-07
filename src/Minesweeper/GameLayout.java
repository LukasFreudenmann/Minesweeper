package Minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class GameLayout {
    private Stage window;
    private GameEngine gameEngine;
    private Button[][] buttons;
    private BorderPane layout;
    private Label timer = new Label();
    private Label bombsLabel = new Label();
    private int seconds = 0;
    private int minutes = 0;
    private int height ;    // only for settings
    private int width;      // only for settings
    private boolean gameOver = true;
    private static final double sizeOfButton = 29;
    private static final int maxHeight = 24;
    private static final int maxWidth = 30;
    private static final double maxBombs = 0.93; // in percent

    // constructor, sets stage
    public GameLayout(Stage stage) {
        this.window = stage;
        window.setTitle("Minesweeper");
        window.getIcons().add(new Image (getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/logo.png")));
        window.setOnCloseRequest(e -> System.exit(0));
        timer();
        this.gameEngine = new GameEngine(this);
        newGame();
        window.show();
    }

    // starts new Game
    public void newGame() {
        gameOver = true;
        gameEngine.startGame();
        resetTimer();
        // generate as many buttons as the game board is big
        buttons = new Button[gameEngine.getHeight()][gameEngine.getWidth()];
        for (int i = 0; i < gameEngine.getHeight(); i++) {
            for (int j = 0; j < gameEngine.getWidth(); j++) {
                buttons[i][j] = new Button();

                // set size of buttons
                buttons[i][j].setMaxHeight(sizeOfButton);
                buttons[i][j].setMinHeight(sizeOfButton);
                buttons[i][j].setMaxWidth(sizeOfButton);
                buttons[i][j].setMinWidth(sizeOfButton);
            }
        }
        layout = new BorderPane();
        layout.setTop(topLayout());
        updateScene();
        Scene runGame = new Scene(layout);
        window.setScene(runGame);
        checkClick();
    }

    // updates thr game - grid
    public void updateScene() {
        setLabel();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 25, 20, 25));
        grid.setAlignment(Pos.CENTER);
        // create button array
        for (int i = 0; i < gameEngine.getHeight(); i++) {
            for (int j = 0; j < gameEngine.getWidth(); j++) {
                GridPane.setConstraints(buttons[i][j], j, i + 1);
                grid.getChildren().add(buttons[i][j]);
            }
        }
        layout.setCenter(grid);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getTime() {
        DecimalFormat df = new DecimalFormat("00");
        return " " + df.format(minutes) + ":" + df.format(seconds) + " ";
    }

    // setup display for marked - bombs
    public void bombDisplay() {
        bombsLabel.setText(String.valueOf(gameEngine.getMarked()) + " / " + String.valueOf(gameEngine.getBombs()));
    }

    private void checkClick() {
        for (int i = 0; i < gameEngine.getHeight(); i++) {
            for (int j = 0; j < gameEngine.getWidth(); j++) {
                setClick(i, j);
            }
        }
    }

    // set click - event
    private void setClick(int row,int column) {
        buttons[row][column].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                    gameOver = false;
                    gameEngine.reveal(row, column);
                    setLabel();
                }else if(button==MouseButton.SECONDARY) {
                    gameOver = false;
                    gameEngine.mark(row, column);
                    setLabel();

                }
            }
        });
    }

    // set character to every field
    private void setLabel() {
        // TODO set right for Loose
        for (int i = 0; i < gameEngine.getHeight(); i++) {
            for (int j = 0; j < gameEngine.getWidth(); j++) {
                if (gameEngine.getGameCell(i, j).isRevealed()) {
                    if (gameEngine.getGameCell(i, j).isBomb()) {
                        Image imageBomb = new Image(getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/bomb.png"));
                        buttons[i][j].setGraphic(new ImageView((imageBomb)));
                        buttons[i][j].setPadding(Insets.EMPTY);
                    } else {
                        buttons[i][j].setText(String.valueOf(gameEngine.getGameCell(i, j).getNearby()));
                    }
                } else if (gameEngine.getGameCell(i, j).isMarked() && !gameEngine.getGameCell(i, j).isRevealed()) {
                    // marked / Flag
                    Image imageFlag = new Image(getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/flag.png"));
                    buttons[i][j].setGraphic(new ImageView(imageFlag));
                    buttons[i][j].setPadding(Insets.EMPTY);
                } else {
                    buttons[i][j].setGraphic(null);
                    buttons[i][j].setText("");
                }
            }
        }
    }

    // menuBar settings and restart
    private BorderPane topLayout() {

        BorderPane topLayout = new BorderPane();
        topLayout.setPadding(new Insets(10, 10, 10, 10));

        // add restart - button
        Button restart = new Button();
        restart.setTooltip(new Tooltip("Restart the game"));
        Image imageBomb = new Image(getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/restart.png"));
        restart.setGraphic(new ImageView(imageBomb));
        restart.setMinWidth(sizeOfButton);
        restart.setMinHeight(sizeOfButton);
        restart.setMaxHeight(sizeOfButton);
        restart.setMaxWidth(sizeOfButton);
        restart.setOnAction(e -> {
            gameEngine.reset();
            resetTimer();
            updateScene();
            bombDisplay();
        });

        // AmountOfBombs set-up
        bombsLabel.setMinWidth(60.0);
        bombsLabel.setMinHeight(27.0);
        bombsLabel.setMaxHeight(35.0);
        bombsLabel.setAlignment(Pos.CENTER);
        bombsLabel.setStyle("-fx-border-color: red;");
        bombDisplay();

        // timer set-up
        timer.setMinWidth(60.0);
        timer.setMinHeight(27.0);
        timer.setMaxHeight(60.0);
        timer.setMaxWidth(27.0);
        timer.setAlignment(Pos.CENTER);
        timer.setStyle("-fx-border-color: red;");
        timer.setText(String.valueOf("00:00" ));


        // Settings-Menu
        MenuBar menuBar = new MenuBar();
        menuBar.setMaxHeight(27.0);
        menuBar.setMinHeight(27.0);
        menuBar.setMinWidth(45);
        menuBar.setMaxWidth(45);
        menuBar.setStyle("-fx-background-color: dimgray");
        menuBar.setTooltip(new Tooltip("Setup your own game!"));
        // new Menu
        Menu difficulty = new Menu();
        Image imageSettings = new Image(getClass().getClassLoader().getResourceAsStream("Minesweeper/imgs/settings.png"));
        ImageView settingsView = new ImageView(imageSettings);
        settingsView.setTranslateY(-5.0);
        difficulty.hide();
        difficulty.setGraphic(settingsView);
        //creates MenuItems
        MenuItem easy = new MenuItem("easy");
        easy.setOnAction(e -> {
            gameEngine.setDimension(8, 8);
            gameEngine.setAmountOfBombs(10);
            newGame();
        });
        MenuItem medium = new MenuItem("normal");
        medium.setOnAction(e -> {
            gameEngine.setDimension(16, 16);
            gameEngine.setAmountOfBombs(40);
            newGame();
        });
        MenuItem hard = new MenuItem("hard");
        hard.setOnAction(e -> {
            gameEngine.setDimension(16, 30);
            gameEngine.setAmountOfBombs(99);
            newGame();
        });
        MenuItem custom = new MenuItem("custom");
        custom.setOnAction(e -> custom());
        difficulty.getItems().addAll(easy, medium, hard, custom);
        menuBar.getMenus().addAll(difficulty);

        // Menu size
        Pane space = new Pane();
        space.setMinHeight(27);
        space.setMinWidth(45);
        space.setMaxHeight(27);
        space.setMaxWidth(45);

        HBox center = new HBox();
        center.getChildren().addAll(menuBar, restart, space);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30.0);

        // set TopLayout
        topLayout.setCenter(center);
        topLayout.setRight(timer);
        topLayout.setLeft(bombsLabel);
        topLayout.setStyle("-fx-background-color: dimgray");
        return topLayout;
    }

    // set-up for timer
    private void timer() {
        DecimalFormat df = new DecimalFormat("00");
        Timeline OneSecond = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!gameOver) {
                    timer.setText(String.valueOf(df.format(minutes))+ ":" + String.valueOf(df.format(seconds)));
                    seconds++;
                    if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                    }
                }

            }
        }));
        OneSecond.setCycleCount(Timeline.INDEFINITE);
        OneSecond.play();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                    }
                }, 0, 1000);
    }

    // reset timer
    private void resetTimer() {
        this.seconds = 0;
        this.minutes = 0;
    }

    // set game-customizer
    private void custom() {
        height = gameEngine.getHeight();
        width = gameEngine.getWidth();

        Stage customize = new Stage();
        customize.initModality(Modality.APPLICATION_MODAL);
        customize.setMinWidth(350);
        customize.setMinHeight(300);

        Button secret = new Button();
        secret.setVisible(false);
        // TODO secret
        secret.setOnAction(e -> System.out.println("SECRET!!!"));

        Label bombs = new Label("Set bombs: ");
        Label infoBomb = new Label();
        infoBomb.setText(gameEngine.getBombs() + " ["
                + (int) (((double) gameEngine.getBombs() / (height * width)) * 100) + "%]");
        Slider setBombs = new Slider();
        setBombs.setMax((int) ((height * width) * maxBombs));
        setBombs.setMin(1);
        setBombs.setValue(gameEngine.getBombs());
        setBombs.setShowTickMarks(true);
        setBombs.setShowTickMarks(true);
        setBombs.setBlockIncrement(1); // set abstand
        setBombs.setMinorTickCount(5);
        setBombs.setMajorTickUnit(10);
        setBombs.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                infoBomb.setText(newValue.intValue() + " ["
                        + (int) (((double) newValue.intValue() / (height * width)) * 100) + "%]");
                infoBomb.setTextFill(Color.RED);
                if (gameEngine.getBombs() == newValue.intValue()) {
                    infoBomb.setTextFill(Color.BLACK);
                }
            }
        });

        TextField setHeight = new TextField();
        setHeight.setPromptText("Height");
        setHeight.setOnKeyReleased(e -> {
            try {
                int temp = Integer.parseInt(setHeight.getText());
                if (temp > 1 && temp <= maxHeight) {
                    this.height = temp;
                    setHeight.setStyle("-fx-text-fill: black");
                    setBombs.setMax((height * width) * maxBombs);

                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException b) {
                setHeight.setStyle("-fx-text-fill: red");
            }
        });
        Label height1 = new Label("Set height:  ");

        TextField setWidth = new TextField();
        setWidth.setPromptText("Width");
        setWidth.setOnKeyReleased(e -> {
            try {
                int temp = Integer.parseInt(setWidth.getText());
                if (temp > 1 && temp <= maxWidth) {
                    this.width = temp;
                    setWidth.setStyle("-fx-text-fill: black");
                    setBombs.setMax((height * width) * maxBombs);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException b) {
                setWidth.setStyle("-fx-text-fill: red");
            }
        });
        Label width1 = new Label("Set width:  ");

        HBox buttons = new HBox();
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            try {
                int tempHeight = Integer.parseInt(setHeight.getText());
                int tempWidth = Integer.parseInt(setWidth.getText());
                if(tempHeight > 1 && tempHeight <= maxHeight && tempWidth > 1 && tempWidth <= maxWidth) {
                    gameEngine.setDimension(height, width);
                    gameEngine.setAmountOfBombs((int) setBombs.getValue());
                    customize.close();
                    newGame();
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException a) {
                //TODO Fehler meldung
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> customize.close());
        buttons.getChildren().setAll(submit, cancel);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        Label gab = new Label();

        // set-up grid
        GridPane grid = new GridPane();
        GridPane.setConstraints(height1, 0,0);
        GridPane.setConstraints(setHeight, 1, 0);
        GridPane.setConstraints(width1, 0, 1);
        GridPane.setConstraints(setWidth, 1, 1);
        GridPane.setConstraints(secret, 0, 2, 2, 1);
        GridPane.setConstraints(bombs, 0,3);
        GridPane.setConstraints(infoBomb, 1,3);
        GridPane.setConstraints(setBombs, 0,4, 2, 1);
        GridPane.setConstraints(gab, 0, 5, 2, 1);
        GridPane.setConstraints(buttons, 0, 6, 2, 1);
        grid.setPadding(new Insets(20,30, 20, 30));
        grid.setVgap(10);
        grid.getChildren().setAll(height1, setHeight, width1, setWidth, secret, bombs, infoBomb, setBombs, gab, buttons);

        Scene customizeScene = new Scene(grid);
        customize.setScene(customizeScene);
        customize.initStyle(StageStyle.UTILITY);
        customize.show();
    }
}