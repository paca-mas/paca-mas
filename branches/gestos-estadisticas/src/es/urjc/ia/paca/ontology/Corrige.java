
package es.urjc.ia.paca.ontology;
import jade.content.Predicate;



/** 
   Clase que implementa el predicado Corrige de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
 */
public class Corrige implements Predicate{

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

}
