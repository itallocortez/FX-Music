package br.com.fxmusic;

import br.com.fxmusic.dao.DiretorioDao;
import br.com.fxmusic.dao.UsuarioDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private static UsuarioDao uDao;

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("visao/TelaPrincipal.fxml"));
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(getClass().getResource("../../../application/TelaPrincipal.css").toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("FX Music");
		stage.setResizable(false);
		stage.show();
		
		//uDao = UsuarioDao.getInstance();
		//uDao.addUsuario("sItallo", "itallo.cortez", "123");
		//uDao.addUsuario("Samuel", "samuel.vta", "123");
		
		//uDao.carregarUsuarios();
		//uDao.listarUsuarios();
		
		//uDao.salvarUsuarios();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}