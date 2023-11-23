package br.com.fxmusic.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.com.fxmusic.modelo.Usuario;

public class UsuarioDao {
	// ArrayList com os usuários
	private ArrayList<Usuario> usuarios;
	
	// Instância única da classe
	private static UsuarioDao instance;
	
	// Caminho do arquivo .txt que contêm os usuários cadastrados
	private static final String USUARIOS_TXT_PATH = "usuarios.txt";
	
	// Construtor privado para evitar a criação de instâncias fora da classe
    private UsuarioDao() {
        // Inicializações necessárias
    	usuarios = new ArrayList<Usuario>();
    }
    
    public static UsuarioDao getInstance() {
		if(instance == null) {
			instance = new UsuarioDao();
		}
		
		return instance;
	}
    
    // Retorna o próximo id disponível para cadastro de novo usuário.
    private int getIdDisponivel() {
    	return usuarios.size();
    }
    
    // Adiciona um usuário ao sistema
    public void addUsuario(String nome, String login, String senha) {
    	Usuario user = new Usuario();
    	
    	user.setId(getIdDisponivel());
    	user.setNome(nome);
    	user.setLogin(login);
    	user.setSenha(senha);
    	
    	// Adiciona o usuário criado no sistema.
    	usuarios.add(user);
    }
    
    // Verifica se o login e senha informada pertence a um usuário
    // [Posso criar uma exceção customizada no futuro]
    public Usuario autenticarUsuario(String login, String senha) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login) && usuario.getSenha().equals(senha)) {
                return usuario; // Usuário autenticado
            }
        }
        return null; // Usuário não encontrado ou senha incorreta
    }
    
    // Lista os usuários cadastrados no sistema
    public void listarUsuarios() {
    	System.out.println(usuarios.size() + " usuarios cadastrados.");
    	for(Usuario u : usuarios) {
    		System.out.println("ID: " + u.getId() + "\n" +
    						   "NOME: " + u.getNome() + "\n" +
    						   "LOGIN: " + u.getLogin() + "\n" +
    						   "SENHA: " + u.getSenha());
    	}
    }
    
    // Salva os usuários em um arquivo .txt de usuários
    // [Posso criar uma exceção customizada no futuro]
    public void salvarUsuarios() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(USUARIOS_TXT_PATH))) {
            for (Usuario u : usuarios) {
                // Escreve o login e senha separados por ","
                writer.write(u.getId() + "," + 
                			 u.getNome() + "," + 
                			 u.getLogin() + "," + 
                			 u.getSenha());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Carrega os usuários contidos no arquivo .txt para o sistema
    // [Posso criar uma exceção customizada no futuro]
    public void carregarUsuarios() {
    	try (BufferedReader reader = new BufferedReader(new FileReader(USUARIOS_TXT_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Supondo que cada linha do arquivo representa um usuário com login e senha separados por ","
                String[] dadosUsuario = line.split(",");
                if (dadosUsuario.length == 4) {
                	Usuario user = new Usuario();
                	
                	user.setId(Integer.parseInt(dadosUsuario[0]));
                	user.setNome(dadosUsuario[1]);
                	user.setLogin(dadosUsuario[2]);
                	user.setSenha(dadosUsuario[3]);
                	
                	// Adiciona o usuário criado no sistema.
                	usuarios.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
