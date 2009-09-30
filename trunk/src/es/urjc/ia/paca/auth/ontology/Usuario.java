package es.urjc.ia.paca.auth.ontology;

import jade.content.Concept;

/**
   Clase que modela un objeto Usuario.
   @author Sergio Saugar Garc�a
   Modificacion Carlos Sim�n Garc�a
 */
public class Usuario implements Concept{
 
	/**
       Identificador del usuario.
	 */
	private String user_id;

	/**
       Clave de acceso del usuario.
	 */
	private String password;


	public String getUser_id(){
		return user_id;
	}

	public void setUser_id(String id){
		user_id=id;
	}




	public String getPassword(){
		return password;
	}

	public void setPassword(String pass){
		password=pass;
	}

}
