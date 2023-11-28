package br.com.fxmusic.modelo;

import java.util.ArrayList;

public class Playlist {
	private String nome;
	private ArrayList<Musica> musicas;
	private int posicao = 0;
	
	public Playlist(ArrayList<Musica> musicas) {
		this.musicas = musicas;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	
	public Musica getAnterior() {
	    // Se existir uma música anterior
	    if (posicao - 1 >= 0) {
	        // Seta para retornar a música anterior na playlist.
	        posicao--;
	    } else {
	        // Se não, vai para o fim da lista.
	        posicao = musicas.size() - 1;
	    }

	    return musicas.get(posicao);
	}
	
	public Musica getMusicaAtual() {
		return musicas.get(posicao);
	}
	
	public Musica getProxima() {
		// Se existir uma próxima música
		if(posicao + 1 < musicas.size()) {
			// Seta para retornar a próxima música na playlist.
			posicao++;
		} else {
			// Se não, volta para o inicio.
			posicao = 0;
		}
		return musicas.get(posicao);
	}
}
