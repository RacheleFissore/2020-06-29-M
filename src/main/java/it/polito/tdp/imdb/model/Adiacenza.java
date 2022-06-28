package it.polito.tdp.imdb.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private Director vertice1;
	private Director vertice2;
	private int peso;
	public Adiacenza(Director vertice1, Director vertice2, int peso) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}
	public Director getVertice1() {
		return vertice1;
	}
	public void setVertice1(Director vertice1) {
		this.vertice1 = vertice1;
	}
	public Director getVertice2() {
		return vertice2;
	}
	public void setVertice2(Director vertice2) {
		this.vertice2 = vertice2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Adiacenza o) {
		return o.peso-this.peso;
	}
}
