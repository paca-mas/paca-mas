


package PACA.ontology;
import jade.content.Predicate;



/**
   Clase que implementa el predicado EvaluacionPractica de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
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
