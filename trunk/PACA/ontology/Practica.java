package PACA.ontology;

import jade.content.Concept;



/**
   Clase que implementa el concepto Practica de la ontología.
   Modificado Carlos Simón García
 */
public class Practica implements Concept{
	private String Id; 

	private String Descripcion;

	public Practica () {
		Id = "";
	}

	public Practica ( String nombre) {
		Id = nombre;
	}

	public String getId( ) 
	{
		return Id;
	} 

	public void setId( String i )
	{
		Id = i;
	}

	public String getDescripcion( )
	{
		return Descripcion;
	}

	public void setDescripcion( String i )
	{
		Descripcion = i;
	}


}
