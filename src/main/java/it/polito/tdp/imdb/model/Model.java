package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	private Graph<Director, DefaultWeightedEdge> grafo;
	private List<Director> best;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		
		for(Director director : dao.listAllDirectors()) {
			idMap.put(director.getId(), director);
		}
	}
	
	public List<Integer> getAnni() {
		return dao.getAnni();
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, anno));
		
		for(Adiacenza adiacenza : dao.getArchi(idMap, anno)) {
			Graphs.addEdgeWithVertices(grafo, adiacenza.getVertice1(), adiacenza.getVertice2(), adiacenza.getPeso());
		}
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
		 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Director> getVertici() {
		return new ArrayList<>(grafo.vertexSet());
	}
	
	public String registiAdiacenti(Director regista) {
		List<Adiacenza> registiAdiacenti = new ArrayList<>();
		
		for(Director director : Graphs.neighborListOf(grafo, regista)) {
			registiAdiacenti.add(new Adiacenza(regista, director, (int)grafo.getEdgeWeight(grafo.getEdge(regista, director))));
		}
		
		Collections.sort(registiAdiacenti);
		String stampa = "";
		
		for(Adiacenza adiacenza : registiAdiacenti) {
			stampa += adiacenza.getVertice2() + " - # attori condivisi: " + adiacenza.getPeso() + "\n";
		}
		return stampa;
	}
	
	public String trovaPercorso(int attoriCondivisi, Director partenza) {
		String stampa = "";
		List<Director> parziale = new ArrayList<>();
		best = new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale, attoriCondivisi);
		
		for(Director director : best) {
			stampa += director + "\n";
		}
		
		return stampa;
	}

	private void cerca(List<Director> parziale, int attoriCondivisi) {
		if(parziale.size() > best.size()) {
			if(calcolaPeso(parziale) <= attoriCondivisi) {
				best = new ArrayList<>(parziale);
			}
			else {
				return;
			}
		}
		
		for(Director director : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(director)) {
				parziale.add(director);
				cerca(parziale, attoriCondivisi);
				parziale.remove(director);
			}
		}
		
	}

	private int calcolaPeso(List<Director> parziale) {
		int peso = 0;
		
		for(int i=1; i < parziale.size(); i++) {
			peso += (int)grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1), parziale.get(i)));
		}
		return peso;
	}
}
