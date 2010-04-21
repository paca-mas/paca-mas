package es.urjc.ia.paca.util;

import jade.core.AID;

//Clase que crea un objeto con el nombre del corrector y el numero de correciones
public class EstadoCorrector implements Comparable{

	private AID identificador;
	private Integer numeroCorrecciones;

  
	// Constructores
	public EstadoCorrector (AID id, Integer num) {
		identificador = id;
		numeroCorrecciones = num;
	}

	public EstadoCorrector () {
		identificador = null;
		numeroCorrecciones = 0;
	}

	
	
	public AID getIdentificador() {
		return identificador;
	}

	public void setIdentificador(AID identificador) {
		this.identificador = identificador;
	}

	public Integer getNumeroCorrecciones() {
		return numeroCorrecciones;
	}

	public void setNumeroCorrecciones(Integer numeroCorrecciones) {
		this.numeroCorrecciones = numeroCorrecciones;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		EstadoCorrector estadoCorr = (EstadoCorrector) o;
	    return numeroCorrecciones.compareTo(estadoCorr.getNumeroCorrecciones());
		
	}
	
	public String toString(){
		return "Corrector "+this.getIdentificador()+" NumeroCorreciones: "+getNumeroCorrecciones();
	}
	
	


}

