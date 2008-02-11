


package PACA.ontology;
import jade.content.Predicate;



/**
   Clase que implementa el predicado EvaluacionPractica de la ontología.
   Modificado Carlos Simón García
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
