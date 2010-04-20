


package es.urjc.ia.paca.ontology;

import jade.content.Predicate;



/**
   Clase que implementa el predicado Tests para la ontolog�a.
*/
public class Tests implements Predicate{
    private Test te;
    
    private Practica pract;

	
    public Test getTest( )
    {
	return te;
    }
    
    public void setTest( Test i )
    {
	te = i;
    }

    public Practica getPractica( )
    {
	return pract;
    }

    public void setPractica( Practica i )
    {
	pract = i;
    }
}
