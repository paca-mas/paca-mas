package PACA.ontology;



import jade.content.Concept;



/**
   Clase que implementa el concepto Test de la ontolog�a.
   Modificado Carlos Sim�n Garc�a
*/
public class Test implements Concept{
    private String Id;
    private String Descripcion;
    
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
