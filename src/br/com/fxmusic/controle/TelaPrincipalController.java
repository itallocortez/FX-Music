package br.com.fxmusic.controle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.ikonli.javafx.FontIcon;

import br.com.fxmusic.dao.DiretorioDao;
import br.com.fxmusic.dao.MusicaDao;
import br.com.fxmusic.modelo.Musica;
import br.com.fxmusic.modelo.Playlist;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaPrincipalController implements Initializable {

	private static DiretorioDao dDao;
    private static MusicaDao mDao;
    
    // Text do Cabeçalho da janela
    @FXML
    private Text labelCabecalho;
    
    // TabPane Principal
    @FXML
    private TabPane tabPanePrincipal;
    
	// Tab de Músicas
	@FXML
    private ListView<Musica> listViewMusicas;
    
    // Tab de Diretórios
    @FXML
    private Button btnAddDiretorio;
    
    @FXML
    private ListView<String> listViewDiretorios;
    
    // Player De Música
    
    @FXML
    private Text labelDuracao;

    @FXML
    private Text labelTempoAtual;
    
    @FXML
    private Button btnTocarAnterior;
    
    @FXML
    private Button btnPlayPause;
    
    @FXML
    private FontIcon iconPlayPause;
    
    @FXML
    private Button btnTocarProxima;
    
    @FXML
    private Slider sliderTempo;
    
    @FXML
    private Slider sliderVolume;
    
    @FXML
    private Button btnVolume;
    
    @FXML
    private FontIcon iconVolume;
    
    private MediaPlayer player;
    
    private Playlist playlistAtual;
    
    private Double volume;
    private boolean estaMutado = false;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Instancia as databases
		dDao = DiretorioDao.getInstance();
		mDao = MusicaDao.getInstance();
		
		// Atualiza as ListViews
		atualizarDiretoriosView();
		atualizarMusicasView();
        
		// Adiciona um ouvinte para detectar mudanças de abas
		tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                // Aqui, você pode realizar ações com base na aba selecionada
                String tabText = newTab.getText();
                
                switch (tabText) {
				case "Músicas": {
					labelCabecalho.setText("Minhas Músicas");
					break;
				}
				case "Playlists": {
					labelCabecalho.setText("Minhas Playlists");
					break;
				}
				case "Diretórios": {
					labelCabecalho.setText("Meus Diretórios");
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + tabText);
				}
            }
        });
        
		// Listener para seleção de música na lista de todas as músicas
		listViewMusicas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                System.out.println("[DEBUG] Musica selecionada: " + newValue);
	                
	                // Cria uma playlist com todas as músicas
	                playlistAtual = new Playlist(mDao.getMusicas());
	                
	                // A posição atual será a da música que escolheu
	                playlistAtual.setPosicao(mDao.getMusicas().indexOf(newValue));
	                
	                // Toca a música
	                tocarMusica(playlistAtual.getMusicaAtual().getDiretorio());
	            }
	     });
		 
	    // Listener para alteração no valor da barra de tempo
	    sliderTempo.valueProperty().addListener(new InvalidationListener() {
	        public void invalidated(Observable ov) {
	        	if(player != null) {
		            // Verifica se o usuário está pressionando a barra de tempo
		            if (sliderTempo.isPressed()) {
		                // Define a posição do vídeo conforme especificado pelo usuário ao pressionar a barra de tempo
		                // A posição é calculada multiplicando a duração total do vídeo pela porcentagem da barra de tempo
		                player.seek(player.getMedia().getDuration().multiply(sliderTempo.getValue() / 100));
		            }
	        	}
	        }
	    });
	    
	    // AListener para alteração no valor da barra de volume
	    sliderVolume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable ov) {
            	if(player != null) {
	                // Verifica se o Slider está sendo pressionado
	                if (sliderVolume.isPressed()) {
	                    // Define o volume do MediaPlayer com base no valor do Slider
	                    player.setVolume(sliderVolume.getValue() / 100);
	                    
	                    // Guarda o valor do volume para mante-lo em outras músicas
	                    volume = sliderVolume.getValue() / 100;
	                }
            	}
            }
        });
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
	 
	 @FXML
	 private void alternarPlayPause() {
		 if(player != null) {
			 // Pega o Estado do player
			 Status estado = player.getStatus();
			 
			 // Se a música estiver tocando (PLAYING) 
			 if(estado == Status.PLAYING) {
				 // Se a música já acabou
	             if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) { 
	            	 // Toca a música do começo
	                 player.seek(player.getStartTime());
	                 player.play();
	                 
	                 // Muda o ícone do botão
		             iconPlayPause.setIconLiteral("ti-control-pause");
	             } 
	             else { 
	            	 // Pausa a música
	    			 player.pause();
	    			 
	    			 // Muda para o ícone do botão
	    			 iconPlayPause.setIconLiteral("ti-control-play");
	             } 
			 }
			 
			 // Se a música estiver PAUSADA ou PARADA ou PRONTO para tocar.
			 if (estado == Status.HALTED || estado == Status.STOPPED || estado == Status.PAUSED || estado == Status.READY) {
				 // Toca a música
	             player.play();
	             
	             // Muda o ícone do botão
	             iconPlayPause.setIconLiteral("ti-control-pause");
	         }
		 }
	 }
	 
	 private String formatarTempo(Duration duration) {
	        int minutes = (int) duration.toMinutes();
	        int seconds = (int) duration.toSeconds() % 60;
	        return String.format("%d:%02d", minutes, seconds);
	 }
	 
	 // Atualiza os elementos que mostram os tempos da música
     private void atualizaValores() { 
         Platform.runLater(new Runnable() { 
             public void run() {
            	 // Posição do Slider de tempo
            	 sliderTempo.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
            	 
            	 // Label que mostra o tempo atual da música
            	 labelTempoAtual.setText(formatarTempo(player.getCurrentTime()));
            	 
            	 // Label que mostra a duração total da música
            	 labelDuracao.setText(formatarTempo(player.getTotalDuration()));
             } 
         }); 
     }
     
     @FXML
	 private void alternarMudo() {
    	 if(player != null) {
	    	 // Se estiver mutado
	    	 if (player.isMute()) {
	    		 // Desmuta o áudio
	             player.setMute(false);
	             
	             // Guarda para manter em todas as músicas.
	             estaMutado = false;
	             
	             // Muda para o ícone do botão
	             iconVolume.setIconLiteral("ti-volume");
	         } else {
	        	 // Muta o áudio
	             player.setMute(true);
	             
	             // Guarda para manter em todas as músicas.
	             estaMutado = true;
	             
	             // Muda para o ícone do botão
	             iconVolume.setIconLiteral("ti-close");
	         }
    	 }
     }
     
     private void tocarMusica(String caminho) {
    	// Se alguma música estiver carregada.
    	if (player != null) {
    		// Para a música.
    		player.stop();
    		
    		// Libera os recursos do player.
    		player.dispose();
        }
    	
    	// Pega o arquivo .mp3 no caminho informado
    	Media media = new Media(new File(caminho).toURI().toString());
 	    player = new MediaPlayer(media);
 	    
 	    // Quando estiver pronto, atualizar os valores dos elementos de tempo
        player.setOnReady(() -> {
            System.out.println("[DEBUG] Musica carregada e pronta para tocar.");
            atualizaValores();
            
            // "Clica" no botão de play automaticamente.
            alternarPlayPause();
        });
        
        // Quando chegar no fim da música, pausa e permite tocar novamente.
        player.setOnEndOfMedia(() -> {
        	// Pausa a música
			 player.pause();
			 
			 // Muda para o ícone do botão
			 iconPlayPause.setIconLiteral("ti-control-play");
        });
        
 	    // Seta um Listener para atualizar os valores dos elementos de tempo
        player.currentTimeProperty().addListener(new InvalidationListener() { 
            public void invalidated(Observable ov) 
            { 
                atualizaValores(); 
            } 
        });
        
        // Mantêm o volume setado em outra música
        if(volume != null) {
        	player.setVolume(volume);
        }
        
        // Mantêm o audio mutado em outra música
        if(estaMutado) {
        	player.setMute(true);
        }
     }
     
     @FXML
	 private void tocarAnterior() {
    	 // Pega a música anterior da playlist
    	 Musica musicaAnterior = playlistAtual.getAnterior();
    	 
    	 // Toca a música
         tocarMusica(musicaAnterior.getDiretorio());
         
         // Seleciona no ListView para mostrar ao usuário
         listViewMusicas.getSelectionModel().select(musicaAnterior);
     }
     
     @FXML
	 private void tocarProxima() {
    	 // Pega a próxima música da playlist
    	 Musica proximaMusica = playlistAtual.getProxima();
    	 
    	 // Toca a música
         tocarMusica(proximaMusica.getDiretorio());
         
         // Seleciona no ListView para mostrar ao usuário
         listViewMusicas.getSelectionModel().select(proximaMusica);
     }   
}