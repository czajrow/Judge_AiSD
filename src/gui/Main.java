package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Judge_AiSD");
        primaryStage.setScene(new Scene(root, 1400, 700));
        primaryStage.show();
    }

    @Override
    public void stop(){
        Platform.exit();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
