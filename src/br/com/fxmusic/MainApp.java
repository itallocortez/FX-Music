package br.com.fxmusic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/br/com/fxmusic/visao/TelaPrincipal.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/TelaPrincipal.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("FX Music");
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> encerrarAplicativo());

        stage.show();
    }

    private void encerrarAplicativo() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
