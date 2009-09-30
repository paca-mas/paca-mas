
package es.urjc.ia.paca.ontology;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.abs.AbsPredicate;



/**
   Clase que implementa la acci�n EntregarPr�ctica de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
*/

public class EntregarPractica implements AgentAction{
    private Corrector correc;
    
    private Practica pract;
    
    private AbsPredicate entrega;

	
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

    public AbsPredicate getEntrega() {
		return entrega;
	}

	public void setEntrega(AbsPredicate entrega) {
		this.entrega = entrega;
	}
}
