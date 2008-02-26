
package PACA.ontology;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.abs.AbsPredicate;



/**
   Clase que implementa la acción EntregarPráctica de la ontología.
   Modificado Carlos Simón García
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
