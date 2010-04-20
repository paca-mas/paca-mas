package es.urjc.ia.paca.auth.ontology;

import jade.content.Predicate;



/**
   Predicado que relaciona un identificador de usuario con su password, y su semantica es
   que ese usuario est� autenticado.
   @author Sergio Saugar Garc�a
   Modificado Carlos Sim�n Garc�a
 */
public class Autenticado implements Predicate{
//public class Autenticado{

//Carlos

    /**
       Atributo usuario.
    */
    private Usuario user;
    
    public void setUsuario(Usuario u) {
	user = u;
    }

    public Usuario getUsuario() {
	return user;
    }
}
