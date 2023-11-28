package br.com.fxmusic.modelo;

public class Musica {
	private String nome;
	private String diretorio;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDiretorio() {
		return diretorio;
	}
	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
