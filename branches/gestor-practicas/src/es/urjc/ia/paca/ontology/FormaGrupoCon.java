


package es.urjc.ia.paca.ontology;

import jade.content.Predicate;



/**
   Clase que implementa el predicado FormaGrupoCon de la ontolog�a.
*/
public class FormaGrupoCon implements Predicate {
    
    private Alumno alumno1;
    
    private Alumno alumno2;
    
    
    public Alumno getAlumno1( )
    {
	return alumno1;
    }
    
    public void setAlumno1( Alumno i )
    {
	alumno1 = i;
    }

    public Alumno getAlumno2( )
    {
	return alumno2;
    }

    public void setAlumno2( Alumno i )
    {
	alumno2 = i;
    }

    public Alumno get_0 ( ) 
    {
	return getAlumno1();
    }

    public void set_0 ( Alumno i )
    {
	setAlumno1(i);
    }

    public Alumno get_1 ( ) 
    {
	return getAlumno2();
    }

    public void set_1 ( Alumno i )
    {
	setAlumno2(i);
    }
}
