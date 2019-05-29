package it.polito.tdp.formulaone.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	SimpleDirectedWeightedGraph<Constructor, DefaultWeightedEdge> grafo;
	Map<Integer, Constructor> idMap;
	FormulaOneDAO dao;
	
	public Model() {
		dao= new FormulaOneDAO();
		grafo= new SimpleDirectedWeightedGraph<Constructor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap= new HashMap<Integer, Constructor>();
		dao.getAllConstructors(idMap);
	}
	
	
	public List<Circuit> getAllCircuit() {
		
		return dao.getAllCircuits();
	}
	
	public String creaGrafo(Circuit c) {
		
		List<Adiacenza> adj= dao.getAdiacenze(c,idMap);
		for(Adiacenza a: adj) {
			
			grafo.addVertex(a.getC1());
			grafo.addVertex(a.getC2());			
			
			DefaultWeightedEdge edge= grafo.getEdge(a.getC1(), a.getC2());
			if (edge == null) {
				Graphs.addEdge(grafo, a.getC1(), a.getC2(), a.getPeso());
			} else {
				grafo.setEdgeWeight(edge, a.getPeso());
			}
		}
		System.out.println(grafo.vertexSet().size() +" vertici " + grafo.edgeSet().size() +" archi " );
		
		Constructor migliore= null;
		String ris="";
		double max=0;
		for(Constructor c1: grafo.vertexSet()) {
			double sum=0;
			for(DefaultWeightedEdge arco: grafo.outgoingEdgesOf(c1)) {
				sum+= grafo.getEdgeWeight(arco);
			}
			for(DefaultWeightedEdge arco: grafo.incomingEdgesOf(c1)) {
				sum-=grafo.getEdgeWeight(arco);
			}
			if(sum>max) {
				max= sum;
				migliore=c1;
				ris= migliore.toString();
			}
		
		}
		
		
		return ris;}

}
