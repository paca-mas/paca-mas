


package PACA.ontology;



import jade.core.AID;
import jade.content.Predicate;



/**
   Clase que implementa el predicado Interactua de la ontología.
   Modificado Carlos Simón García
*/
public class Interactua implements Predicate {
    
    private Alumno alumno;

    private AID interfaz;
    
	
    public Alumno getAlumno( )
    {
	return alumno;
    }
    
    public void setAlumno( Alumno i )
    {
	alumno = i;
    }

    public AID getInterfaz( )
    {
	return interfaz;
    }

    public void setInterfaz( AID i )
    {
	interfaz = i;
    }


    public Alumno get_0( )
    {
	return getAlumno();
    }
    
    public void set_0( Alumno i )
    {
	setAlumno(i);
    }

    public AID get_1( )
    {
	return getInterfaz();
    }

    public void set_1( AID i )
    {
	setInterfaz(i);
    }
}
