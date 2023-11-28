package br.com.fxmusic.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DiretorioDao {
	// ArrayList com os diretórios
	private ArrayList<String> diretorios;
	
	// Instância única da classe
	private static DiretorioDao instance;
	
	// Caminho do arquivo .txt que contêm os diretorios cadastrados
	private static final String DIRETORIOS_TXT_PATH = "diretorios.txt";
	
	// Construtor privado para evitar a criação de instâncias fora da classe
    private DiretorioDao() {
        // Inicializações necessárias
    	diretorios = new ArrayList<String>();
    }
    
    public static DiretorioDao getInstance() {
		if(instance == null) {
			instance = new DiretorioDao();
		}
		
		return instance;
	}
    
    public ArrayList<String> getDiretorios() {
    	return diretorios;
    }
    
    public void addDiretorio(String path) {
    	diretorios.add(path);
    }
    
    // Lista os diretorios cadastrados no sistema
    public void listarDiretorios() {
    	System.out.println(diretorios.size() + " diretorios cadastrados.");
    	for(String path : diretorios) {
    		System.out.println(path);
    	}
    }
    
    // Salva os diretórios em um arquivo .txt de diretorios
    // [Posso criar uma exceção customizada no futuro]
    public void salvarDiretorios() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRETORIOS_TXT_PATH))) {
            for (String path : diretorios) {
                // Escreve uma linha para cada diretório
                writer.write(path);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Carrega os diretórios contidos no arquivo .txt para o sistema
    // [Posso criar uma exceção customizada no futuro]
    public void carregarDiretorios() {
    	// Zera a ArrayList para não somar com possíveis valores anteriores
    	if(diretorios.size() > 0) {
    		diretorios.clear();
    	}
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(DIRETORIOS_TXT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Supondo que cada linha do arquivo representa um diretorio
            	diretorios.add(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("[ERRO] Arquivo diretorio.txt não foi encontrado.");
        }
    }
}
