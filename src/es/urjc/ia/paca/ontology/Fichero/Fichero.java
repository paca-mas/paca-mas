package es.urjc.ia.paca.ontology.Fichero;

import jade.content.Concept;

/**
 *  Modificado Carlos Sim�n Garc�a
 *
 */
public class Fichero implements Concept {

	private String Nombre;
	private String Contenido;

	public Fichero() {
		Nombre = "";
		this.Contenido = "";
	}

	public Fichero(String n) {
		Nombre = n;
		this.Contenido = "";
	}

	public String getNombre() {
		return Nombre;
	}

	public String getContenido() {
		return Contenido;
	}

	public void setContenido(String contenido) {
		Contenido = contenido;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}
}
