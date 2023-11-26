package br.com.fxmusic.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.com.fxmusic.modelo.Musica;

public class MusicaDao {
	// ArrayList com os usuários
	private ArrayList<Musica> musicas;
	
	// Instância única da classe
	private static MusicaDao instance;
	
	// Caminho do arquivo .txt que contêm as músicas cadastradas
	private static final String MUSICAS_TXT_PATH = "musicas.txt";
	
	// Construtor privado para evitar a criação de instâncias fora da classe
    private MusicaDao() {
        // Inicializações necessárias
    	musicas = new ArrayList<Musica>();
    }
    
    public static MusicaDao getInstance() {
		if(instance == null) {
			instance = new MusicaDao();
		}
		
		return instance;
	}
    
    public ArrayList<Musica> getMusicas() {
    	return musicas;
    }
    
    public void listarMusicas() {
    	for(Musica m : musicas) {
    		System.out.println("Nome: " + m.getNome() + "\n" +
    						   "Caminho: " + m.getDiretorio());
    	}
    }
    
    // Procura por músicas nos diretórios informados e os guarda na ArrayList de musicas.
    public void buscarMusicas(ArrayList<String> diretorios) {
    	// Zera a ArrayList para não somar com possíveis valores anteriores
    	if(!musicas.isEmpty()) {
    		musicas.clear();
    	}
    	
    	for (String caminho : diretorios) {
            File pasta = new File(caminho);
            if (pasta.exists() && pasta.isDirectory()) {
                File[] files = pasta.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));

                if (files != null) {
                    for (File file : files) {
                    	// Nome do arquivo sem o .mp3
                        String nomeMusica = file.getName().replaceFirst("[.][^.]+$", "");
                        String caminhoMusica = file.getAbsolutePath();

                        Musica musica = new Musica();
                        musica.setNome(nomeMusica);
                        musica.setDiretorio(caminhoMusica);
                        
                        musicas.add(musica);
                    }
                }
            }
        }
    }
    
    public void salvarMusicas() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(MUSICAS_TXT_PATH))) {
            for (Musica m : musicas) {
                // Escreve o login e senha separados por ","
                writer.write(m.getDiretorio());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Carrega as músicas contidas no arquivo .txt para o sistema
    // [Posso criar uma exceção customizada no futuro]
    public void carregarMusicas() {
    	// Zera a ArrayList para não somar com possíveis valores anteriores
    	if(musicas.size() > 0) {
    		musicas.clear();
    	}
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(MUSICAS_TXT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	// Nome do arquivo sem o .mp3
            	String nomeMusica = new File(line).getName().replaceFirst("[.][^.]+$", "");
                String caminhoMusica = line;
            	
                Musica musica = new Musica();
                musica.setNome(nomeMusica);
                musica.setDiretorio(caminhoMusica);
                
                musicas.add(musica);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
