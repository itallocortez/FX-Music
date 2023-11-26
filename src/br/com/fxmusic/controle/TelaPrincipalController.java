package br.com.fxmusic.controle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.fxmusic.dao.DiretorioDao;
import br.com.fxmusic.dao.MusicaDao;
import br.com.fxmusic.modelo.Musica;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class TelaPrincipalController implements Initializable {

	@FXML
    private ListView<Musica> listViewMusicas;
	
    @FXML
    private ListView<String> listViewDiretorios;
    
    @FXML
    private Button btnAddDiretorio;
    
    private static DiretorioDao dDao;
    private static MusicaDao mDao;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dDao = DiretorioDao.getInstance();
		mDao = MusicaDao.getInstance();
		
		// Atualiza as ListViews
		atualizarDiretoriosView();
		atualizarMusicasView();
	}
	
	// Atualiza a ListView de diretórios mostrados para o usuário
	private void atualizarDiretoriosView() {
		dDao.carregarDiretorios();
		
		// Se houverem itens anteriores, os remove para não somar com os novos.
		if(!listViewDiretorios.getItems().isEmpty()) {
			listViewDiretorios.getItems().clear();
		}
		
		listViewDiretorios.getItems().addAll(dDao.getDiretorios());
	}
	
	// Atualiza a ListView de músicas mostradas para o usuário
	private void atualizarMusicasView() {
		mDao.buscarMusicas(dDao.getDiretorios());
		
		// Se houverem itens anteriores, os remove para não somar com os novos.
		if(!listViewMusicas.getItems().isEmpty()) {
			listViewMusicas.getItems().clear();
		}
		
		listViewMusicas.getItems().addAll(mDao.getMusicas());
	}
	
	 @FXML
	 void escolherDiretorio(ActionEvent event) {
		 // Cria um seletor de diretório
	     DirectoryChooser directoryChooser = new DirectoryChooser();
	     directoryChooser.setTitle("Selecione uma pasta");

	     // Obtém a referência ao palco da aplicação
	     Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	     
	     // Exibe o seletor de diretório
	     File selectedDirectory = directoryChooser.showDialog(stage);

	     if (selectedDirectory != null) {
	    	 System.out.println("[DEBUG] Pasta selecionada: " + selectedDirectory.getAbsolutePath());
	    	 
	    	 // Adiciona o diretorio no sistema
	    	 dDao.addDiretorio(selectedDirectory.getAbsolutePath());
	    	 
	    	 // Salva os diretorios no diretorios.txt
	    	 dDao.salvarDiretorios();
	    	 
	    	 // Atualiza a ListView de diretorios para o usuário
	    	 atualizarDiretoriosView();
	    	 
	    	 // Atualiza a de músicas também
	    	 atualizarMusicasView();
	    	 
	    	 // Salva possíveis novas musicas no musicas.txt
	    	 mDao.salvarMusicas();
	     } else {
	    	 System.out.println("[DEBUG] Nenhuma pasta selecionada.");
	     }
	 }

}