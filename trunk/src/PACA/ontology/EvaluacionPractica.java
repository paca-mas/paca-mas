


package PACA.ontology;
import jade.content.Predicate;



/**
   Clase que implementa el predicado EvaluacionPractica de la ontología.
   Modificado Carlos Simón García
*/
public class EvaluacionPractica implements Predicate{
    

    private Practica EvaluaPractica;
    
    
    private String textoEvaluacion;
    
    
    public void setTextoEvaluacion( String texto )
    {
	textoEvaluacion = texto;
    }
    
    
    public String getTextoEvaluacion( )
    {
	return textoEvaluacion;
    }
    
    
}
