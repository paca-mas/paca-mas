
package PACA.ontology.Fichero;

 
import jade.content.Concept;

/**
 *  Modificado Carlos Sim�n Garc�a
 *
 */

public class Fichero implements Concept{
	

	private String Nombre;
	
	
	private String Contenido;
	
    public Fichero () {
	Nombre = "";
    }

    public Fichero ( String n) {
	Nombre = n;
    }
    
	
    public String getNombre( )
    {
	return Nombre;
    }
    
    
    public String getContenido( )
    {
	return Contenido;
    }
    
    
    public void setContenido( String contenido )
    {
	Contenido = contenido;
    }
    
    
    public void setNombre( String nombre )
    {
	Nombre = nombre;
    }
    
    
}
