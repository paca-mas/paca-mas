


package es.urjc.ia.paca.ontology;
import jade.content.Predicate;


 
/**
   Clase que implementa el predicado EvaluacionPractica de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
*/
public class EvaluacionPractica implements Predicate{
    

    private Practica EvaluaPractica;
    
    
    private String textoEvaluacion;
    private Alumno alumno;
    
    
    public void setTextoEvaluacion( String texto )
    {
	textoEvaluacion = texto;
    }
    
    
    public String getTextoEvaluacion( )
    {
	return textoEvaluacion;
    }
    
    public void setAlumno(Alumno al1){
    	alumno = al1;
    }
    
    public Alumno getAlumno(){
    	return alumno;
    }
}
