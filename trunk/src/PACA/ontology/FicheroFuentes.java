
package PACA.ontology;



import PACA.ontology.Fichero.*;
import jade.content.Predicate;



/**
   Clase que implementa el predicado FicheroFuentes de la ontología.
   Modificado Carlos Simón García
*/

public class FicheroFuentes implements Predicate{
    private Test te;
    
    private FuentesPrograma fp;
    
	
    public Test getTest( )
    {
	return te;
    }
    
    public void setTest( Test i )
    {
	te = i;
    }

    public FuentesPrograma getFuentesPrograma( )
    {
	return fp;
    }

    public void setFuentesPrograma( FuentesPrograma i )
    {
	fp = i;
    }
}
