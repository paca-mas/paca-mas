package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;

public class RegistrarEstadisticaEntrega implements AgentAction{


	private Alumno alumno1;
	private Alumno alumno2;
	private Practica practica;
	
	public RegistrarEstadisticaEntrega (Alumno a1, Alumno a2, Practica pract){
		this.alumno1 = a1;
		this.alumno2 = a2;
		this.practica = pract;
	}
	
	public void setAlumno1(Alumno alumno1){
		this.alumno1 = alumno1;
	}

	public Alumno getAlumno1(){
		return alumno1;
	}

	public void setAlumno2(Alumno alumno2){
		this.alumno2 = alumno2;
	}

	public Alumno getAlumno2(){
		return alumno2;
	}
	public void setPractica(Practica practica){
		this.practica = practica;
	}

	public Practica getPractica(){
		return practica;
	}
}