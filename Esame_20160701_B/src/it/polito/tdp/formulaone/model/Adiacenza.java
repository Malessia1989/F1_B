package it.polito.tdp.formulaone.model;

public class Adiacenza {
	
	Constructor c1;
	Constructor c2;
	double peso;
	
	public Adiacenza(Constructor c1, Constructor c2, double peso) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.peso = peso;
	}
	public Constructor getC1() {
		return c1;
	}
	public void setC1(Constructor c1) {
		this.c1 = c1;
	}
	public Constructor getC2() {
		return c2;
	}
	public void setC2(Constructor c2) {
		this.c2 = c2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	

}
