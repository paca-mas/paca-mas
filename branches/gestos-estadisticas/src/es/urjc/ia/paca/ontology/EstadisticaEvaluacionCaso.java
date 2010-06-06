package es.urjc.ia.paca.ontology;

import jade.content.Concept;

public class EstadisticaEvaluacionCaso implements Concept {

	private Alumno alumno;
	private Practica practica;
	private Test test;
	private Caso caso;
	private String evaluacion;

	  //Constructor de la clase
    public EstadisticaEvaluacionCaso(Alumno alumno, Practica practica,
    		Test test, Caso caso, String evaluacion){
    	this.alumno =  alumno;
    	this.practica = practica;
    	this.test = test;
    	this.caso = caso;
    	this.evaluacion = evaluacion;
    }
    
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