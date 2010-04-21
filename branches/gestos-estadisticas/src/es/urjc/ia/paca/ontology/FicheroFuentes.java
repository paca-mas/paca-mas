
package es.urjc.ia.paca.ontology;



import es.urjc.ia.paca.ontology.Fichero.*;
import jade.content.Predicate;



/**
   Clase que implementa el predicado FicheroFuentes de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
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
