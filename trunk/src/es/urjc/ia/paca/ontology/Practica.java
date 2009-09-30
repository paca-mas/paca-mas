package es.urjc.ia.paca.ontology;

import jade.content.Concept;



/**
   Clase que implementa el concepto Practica de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
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
        
        public Practica (String _id, String _description) {
            this.Id = _id;
            this.Descripcion = _description;
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
