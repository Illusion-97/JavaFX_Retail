package dwn.jfx.retail;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RetailApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RetailApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Retail Game");
        stage.setScene(scene);
        setIcon(stage);
        setPosition(stage);
        onClose(stage);
        stage.show();
    }

    private static void onClose(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> Platform.exit());
    }

    private static void setPosition(Stage stage) {
        stage.setX(Screen.getPrimary().getOutputScaleX());
        stage.setY(Screen.getPrimary().getOutputScaleY());
    }

    private void setIcon(Stage stage) {
        // getClass().getResource("icon/livraison-rapide.png"); Get Resource from resources folder
        Image icon = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("icon/livraison-rapide.png"))
        );
        stage.getIcons().add(icon);
    }
}