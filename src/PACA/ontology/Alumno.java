
package PACA.ontology;

import jade.content.Concept;
import jade.content.Predicate;

 
 
/**
   Clase que implementa el concepto Alumno de la ontología.
   Modificado Carlos Simón García 
*/
public class Alumno implements Concept{

    /**
       Identificador del alumno.
    */
    private String identificador;
	
    /**
       Clave del alumno.
    */
    private String password;


    // Constructores

    
    public Alumno (String id, String passwd) {
	
	identificador = id;
	password = passwd;

    }

    public Alumno (String id) {
	
	identificador = id;
	password = "";
    }

    public Alumno () {
	
	identificador = "";
	password = "";

    }


    // Observadoras
    
    public void setIdentificador (String id) {
	
	identificador = id;
    }

    public String getIdentificador ( ) {
	
	return identificador;

    }

    public void setPassword (String pass) {
	
	password = pass;

    }

    public String getPassword ( ) {

	return password;

    }
	
}
