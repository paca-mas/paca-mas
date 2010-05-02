package es.urjc.ia.paca.ontology;

import jade.content.Predicate;

public class EstadisticaEvaluacionCaso implements Predicate{

	private Alumno alumno;
	private Practica practica;
	private Test test;
	private Caso caso;
	private String evaluacion;
	
	public void setAlumno(Alumno alumno){
		this.alumno = alumno;
	}

	public Alumno getAlumno(){
		return alumno;
	}
	
	public void setPractica(Practica practica){
		this.practica = practica;
	}

	public Practica getPractica(){
		return practica;
	}
	
	public void setTest(Test test){
		this.test = test;
	}

	public Test getTest(){
		return test;
	}
	
	public void setCaso(Caso caso){
		this.caso = caso;
	}

	public Caso getCaso(){
		return caso;
	}

	public void setEvaluacion(String evaluacion){
		this.evaluacion = evaluacion;
	}

	public String getEvaluacion(){
		return evaluacion;
	}
}
