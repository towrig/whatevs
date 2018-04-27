package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane client = new BorderPane();

        HBox topSection = new HBox();
        GridPane map = new GridPane();
        map.setMinHeight(640);
        map.setMinWidth(640);
        BorderPane topRight = new BorderPane();

        GridPane inventory = new GridPane();
        ImageView inventor_1 = new ImageView(new Image("TileSprites/inventorySlot.png"));
        ImageView inventor_2 = new ImageView(new Image("TileSprites/inventorySlot.png"));
        ImageView inventor_3 = new ImageView(new Image("TileSprites/inventorySlot.png"));
        ImageView inventor_4 = new ImageView(new Image("TileSprites/inventorySlot.png"));
        inventory.add(inventor_1,0,0);
        inventory.add(inventor_2,1,0);
        inventory.add(inventor_3,2,0);
        inventory.add(inventor_4,3,0);
        topRight.setBottom(inventory);
        topSection.getChildren().addAll(map, topRight);

        TextArea textArea = new TextArea();
        TextField textField = new TextField();

        BorderPane midLeft = new BorderPane();
        midLeft.setTop(textArea);
        midLeft.setCenter(textField);

        Label name = new Label();
        Label hpBar = new Label();
        Label damage = new Label();
        String picturePath="TileSprites/characterPortrait.png";
        StatLabels info = new StatLabels(name, hpBar, damage, picturePath);
        ImageView character = new ImageView(new Image(picturePath));
        VBox stats = new VBox();
        GridPane statsAndPortrait = new GridPane();
        stats.getChildren().addAll(name, hpBar, damage);
        statsAndPortrait.add(character,0 ,0);
        statsAndPortrait.add(stats,0,0);
        topRight.setCenter(statsAndPortrait);


        GridPane midRight = new GridPane();
        Button left = new Button("LEFT");
        Button right = new Button("RIGHT");
        Button up = new Button("UP");
        Button down = new Button("DOWN");
        Button stop = new Button("STOP");
        Buttons buttons = new Buttons(up, down, left, right, stop);
        midRight.add(up, 1, 0);
        midRight.add(left, 0, 1);
        midRight.add(stop, 1, 1);
        midRight.add(right, 2, 1);
        midRight.add(down, 1, 2);

        HBox middleSection = new HBox();
        middleSection.getChildren().addAll(midLeft, midRight);

        client.setTop(topSection);
        client.setCenter(middleSection);
        primaryStage.setTitle("Forest battle arena");
        primaryStage.setScene(new Scene(client));

        //insert all needed IO/other Threads
        ServerCommunicator comm;
        Thread ioThread;
        try {
            comm = new ServerCommunicator(map, textArea, textField, buttons, info);
            ioThread = new Thread(comm);
            ioThread.start();
        } catch (ConnectException e) {
            System.err.println("Server is not running!\nExiting game...");
            Platform.exit();
            return; // comm.close() viskab näkku muidu
        }

        primaryStage.setOnCloseRequest(event -> {
            try {
                comm.close();
                comm.stopRunning();
                ioThread.interrupt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Platform.setImplicitExit(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
