
package PACA.ontology;

import jade.content.Predicate;



/**
   Clase que implementa la acci�n EntregarPr�ctica de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
*/

public class EntregarPractica implements Predicate{
    private Corrector correc;
    
    private Practica pract;

	
    public Corrector getCorrector( )
    {
	return correc;
    }
    
    public void setCorrector( Corrector i )
    {
	correc = i;
    }

    public Practica getPractica( )
    {
	return pract;
    }

    public void setPractica( Practica i )
    {
	pract = i;
    }

    public Corrector get_0( )
    {
	return getCorrector();
    }

    public void set_0( Corrector i )
    {
	setCorrector(i);
    }

    public Practica get_1( )
    {
	return getPractica();
    }

    public void set_1 ( Practica i )
    {
	setPractica(i);
    }
}
